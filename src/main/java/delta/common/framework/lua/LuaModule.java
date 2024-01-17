package delta.common.framework.lua;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import delta.common.framework.console.event.ConsoleCommandEvent;
import delta.common.framework.lua.command.LuaMTCCommand;
import delta.common.framework.lua.command.LuaMTCCommandMethod;
import delta.common.framework.lua.command.LuaMTCEvent;
import delta.common.framework.lua.command.LuaModuleCommand;
import delta.common.framework.lua.command.LuaModuleCommandPath;
import delta.common.framework.lua.command.LuaModuleThreadCommand;
import delta.common.framework.lua.event.LuaModuleEvent;
import delta.common.framework.module.Module;
import delta.common.framework.module.command.ModuleCommand;
import delta.common.framework.module.command.ModuleExecutorCommand;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import delta.games.lotro.lua.utils.URLToolsLua;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;
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
    /**
     * Module new thread.
     */
    NEW_THREAD,
    /**
     * Module interrupt thread.
     */
    INTERRUPT_THREAD,
    /**
     * Module command.
     */
    COMMAND,
    /**
     * Module lua event
     */
    EVENT,
  }

	protected final static List<Lua.LuaError> NO_ERROR = Arrays.asList(Lua.LuaError.OK, Lua.LuaError.YIELD);

  //private static Logger LOGGER = Logger.getLogger(LuaModule.class);
	protected boolean _isClosed = false;
	protected UUID _uuid;
  protected Lua _lua;
  private Map<UUID, LuaThreadState> _threadStates;
  
  /**
   * @param uuid .
   */
  public LuaModule(UUID uuid) {
  	_uuid = uuid;
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
  
  /**
   * @return return thread states.
   */
  public Collection<LuaThreadState> getThreadStates() {
  	return _threadStates.values();
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

  /**
   * @param threadUuid .
   * @return return thread state.
   */
  public LuaThreadState findThreadState(UUID threadUuid) {
  	return (_threadStates != null)?_threadStates.get(threadUuid):null;
  }

  protected LuaThreadState buildThreadState(LuaModuleThreadCommand lThreadCommand, UUID threadUuid, Lua thread) {
  	return new LuaThreadState(threadUuid, thread);
  }

  protected LuaThreadState buildThread(LuaModuleThreadCommand lThreadCommand) {
  	UUID threadUuid = lThreadCommand.getThreadUuid();
  	if (_threadStates == null) {
  		_threadStates = new HashMap<UUID, LuaThreadState>();
  	}
  	Lua thread = _lua.newThread();
  	LuaThreadState luaThreadState = buildThreadState(lThreadCommand, threadUuid, thread);
  	_threadStates.put(threadUuid, luaThreadState);
  	LuaTools.setJavaThreadUuid(thread, threadUuid);
  	return luaThreadState;
  }

  protected void interruptThread(LuaModuleThreadCommand lThreadCommand) {
		UUID threadUuid = lThreadCommand.getThreadUuid();
		//LuaThreadState luaThreadState = _threadStates.get(threadUuid);
		/* TODO Interupt */
		_threadStates.remove(threadUuid);
		//luaThreadState = null;
  }

  private @Nullable LuaThreadState findThreadState(LuaModuleThreadCommand lThreadCommand) {
  	return (_threadStates != null)?_threadStates.get(lThreadCommand.getThreadUuid()):null;
  }

  @Override
  public void load(ModuleCommand[] commands) {
  	//ExternalLoader loader;

  	_lua = new Lua51();
    // Allow externals natives libraries for posix systems
    /*if (_lua.getLuaNative() instanceof Lua51Natives) {
    	((Lua51Natives)_lua.getLuaNative()).loadAsGlobal();
    }*/
    
    /*if (loader != null) {
      _lua.setExternalLoader(loader);
    }*/
    _lua.openLibraries();
    LuaTools.setJavaModuleUuid(_lua, _uuid);
    LuaTools.setJavaLuaModule(_lua, this);
    EventsManager.addListener(ConsoleCommandEvent.class, this);
    
    if (commands != null) {
      for (ModuleCommand command:commands) {
      	if (command instanceof LuaModuleCommandPath) {
      		LuaModuleCommandPath lCommandPath = (LuaModuleCommandPath)command;
      		initPackageLib(lCommandPath.getPaths());
      	}
      }
    }
  }

  @Override
  public void execute(ModuleCommand command) {
  	if (_isClosed) throw new IllegalStateException("LuaModule has been closed");
  	if (command instanceof LuaModuleCommand) {
  		LuaModuleCommand lCommand = (LuaModuleCommand)command;
  		Lua.LuaError error = null;
    	Lua thread = _lua;
    			
    	switch (lCommand.getType()) {
    		case NEW_THREAD: {
    			if (command instanceof LuaModuleThreadCommand) {
      			LuaModuleThreadCommand lThreadCommand = (LuaModuleThreadCommand)command;
      			LuaThreadState luaThreadState = buildThread(lThreadCommand);
      			if (luaThreadState != null) {
      				error = luaThreadState.getError();
      				thread = luaThreadState.getThead();
      			}
      			if (thread != _lua) {
      				_lua.pop(1); /* pop thread */
      				thread = _lua;
      			}
    			}
    			break;
    		}
    		case INTERRUPT_THREAD: {
    			if (command instanceof LuaModuleThreadCommand) {
      			LuaModuleThreadCommand lThreadCommand = (LuaModuleThreadCommand)command;
      			interruptThread(lThreadCommand);
    			}
    			break;
    		}
    		case COMMAND: {
    			if (lCommand instanceof LuaMTCCommand) {
    				LuaMTCCommand lcCommand = (LuaMTCCommand)lCommand;
    				LuaMTCCommandMethod luaMTCCommandMethod = null;
    				LuaValue luaInstance = null;
    				if (lcCommand instanceof LuaMTCCommandMethod) {
    					luaMTCCommandMethod = (LuaMTCCommandMethod)lCommand;
    					luaInstance = LuaObject.findLuaObjectFromObject(luaMTCCommandMethod.getObject());
    				}
    				
    				LuaThreadState luaThreadState = findThreadState(lcCommand);
    				if (luaThreadState != null) {
      				thread = luaThreadState.getThead();
      				thread.getGlobal(LuaTools.PCALL_ERR_FUNC_NAME);
    
      				if (luaMTCCommandMethod != null) {
      					luaMTCCommandMethod = (LuaMTCCommandMethod)lCommand;
      					thread.push(luaInstance, Conversion.NONE);
      				} else {
      					thread.pushValue(Lua51Consts.LUA_ENVIRONINDEX);
      				}
      				thread.getField(-1, lcCommand.getCommand()); /* find command from instance or env */
      				thread.replace(-2); /* (luaInstance or env) <- method */
      				
      				/* If command is found */
      				if (!thread.isNil(-1)) {
        				int nArgs = 0;
        				if (luaMTCCommandMethod != null) {
        					thread.push(luaInstance, Conversion.NONE); /* push luaInstance to call luaInstance:method(args) */
        					++nArgs;
        				}
        				String[] args = lcCommand.getArgs();
        				if (args != null) {
        					nArgs += args.length;
        					for (String arg:args) {
          					thread.push(arg);
          				}
        				}    				
        				error = LuaTools.pCall(thread, nArgs, 1, -(2 + nArgs));
        				if (!NO_ERROR.contains(error)) break;
      				}
          		thread.pop(2); /* pop ERR_FUNC, result  or if command not found pop ERR_FUNC, nil */
    				}
    			} else {
    				if (lCommand instanceof LuaModuleThreadCommand) {
    					LuaModuleThreadCommand ltCommand = (LuaModuleThreadCommand)lCommand;
    					LuaThreadState luaThreadState = findThreadState(ltCommand);
    					error = executeCommand(luaThreadState.getThead(), ltCommand);
    					luaThreadState.setError(error);
    				}
    			}
    			break;
    		}
    		case EVENT: {
    			if (lCommand instanceof LuaMTCEvent) {
    				LuaMTCEvent leCommand = (LuaMTCEvent)lCommand;
    				LuaThreadState luaThreadState = findThreadState(leCommand);
    				if (luaThreadState != null) {
    					thread = luaThreadState.getThead();
      				error = executeEvent(thread, leCommand.getEvent());
      				if (!NO_ERROR.contains(error)) break;
    				}
    			}
    			break;
    		}
    	}
    	if (error != null) {
    		LuaTools.handleLuaResult(thread, error);
    	}
  	}
  }

  @Override
  public boolean canAccept(ModuleExecutorCommand command) {
  	return command.getModuleUuid() == _uuid;
  }

  protected abstract Lua.LuaError executeCommand(Lua thread, LuaModuleThreadCommand ltCommand);

  /**
   * Execute an event.
   * @param thread .
   * @param event .
   * @return Lua.LuaError.
   */
  public abstract Lua.LuaError executeEvent(Lua thread, LuaModuleEvent event);

  @Override
  public void unLoad() {
  	if (!_isClosed) {
      _isClosed = true;
      EventsManager.removeListener(ConsoleCommandEvent.class, this);
      _lua.close();
      _lua = null;
  	}
  }

  @Override
  public void dispose() {
  	unLoad();
  	_uuid = null;
  }
}
