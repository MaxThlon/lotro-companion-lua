package delta.games.lotro.lua;

import static org.squiddev.cobalt.ValueFactory.valueOf;
import static org.squiddev.cobalt.ValueFactory.varargsOf;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaString;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaThread;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.ValueFactory;
import org.squiddev.cobalt.Varargs;
import org.squiddev.cobalt.compiler.CompileException;
import org.squiddev.cobalt.compiler.LoadState;
import org.squiddev.cobalt.function.LuaClosure;
import org.squiddev.cobalt.interrupt.InterruptAction;
import org.squiddev.cobalt.lib.Bit32Lib;
import org.squiddev.cobalt.lib.system.SystemLibraries;

import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.utils.URLToolsLua;

/**
 * LuaRunner for lua scripts.
 * 
 * @author MaxThlon
 */
public class LuaRunner {
  private static Logger LOGGER = Logger.getLogger(LuaRunner.class);

  /**
   * The amount of time this runner used to work to evenly amongst runners.
   */
  long runtime = 0;
  
  private TimeoutState timeout;
  private final Runnable timeoutListener = this::updateTimeout;
  
  private LuaState state;
  private LuaThread mainRoutine;
  
  private volatile boolean isDisposed = false;
  private boolean thrownSoftAbort;
  
  private String eventFilter = null;

  public LuaRunner(InputStream script, LuaString name) throws LuaError {
    timeout = new TimeoutState();
    
    // Create an environment to run in
    this.state = LuaState.builder()
        .interruptHandler(() -> {
          if (timeout.isHardAborted() || isDisposed) throw new HardAbortError();
          if (timeout.isSoftAborted() && !thrownSoftAbort) {
            thrownSoftAbort = true; throw new LuaError(TimeoutState.ABORT_MESSAGE);
          }
          return timeout.isPaused() ? InterruptAction.SUSPEND : InterruptAction.CONTINUE; 
        })
        .errorReporter((e, msg) -> {
          LOGGER.error("Error occurred in the Lua runtime. Script will continue to execute: " + msg.get());
        })
        .build();
    
    LuaTable globals = state.getMainThread().getfenv();
    SystemLibraries.debugGlobals(state);
    Bit32Lib.add(state, globals);
    URLToolsLua.add(state, globals);
    Turbine.add(state, globals);
    LuaLotroCompanion.add(state, globals);
    //globals.load(new LuaJSocketLib());
    
    try {
      LuaTable packageLib = globals.rawget("package").checkTable();
      packageLib.rawset("path", 
          valueOf("?.lua;" + 
              URLToolsLua.getFromClassPath("test") + "/?.lua;" +
              URLToolsLua.getFromClassPath("translate") + "/?.lua;" +
              URLToolsLua.getFromClassPath("thirdpart") + "/?.lua")
      );
      
      String FILE_SEP = System.getProperty("file.separator");
      packageLib.rawset("config", valueOf(FILE_SEP + "\n;\n?\n!\n-\n"));
    } catch (LuaError e1) {    
      e1.printStackTrace();
    }
    
    try {
      LuaClosure value = LoadState.load(state, script, name, globals);
      mainRoutine = new LuaThread(state, value, globals);
    } catch (CompileException e) {
      LOGGER.error("Message: " + e.getMessage());
    }
    
    timeout.addListener(timeoutListener);
  }

  private void updateTimeout() {
    if (isDisposed) return;
    if (!timeout.isSoftAborted()) thrownSoftAbort = false;
    if (timeout.isSoftAborted() || timeout.isPaused()) state.interrupt();
  }
  
  public void handleEvent(String eventName, Object[] arguments) {
    if (isDisposed) throw new IllegalStateException("LuaRunner has been closed");
    
    if (eventFilter != null && eventName != null && !eventName.equals(eventFilter) && !eventName.equals("terminate")) {
      //return MachineResult.OK;
    }
    
    try {
      Varargs resumeArgs = eventName == null ? Constants.NONE : varargsOf(valueOf(eventName), toValues(arguments));
      
      // Resume the current thread, or the main one when first starting off.
      LuaThread thread = state.getCurrentThread();
      if (thread == null || thread == state.getMainThread()) thread = mainRoutine;
      
      Varargs results = LuaThread.run(thread, resumeArgs);
      
      LuaValue filter = results.first();
      eventFilter = filter.isString() ? filter.toString() : null;
      
      if (!mainRoutine.isAlive()) {
        close();
        // return MachineResult.GENERIC_ERROR;
      }
    } catch (LuaError luaError) {
        close();
        LOGGER.warn("Top level coroutine errored: {}", /*new SanitisedError(*/luaError/*)*/);
        //return MachineResult.error(e);
    }
  }

  public void close() {
    state.interrupt();
  }
  
  @SuppressWarnings("boxing")
  private LuaValue toValue(Object object, IdentityHashMap<Object, LuaValue> values) throws LuaError {
    if (object == null) return Constants.NIL;
    if (object instanceof Number) return ValueFactory.valueOf(((Number)object).doubleValue());
    if (object instanceof Boolean) return ValueFactory.valueOf((Boolean)object);
    if (object instanceof String) return ValueFactory.valueOf((String)object);
    if (object instanceof byte[]) {
      byte[] b = (byte[])object;
      return ValueFactory.valueOf(Arrays.copyOf(b, b.length));
    }
    if (object instanceof ByteBuffer) {
      ByteBuffer b = (ByteBuffer)object;
      byte[] bytes = new byte[b.remaining()];
        b.get(bytes);
        return ValueFactory.valueOf(bytes);
    }

    if (values == null) values = new IdentityHashMap<>(1);
    LuaValue result = values.get(object);
    if (result != null) return result;

    if (object instanceof Map<?, ?>) {
      Map<?, ?> map = (Map<?, ?>)object;
      LuaTable table = new LuaTable();
      values.put(object, table);

      for (Map.Entry<?, ?> pair : map.entrySet()) {
        LuaValue key = toValue(pair.getKey(), values);
        LuaValue value = toValue(pair.getValue(), values);
        if (!key.isNil() && !value.isNil()) table.rawset(key, value);
      }
      return table;
    }

    if (object instanceof Collection<?>) {
      Collection<?> objects = (Collection<?>)object;
      LuaTable table = new LuaTable(objects.size(), 0);
      values.put(object, table);
      Integer i = 0;
      for (Object child : objects) table.rawset(++i, toValue(child, values));
      return table;
    }

    if (object instanceof Object[]) {
      Object[] objects = (Object[])object;
      LuaTable table = new LuaTable(objects.length, 0);
      values.put(object, table);
      for (Integer i = 0; i < objects.length; i++) table.rawset(i + 1, toValue(objects[i], values));
      return table;
    }

    /*LuaTable wrapped = wrapLuaObject(object);
    if (wrapped != null) {
        values.put(object, wrapped);
        return wrapped;
    }*/

    LOGGER.warn("Received unknown type '{}', returning nil.");
    return Constants.NIL;
  }
  
  /*private LuaTable wrapLuaObject(Object object) {
    LuaTable table = new LuaTable();
    var found = luaMethods.forEachMethod(object, (target, name, method, info) ->
        table.rawset(name, info != null && info.nonYielding()
            ? new BasicFunction(this, method, target, context, name)
            : new ResultInterpreterFunction(this, method, target, context, name)));

    return found ? table : null;
  }*/
  
  Varargs toValues(Object[] objects) throws LuaError {
    if (objects == null || objects.length == 0) return Constants.NONE;
    if (objects.length == 1) return toValue(objects[0], null);

    IdentityHashMap<Object, LuaValue> result = new IdentityHashMap<Object, LuaValue>(0);
    LuaValue[] values = new LuaValue[objects.length];
    for (int i = 0; i < values.length; i++) {
      Object object = objects[i];
        values[i] = toValue(object, result);
    }
    return ValueFactory.varargsOf(values);
  }
  
  private static final class HardAbortError extends Error {
    private static final long serialVersionUID = 7954092008586367501L;

    private HardAbortError() {
        super("Hard Abort");
    }
}
}
