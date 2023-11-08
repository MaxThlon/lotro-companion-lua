package delta.common.framework.lua;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.ImmutablePair;

import delta.common.framework.console.ConsoleCommandEvent;
import delta.common.framework.lua.command.LuaMTCCommand;
import delta.common.framework.lua.command.LuaMTCEvent;
import delta.common.framework.lua.command.LuaModuleCommand;
import delta.common.framework.lua.command.LuaModuleThreadCommand;
import delta.common.framework.lua.event.LuaModuleEvent;
import delta.common.framework.module.Module;
import delta.common.framework.module.ModuleExecutor;
import delta.common.framework.module.ModuleManager;
import delta.common.framework.module.command.ModuleCommand;
import delta.common.framework.module.command.ModuleExecutorCommand;
import delta.games.lotro.lua.LotroLuaModule;
import delta.games.lotro.lua.utils.LuaTools;
import delta.games.lotro.lua.utils.URLToolsLua;
import delta.games.lotro.utils.events.GenericEventsListener;
import party.iroiro.luajava.ExternalLoader;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.LuaType;
import party.iroiro.luajava.lua51.Lua51;
import party.iroiro.luajava.lua51.Lua51Consts;
import party.iroiro.luajava.value.LuaValue;

/**
 * LuaModule for lua scripts.
 * 
 * @author MaxThlon
 */
public abstract class LuaModule implements Module, GenericEventsListener<ConsoleCommandEvent> {
	/**
	 * Enum for LuaModule commands types.
	 * @author MaxThlon
	 */
	public enum Command {
    NEW_THREAD,
    COMMAND,
    EVENT,
  }

	protected final static List<Lua.LuaError> NO_ERROR = Arrays.asList(Lua.LuaError.OK, Lua.LuaError.YIELD);

  //private static Logger LOGGER = Logger.getLogger(LuaModule.class);
	protected boolean _isClosed = false;
	protected UUID _uuid;
  protected Lua _lua;
  private Map<UUID, Lua> _threads;
  
  /**
   * @param uuid .
   * @param loader .
   * @param paths .
   */
  public LuaModule(UUID uuid, @Nullable ExternalLoader loader, Path... paths) {
  	_uuid = uuid;
    _lua = new Lua51();
    // Allow externals natives libraries for posix systems
    /*if (_lua.getLuaNative() instanceof Lua51Natives) {
    	((Lua51Natives)_lua.getLuaNative()).loadAsGlobal();
    }*/
    
    if (loader != null) {
      _lua.setExternalLoader(loader);
    }
    _lua.openLibraries();
    LuaTools.setJavaModuleUuid(_lua, _uuid);
    LuaTools.setJavaLuaModule(_lua, this);
    initPackageLib(paths);
  }
  
  @Override
  public UUID getUuid() {
		return _uuid;
	}

	@Override
	public String getName() {
		return "LuaModule";
	}

  /**
   * @return the main lua thread.
   */
  public Lua getLua() {
  	return _lua;
  }

  @Override
  public boolean canAccept(ModuleExecutorCommand command) {
  	return command.getModuleUuid() == _uuid;
  }

  private void initPackageLib(Path... paths) {
    LuaValue packageLib = _lua.get("package");
    
    if (packageLib.type() == LuaType.TABLE) {
    	String FILE_SEP = System.getProperty("file.separator");

    	List<String> packagePaths = new ArrayList<String>();
    	packagePaths.addAll(Arrays.stream(paths).map(path -> path.toString()).collect(Collectors.toList()));
    	packagePaths.add(URLToolsLua.getFromClassPath(""));
    	//packagePaths.add(URLToolsLua.getFromClassPath(Paths.get("lib", "lua", "5.1").toString()));
    	//packagePaths.add(URLToolsLua.getFromClassPath(Paths.get("share", "lua", "5.1").toString()));
    	packagePaths.add(URLToolsLua.getFromClassPath(Paths.get("luarocks", "lua").toString()));
    	packagePaths.add(URLToolsLua.getFromClassPath("thirdpart"));
      
    	/* Set path */
    	packageLib.set("path", _lua.from(
    			((String)packageLib.get("path").toJavaObject()) + ";" +
      		packagePaths.stream().flatMap(path -> Stream.of(
      				path + FILE_SEP + "?.lua",
      				path + FILE_SEP + "?" + FILE_SEP + "__init__.lua"
      		)).collect(Collectors.joining (";"))
      ));
      
    	/* Set cpath */
    	packageLib.set("cpath", _lua.from(
      		((String)packageLib.get("cpath").toJavaObject()) + ";" +
      		packagePaths.stream().map(path -> path + FILE_SEP + "?.dll")
      							  .collect(Collectors.joining (";"))
      ));

      packageLib.set("config", _lua.from(FILE_SEP + "\n;\n?\n!\n-\n"));
    }
  }

  /*@Override
  public void load(ModuleEvent event) {
  }*/

  protected ImmutablePair<Lua, Lua.LuaError> buildThread(LuaModuleCommand command) {
  	UUID threadUuid;
  	if (command instanceof LuaModuleThreadCommand) {
  		threadUuid = ((LuaModuleThreadCommand)command).getThreadUuid();
  	} else {
  		threadUuid = UUID.randomUUID();
  	}
  	if (_threads == null) {
  		_threads = new HashMap<UUID, Lua>();
  	}
  	Lua thread = _lua.newThread();
  	_threads.put(threadUuid, thread);
  	LuaTools.setJavaThreadUuid(thread, threadUuid);
  	return new ImmutablePair<Lua, Lua.LuaError>(thread, Lua.LuaError.OK);
  }

  private Lua findThread(LuaModuleThreadCommand command, Lua currentThread) {
  	if (command.getThreadUuid() != _uuid) { /* If not main thread */
  		Lua newthread = (_threads != null)?_threads.get(command.getThreadUuid()):null;
			if (newthread == null) {
				currentThread.error("lua thread not found with uuid: " + command.getThreadUuid().toString());
			} else if (newthread != currentThread) {
				currentThread = newthread;
			}
		}
  	return currentThread;
  }

  @Override
  public void execute(ModuleCommand command) {
  	if (_isClosed) throw new IllegalStateException("LuaModule has been closed");
  	if (command instanceof LuaModuleCommand) {
  		LuaModuleCommand lCommand = (LuaModuleCommand)command;
    	Lua.LuaError error = null;
    	Lua thread = _lua;
    			
    	switch (lCommand.getType()) {
    		case NEW_THREAD:
    			ImmutablePair<Lua, Lua.LuaError> threadAndError = buildThread(lCommand);
    			thread = threadAndError.left;
    			error = threadAndError.right;
    			threadAndError = null;
    			if (!NO_ERROR.contains(error)) break;
    			if (thread != _lua) {
    				_lua.pop(1); /* pop thread */
    				thread = _lua;
    			}
    			break;
    		case COMMAND:
    			if (lCommand instanceof LuaMTCCommand) {
    				LuaMTCCommand lcCommand = (LuaMTCCommand)lCommand;
    				
    				thread = findThread(lcCommand, _lua);
    				thread.pushThread();
    				thread.pushValue(Lua51Consts.LUA_GLOBALSINDEX);
    				thread.replace(-2); /* thread <- env */
    				thread.getGlobal(LuaTools.PCALL_ERR_FUNC_NAME);
    				thread.getField(-2, lcCommand.getCommand()); /* find command from env */
    				int nArgs = 0;
    				String[] args = lcCommand.getArgs();
    				if (args != null) {
    					nArgs = args.length;
    					for (String arg:args) {
      					thread.push(arg);
      				}
    				}    				
    				error = LuaTools.pCall(thread, nArgs, 1, -(2 + nArgs));
    				if (!NO_ERROR.contains(error)) break;
        		thread.pop(3); /* pop env, PCALL_ERR_FUNC, result */
    			}
    			break;
    		case EVENT: {
    			if (lCommand instanceof LuaMTCEvent) {
    				LuaMTCEvent leCommand = (LuaMTCEvent)lCommand;
    				
    				thread = findThread(leCommand, _lua);
    				error = executeEvent(thread, leCommand.getEvent());
    				if (!NO_ERROR.contains(error)) break;
    			}
    			break;
    		}
    	}
      LuaTools.handleLuaResult(thread, error);
  	}
  }

  /**
   * Execute an event.
   * @param thread .
   * @param event .
   * @param errfunc .
   * @return Lua.LuaError.
   */
  public abstract Lua.LuaError executeEvent(Lua thread, LuaModuleEvent event);

  @Override
  public void unLoad() {
  	if (!_isClosed) {
      _isClosed = true;
      _lua.close();
      _lua = null;
  	}
  }

  private LuaMTCEvent convert(Lua thread, ConsoleCommandEvent event, ImmutablePair<LuaValue, LuaValue> luaCommand) {
  	return new LuaMTCEvent(
  			LuaTools.getJavaThreadUuid(thread),
      	new LuaModuleEvent(
    				luaCommand.left,
    				Stream.of(luaCommand.right, thread.from(event.getCommand()), thread.from(event.getArgs()))
    							.toArray(length -> new LuaValue[length])
    		)
    );
  }

	@Override
	public void eventOccurred(ConsoleCommandEvent event) {
		ImmutablePair<LuaValue, LuaValue> luaCommand = ((LotroLuaModule)this).findCommand(event.getCommand());
  	if (luaCommand != null) {
  		Lua thread = luaCommand.left.state();
    	
  		ModuleManager.getInstance().offer(new ModuleExecutorCommand(
    			ModuleExecutor.Command.EXECUTE,
    			LuaTools.getJavaModuleUuid(thread),
      		convert(thread, event, luaCommand)
      ));
  	}
	}
}
