package delta.games.lotro.lua;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.apache.log4j.Logger;

import delta.common.framework.console.event.ConsoleCommandEvent;
import delta.common.framework.lua.LuaModule;
import delta.common.framework.lua.LuaThreadState;
import delta.common.framework.lua.command.LuaMTCEvent;
import delta.common.framework.lua.command.LuaModuleCommandCaracterFile;
import delta.common.framework.lua.command.LuaModuleThreadCommand;
import delta.common.framework.lua.event.LuaModuleEvent;
import delta.common.framework.module.ModuleExecutor;
import delta.common.framework.module.ModuleManager;
import delta.common.framework.module.command.ModuleCommand;
import delta.common.framework.module.command.ModuleExecutorCommand;
import delta.common.framework.plugin.PluginConfiguration;
import delta.common.framework.plugin.PluginManager;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.lua.command.LotroLMCNewScriptState;
import delta.games.lotro.lua.command.LotroLMCUnloadScriptState;
import delta.games.lotro.lua.command.LuaMTCLoadLocalizedPlugin;
import delta.games.lotro.lua.command.LuaMTCLoadPlugin;
import delta.games.lotro.lua.command.LuaMTCRequireScriptFile;
import delta.games.lotro.lua.i18n.LuaMultilocalesTranslator;
import delta.games.lotro.lua.i18n.LuaTranslator;
import delta.games.lotro.lua.turbine.LotroLuaScriptState;
import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.turbine.TurbineLanguage;
import delta.games.lotro.lua.turbine.plugin.DataScope;
import delta.games.lotro.lua.turbine.plugin.LuaLocalizedPlugin;
import delta.games.lotro.lua.turbine.plugin.LuaPlugin;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;
import party.iroiro.luajava.lua51.Lua51Consts;
import party.iroiro.luajava.value.LuaValue;

/**
 * @author MaxThlon
 */
public class LotroLuaModule extends LuaModule {
	private static Logger LOGGER = Logger.getLogger(LuaTools.class);

	private CharacterFile _characterFile;
	private Map<String, UUID> _scriptStateToThreadUuid;
	private Map<String, LuaValue> _shellCommands;
	
	/**
	 * @param uuid .
	 */
	public LotroLuaModule(UUID uuid) {
		super(uuid);
  }

	/**
	 * @return a CharacterFile.
	 */
	public CharacterFile getCharacterFile() {
		return _characterFile;
	}

	/**
	 * @return a set of known commands.
	 */
	public @Nullable Set<String> getCommands() {
		return (_shellCommands != null)?_shellCommands.keySet() : null;
	}

	/**
	 * @param scriptStateName .
	 * @return an thread uuid.
	 */
	public @Nullable UUID findThreadUuidFromScriptStateName(String scriptStateName) {
		return (_scriptStateToThreadUuid != null)?_scriptStateToThreadUuid.get(scriptStateName):null;
	}

	@Override
  protected LuaThreadState buildThreadState(LuaModuleThreadCommand lThreadCommand, UUID threadUuid, Lua thread) {
	  if (lThreadCommand instanceof LotroLMCNewScriptState) {
    	LotroLMCNewScriptState lotroLMCNewThread = (LotroLMCNewScriptState)lThreadCommand;
    	String scriptStateName = lotroLMCNewThread.getScriptStateName();
    	return new LotroLuaScriptState(threadUuid, thread, LuaTools.libraryNameSplit(scriptStateName, false));
	  }
    return super.buildThreadState(lThreadCommand, threadUuid, thread);
  }

	@Override
	protected LuaThreadState buildThread(LuaModuleThreadCommand lThreadCommand) {
	  LuaThreadState luaThreadState = super.buildThread(lThreadCommand);
	  
	  if (lThreadCommand instanceof LotroLMCNewScriptState) {
  		LotroLMCNewScriptState lotroLMCNewThread = (LotroLMCNewScriptState)lThreadCommand;
  		LotroLuaScriptState lotroLuaScriptState = (LotroLuaScriptState)luaThreadState;

  		if (lotroLuaScriptState != null) {
  			if (_scriptStateToThreadUuid == null) {
  				_scriptStateToThreadUuid = new HashMap<String, UUID>();
  			}
    		_scriptStateToThreadUuid.put(lotroLMCNewThread.getScriptStateName(), lotroLuaScriptState.getTheadUuid());
    		Lua thread = lotroLuaScriptState.getThead();
   
    		/* work in main thread space */
    		Turbine.pushGlobals(_lua, lotroLuaScriptState.getNamespace()); /* push env for thread */
    		_lua.pushValue(-1);
    		LuaTools.setfenv(_lua, -3); /* set thread env */
    
    		/* work in thread space */
    		_lua.xMove(thread, 1); /* xMove env to thread */
    		TurbineLanguage.openPackage(thread, -1);
    		Turbine.openPackage(thread, -1);
    		/*if ((error = thread.load(
    				LuaTools.loadBuffer(Paths.get("Turbine", "EventThread.lua"), LotroLuaModule.class),
    				"Turbine.EventThread"
        )) != Lua.LuaError.OK)  {
    			lotroLuaScriptState.setError(error);
    			return lotroLuaScriptState;
    		}
    		thread.replace(-3); / PCALL_ERR_FUNC <- chunk /
    		LuaTools.setfenv(thread, -2); / set chunk env /
    		thread.pushValue(-1);
    		error = thread.resume(0);*/
    		thread.pop(1); /* env */
  		}
	  }
		return luaThreadState;
	}

	@Override
	protected void interruptThread(LuaModuleThreadCommand lThreadCommand) {
		if (_scriptStateToThreadUuid != null) {
			if (lThreadCommand instanceof LotroLMCUnloadScriptState) {
				LotroLMCUnloadScriptState LotroLMCUnloadScriptState = (LotroLMCUnloadScriptState)lThreadCommand;
				_scriptStateToThreadUuid.remove(LotroLMCUnloadScriptState.getScriptStateName());
			}
		}
		super.interruptThread(lThreadCommand);
	}
	
  private Lua.LuaError bootstrapLotro() {
    Lua.LuaError error;

    if ((error = LuaTools.openRootPackage(_lua)) != Lua.LuaError.OK) return error;
    if ((error = Turbine.openRootPackage(_lua)) != Lua.LuaError.OK) return error;
    
    return error;
  }

	@Override
  public void load(ModuleCommand[] commands) {
		super.load(commands);
		_shellCommands = new HashMap<String, LuaValue>();
  	LuaTools.handleLuaResult(_lua, bootstrapLotro());

  	if (commands != null) {
      for (ModuleCommand command:commands) {
      	if (command instanceof LuaModuleCommandCaracterFile) {
      		LuaModuleCommandCaracterFile lCommandCaracterFile = (LuaModuleCommandCaracterFile)command;
      		_characterFile = lCommandCaracterFile.getCharacterFile();
      	}
      }
    }
  }
	
	/**
	 * @param name .
	 * @param luaCommand .
	 */
	public void addCommand(String name, LuaValue luaCommand) {
		if (_shellCommands != null) {
			_shellCommands.put(name, luaCommand);
		}
	}
	
	/**
	 * @param name .
	 */
	public void removeCommand(String name) {
		if (_shellCommands != null) {
			_shellCommands.remove(name);
		}
	}
	
	/**
	 * @param name .
	 * @return return a pair of {@link LuaValue} the Execute command instance method and the command instance.
	 */
	public LuaValue findCommand(String name) {
		return (_shellCommands != null)?_shellCommands.get(name):null;
	}

	/**
	 * @param dataScope .
	 * @param key .
	 * @return the path for the corresponding dataScope and key.
	 */
	public Path resolveDataScopePath(DataScope dataScope, String key) {
		Path path = null;
		switch (dataScope) {
			case ACCOUNT:
				path = Paths.get(
						_characterFile.getAccountName(),
						PluginConfiguration.PLUGIN_DATA_PATH_SERVER_ALL,
						key + PluginConfiguration.PLUGIN_DATA_FILE_EXT
				);
				break;
			case SERVER:
				path = Paths.get(
						_characterFile.getAccountName(),
						_characterFile.getServerName(),
						PluginConfiguration.PLUGIN_DATA_PATH_CHARACTER_ALL,
						key + PluginConfiguration.PLUGIN_DATA_FILE_EXT
				);
				break;
			case CHARACTER:
				path = Paths.get(
						_characterFile.getAccountName(),
						_characterFile.getServerName(),
						_characterFile.getName(),
						key + PluginConfiguration.PLUGIN_DATA_FILE_EXT
				);
				break;
		}
		return PluginManager.getInstance().getPluginConfiguration().getPluginDataPath().resolve(path);
	}

	/**
	 * Load the plugin data from corresponding dataScope and key.
	 * @param lua .
	 * @param dataScope
	 * @param key
	 * @return a Lua.LuaError.
	 */
	public Lua.LuaError loadPluginData(Lua lua, DataScope dataScope, String key) {
		Lua.LuaError error;
		Path pluginDataPath = resolveDataScopePath(dataScope, key);
		File pluginDataFile = pluginDataPath.toFile();
		if (pluginDataFile.exists()) {
			error = _lua.load(LuaTools.loadBuffer(pluginDataPath), key);
		} else  {
			lua.pushNil();
			error = Lua.LuaError.OK;
		}
		return error;
	}

	/**
	 * Save the plugin data from corresponding dataScope and key.
	 * @param lua .
	 * @param dataScope .
	 * @param key .
	 * @param dataIndex stack index of the data.
	 * @return a Lua.LuaError.
	 */
	public Lua.LuaError savePluginData(Lua lua, DataScope dataScope, String key, int dataIndex) {
		Lua.LuaError error;

		Path pluginDataPath = resolveDataScopePath(dataScope, key);
		BufferedWriter writer = null;
		try {
			File file = pluginDataPath.toFile();
			writer = new BufferedWriter(new FileWriter(file));
			writer.write("return ");
			LuaTools.serialize(lua, writer, dataIndex);
	    error = Lua.LuaError.OK;
		} catch (IOException e) {
			//LuaTools.invokeErrorEvent(lua, Lua.LuaError.FILE);
			LOGGER.error(e);
			error = Lua.LuaError.FILE;
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					LOGGER.error(e);
					error = Lua.LuaError.FILE;
				}
			}
		}
		return error;
	}
	
	@Override
	protected Lua.LuaError executeCommand(Lua thread, LuaModuleThreadCommand ltCommand) {
		Lua.LuaError error = null;
		if (ltCommand instanceof LuaMTCRequireScriptFile) {
		  LuaMTCRequireScriptFile lRequireScriptFile = (LuaMTCRequireScriptFile)ltCommand;

		  thread.push(lRequireScriptFile.getScriptFile());
		  thread.pushValue(Lua51Consts.LUA_GLOBALSINDEX);
      thread.getGlobal(LuaTools.PCALL_ERR_FUNC_NAME);
      Turbine.require(thread, false, true);
      thread.pop(4); /* pop package name, globals, ERR_FUNC, package */
		} else if (ltCommand instanceof LuaMTCLoadLocalizedPlugin) {
      LuaMTCLoadLocalizedPlugin lLoadLocalizedPlugin = (LuaMTCLoadLocalizedPlugin)ltCommand;

      thread.pushValue(Lua51Consts.LUA_GLOBALSINDEX);
      TurbineLanguage.openPackage(thread, -1);
      LuaTranslator.openPackage(thread, -1);
      LuaMultilocalesTranslator.openPackage(thread, -1);
      Turbine.pushNamespace(thread, -1, "Turbine"); /* push namespace */
      LuaPlugin.openPackage(thread, -2, -1);
      LuaLocalizedPlugin.openPackage(thread, -2, -1);
      
      thread.pop(1); /* pop namespace */
      LuaPlugin.pushLuaPlugin(thread, -1, lLoadLocalizedPlugin.getLocalizedPlugin().getPlugin());
      thread.setField(-2, "plugin");

      LuaLocalizedPlugin.pushLuaLocalizedPlugin(thread, -1, lLoadLocalizedPlugin.getLocalizedPlugin());
      thread.setField(-2, "localizedPlugin");      
      
      thread.pop(1); /* pop globals */
      thread.push(lLoadLocalizedPlugin.getLocalizedPlugin().getPlugin().getPackage());
      thread.pushValue(Lua51Consts.LUA_GLOBALSINDEX);
      thread.getGlobal(LuaTools.PCALL_ERR_FUNC_NAME);
      Turbine.require(thread, true, true);
      thread.pop(4); /* pop package name, globals, ERR_FUNC, package */
    } else if (ltCommand instanceof LuaMTCLoadPlugin) {
			LuaMTCLoadPlugin lLoadPluginCommand = (LuaMTCLoadPlugin)ltCommand;

			thread.pushValue(Lua51Consts.LUA_GLOBALSINDEX);
			LuaPlugin.pushLuaPlugin(thread, -1, lLoadPluginCommand.getPlugin());
			thread.setField(-2, "plugin");
			thread.pop(1); /* pop globals */
			thread.push(lLoadPluginCommand.getPlugin().getPackage());
			thread.pushValue(Lua51Consts.LUA_GLOBALSINDEX);
			thread.getGlobal(LuaTools.PCALL_ERR_FUNC_NAME);
			Turbine.require(thread, true, true);
			thread.pop(4); /* pop package name, globals, ERR_FUNC, package */

			if ((error = thread.load(
  				LuaTools.loadBuffer(Paths.get("Turbine", "EventThread.lua"), LotroLuaModule.class),
  				"Turbine.EventThread"
      )) != Lua.LuaError.OK)  return error;
			thread.pushValue(Lua51Consts.LUA_GLOBALSINDEX);
  		LuaTools.setfenv(thread, -2); /* set chunk env */
  		thread.pushValue(-1);
  		error = thread.resume(0);
		}
		return error;
	}

	@Override
	public Lua.LuaError executeEvent(Lua thread, LuaModuleEvent event) {
		Lua.LuaError error = Lua.LuaError.OK;

		thread.pushValue(-1);
		int nArgs = 1;
    thread.push(event.getEventHandlers(), Conversion.NONE);
    LuaValue[] args = event.getArgs();
    if (args != null) {
    	nArgs += args.length;
      for (LuaValue arg:args) {
      	thread.push(arg, Conversion.NONE); /* push arg */
      }
    }
    if ((error = thread.resume(nArgs)) != Lua.LuaError.YIELD) return error;
    return error;
  }

	/**
	 * Convert a java event to lua event, calling Execute method of lua Event class.
	 * @param thread
	 * @param event
	 * @param luaCommand
	 * @return a LuaMTCEvent.
	 */
  private LuaMTCEvent convert(Lua thread, ConsoleCommandEvent event, LuaValue luaCommand) {
  	return new LuaMTCEvent(
  			LuaTools.getJavaThreadUuid(thread),
      	new LuaModuleEvent(
      			luaCommand.get("Execute"),
    				Stream.of(luaCommand, thread.from(event.getCommand()), thread.from(event.getArgs()))
    							.toArray(length -> new LuaValue[length])
    		)
    );
  }

	@Override
	public void eventOccurred(ConsoleCommandEvent event) {
		LuaValue luaCommand = findCommand(event.getCommand());
  	if (luaCommand != null) {
  		Lua thread = luaCommand.state();
  		
  		ModuleManager.getInstance().offer(new ModuleExecutorCommand(
    			ModuleExecutor.Command.EXECUTE,
    			LuaTools.getJavaModuleUuid(thread),
      		convert(thread, event, luaCommand)
      ));
  	}
	}
	
  @Override
  public void unLoad() {
  	if (!_isClosed) {
  		_shellCommands = null;
  		_scriptStateToThreadUuid = null;
  		_characterFile = null;
      super.unLoad();
  	}
  }
}
