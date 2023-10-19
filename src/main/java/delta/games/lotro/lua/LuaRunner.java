package delta.games.lotro.lua;

import static org.squiddev.cobalt.ValueFactory.valueOf;
import static org.squiddev.cobalt.ValueFactory.tableOf;
import static org.squiddev.cobalt.ValueFactory.userdataOf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaString;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaThread;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.ValueFactory;
import org.squiddev.cobalt.Varargs;
import org.squiddev.cobalt.compiler.CompileException;
import org.squiddev.cobalt.compiler.LoadState;
import org.squiddev.cobalt.function.LuaClosure;
import org.squiddev.cobalt.interrupt.InterruptAction;
import org.squiddev.cobalt.lib.Bit32Lib;
import org.squiddev.cobalt.lib.system.SystemLibraries;

import com.eleet.dragonconsole.CommandProcessor;

import delta.common.framework.plugin.PluginEvent;
import delta.common.framework.plugin.PluginImpl;
import delta.common.framework.plugin.PluginResult;
import delta.common.framework.plugin.TimeoutState;
import delta.games.lotro.client.plugin.Plugin;
import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.turbine.plugin.LuaPlugin;
import delta.games.lotro.lua.utils.URLToolsLua;

/**
 * LuaRunner for lua scripts.
 * 
 * @author MaxThlon
 */
public class LuaRunner implements PluginImpl {
  private static Logger LOGGER = Logger.getLogger(LuaRunner.class);

  private TimeoutState _timeout;
  private final Runnable _timeoutListener = this::updateTimeout;
  
  private LuaState _state;
  private LuaThread _mainRoutine=null;
  
  private volatile boolean _isDisposed = false;
  //private boolean _thrownSoftAbort;
  
  private CommandProcessor _commandProcessor;

  public LuaRunner(CommandProcessor commandProcessor) {
    _commandProcessor=commandProcessor;
    _timeout = new TimeoutState();
    
    // Create an environment to run in
    _state = LuaState.builder()
        .interruptHandler(() -> {
          /*if (_timeout.isHardAborted() || _isDisposed) throw new HardAbortError();
          if (_timeout.isSoftAborted() && !_thrownSoftAbort) {
            _thrownSoftAbort = true; throw new LuaError(TimeoutState.ABORT_MESSAGE);
          }
          return _timeout.isPaused() ? InterruptAction.SUSPEND : InterruptAction.CONTINUE;*/
          return InterruptAction.SUSPEND;
        })
        .errorReporter((e, msg) -> {
          LOGGER.error("Error occurred in the Lua runtime. Script will continue to execute: " + msg.get());
        })
        .build();
    
    LuaTable globals = _state.getMainThread().getfenv();
    try
    {
      SystemLibraries.debugGlobals(_state);
      Bit32Lib.add(_state, globals);

    } catch (LuaError error) {
      LOGGER.error("Message: " + error.getMessage());
    }

    _timeout.addListener(_timeoutListener);
  }
  
  public void initPackageLib(Path... paths) {
    LuaTable globals = _state.getMainThread().getfenv();
    try
    {
      LuaTable packageLib = globals.rawget("package").checkTable();

      packageLib.rawset("path", valueOf(
          Stream.concat(
            Stream.concat(
                Stream.of(""),
                Stream.ofNullable(paths).flatMap(Arrays::stream).map(path -> path.toString())
            ),
            Stream.of(
                LuaRunner.class.getResource("").getPath(),
                URLToolsLua.getFromClassPath("thirdpart")
            )
          ).map(path -> path + "/?.lua;" + path +"/?/__init__.lua")
           .collect(Collectors.joining (";"))
         )
     );

      String FILE_SEP = System.getProperty("file.separator");
      packageLib.rawset("config", valueOf(FILE_SEP + "\n;\n?\n!\n-\n"));
    } catch (LuaError error) {
      LOGGER.error("initPackageLib: {}", error);
    }    
  }

  public void bootstrapLotro(String scriptFilename, InputStream script) {
    LuaTable globals = _state.getMainThread().getfenv();
    try {
      URLToolsLua.add(_state, globals);
      Turbine.add(_state, globals);
      Turbine.import_lua(_state, LuaString.valueOf("Turbine.Enum"));

      LuaClosure luaClosure = LoadState.load(_state, script, scriptFilename, globals);
      _mainRoutine = new LuaThread(_state, luaClosure, globals);
    } catch (CompileException|LuaError | UnwindThrowable error) {
      LOGGER.error("bootstrapLotro: {}", error);
    }
  }

  public void bootstrapSandBoxedLotro() /*String scriptFilename, InputStream script)*/ {
    LuaTable globals = _state.getMainThread().getfenv();

    try {
      URLToolsLua.add(_state, globals);
      Turbine.add(_state, globals);
      
      LuaClosure luaClosure = LoadState.load(
          _state,
          new FileInputStream(URLToolsLua.getFromClassPath("turbine-thread.lua")),
          "turbine-thread",
          globals
      );
      _mainRoutine = new LuaThread(_state, luaClosure, globals);
    } catch (LuaError | FileNotFoundException | CompileException error) {
      LOGGER.error("bootstrapSandBoxedLotro: {}", error);
    }
  }

  private void updateTimeout() {
    if (_isDisposed) return;
    //if (!_timeout.isSoftAborted()) _thrownSoftAbort = false;
    if (_timeout.isSoftAborted() || _timeout.isPaused()) _state.interrupt();
  }

  @Override
  public PluginResult handleEvent(PluginEvent event) {
    if (_isDisposed) throw new IllegalStateException("LuaRunner has been closed");

    try {
      // Resume the current thread, or the main one when first starting off.
      LuaThread thread=_state.getCurrentThread();
      if ((thread == null) || (thread == _state.getMainThread())) thread=_mainRoutine;
      Varargs results = LuaThread.run(thread, (event == null)?Constants.NONE : userdataOf(event));
      if (results == null) return PluginResult.PAUSE;

      if (!_mainRoutine.isAlive()) {
        close();
        return PluginResult.GENERIC_ERROR;
      }
      return PluginResult.OK;
    } catch (LuaError error) {
        close();
        LOGGER.warn("Top level coroutine errored: {}", /*new SanitisedError(*/error/*)*/);
        return PluginResult.error(error);
    }
  }
  
  @Override
  public void close() {
      _isDisposed = true;
      _state.interrupt();
      _timeout.removeListener(_timeoutListener);
  }

  @Override
  public CommandProcessor getCommandProcessor()
  {
    return _commandProcessor;
  }
  
  @SuppressWarnings("boxing")
  private LuaValue toValue(Object object, IdentityHashMap<Object, LuaValue> values) throws LuaError, UnwindThrowable {
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

    if (object instanceof Plugin) {
      LuaValue result = Turbine.findLuaObjectFromObject(object);
      if (result == null) {
        result = LuaPlugin.newLuaPlugin(_state, (Plugin)object);
      }
      return result;
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

    LOGGER.warn("Received unknown type '{}', returning nil.");
    return Constants.NIL;
  }
  
  Varargs toValues(Object[] objects) throws LuaError, UnwindThrowable {
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
