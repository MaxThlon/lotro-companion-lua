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

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.log4j.Logger;

import delta.common.framework.lua.LuaModule;
import delta.common.framework.lua.command.LuaModuleCommand;
import delta.common.framework.lua.event.LuaModuleEvent;
import delta.common.framework.module.command.ModuleExecutorCommand;
import delta.common.framework.plugin.PluginConfiguration;
import delta.common.framework.plugin.PluginManager;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.client.plugin.Plugin;
import delta.games.lotro.lua.command.LotroLMCNewThread;
import delta.games.lotro.lua.turbine.Apartment;
import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.turbine.plugin.DataScope;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.ExternalLoader;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;
import party.iroiro.luajava.Lua.LuaError;
import party.iroiro.luajava.value.LuaValue;

/**
 * @author MaxThlon
 */
public class LotroLuaModule extends LuaModule {
	private static String PLUGIN_CONFIG_APARTMENT_TAG = "Apartment";
	private static String DEFAULT_APARTMENT_NAME = "Default";

	private static Logger LOGGER = Logger.getLogger(LuaTools.class);

	private CharacterFile _characterFile;
	private Map<String, Apartment> _apartments;
	private Map<String, ImmutablePair<LuaValue, LuaValue>> _shellCommands;
	
	/**
	 * @param uuid .
	 * @param loader .
	 * @param characterFile .
	 * @param paths .
	 */
	public LotroLuaModule(UUID uuid,
												@Nullable ExternalLoader loader,
												CharacterFile characterFile,
												Path... paths) {
		super(uuid, loader, paths);
		
		_characterFile = characterFile;
  	_apartments = new HashMap<String, Apartment>();
  	_shellCommands = new HashMap<String, ImmutablePair<LuaValue, LuaValue>>();
  }
	
  private Lua.LuaError bootstrapLotro() {
    Lua.LuaError error;

    if ((error = LuaTools.openRootPackage(_lua)) != Lua.LuaError.OK) return error;
    if ((error = Turbine.openRootPackage(_lua)) != Lua.LuaError.OK) return error;
    
    return error;
  }
	
	/**
	 * Find an apartment name from plugin or return {@link #DEFAULT_APARTMENT_NAME}
	 * @param plugin .
	 * @return apartment name.
	 */
	public String findApartmentName(Plugin plugin) {
		String apartmentName = DEFAULT_APARTMENT_NAME;
		Map<String, String> configuration = plugin.getConfiguration();
		if (configuration != null) {
			apartmentName = plugin.getConfiguration().get(PLUGIN_CONFIG_APARTMENT_TAG);
		}
		return apartmentName;
	}
	
	/**
	 * @param apartmentName
	 * @return an Apartment.
	 */
	public @Nullable Apartment getApartment(String apartmentName) {
		return _apartments.get(apartmentName);
	}
	
	/**
	 * @param name
	 * @return an Apartment.
	 */
	public Apartment addApartment(String name) {
		Apartment apartment = new Apartment(LuaTools.libraryNameSplit(name, false));
		_apartments.put(name, apartment);

		return apartment;
	}
	
	@Override
	protected ImmutablePair<Lua, LuaError> buildThread(LuaModuleCommand command) {
		LotroLMCNewThread lotroLMCLoadPlugin = (LotroLMCNewThread)command;
		
		ImmutablePair<Lua, LuaError> threadAndError = super.buildThread(command);
		if (threadAndError.right != Lua.LuaError.OK) return threadAndError;
		Lua thread = threadAndError.left;
		LuaError error = threadAndError.right;
		threadAndError = null;

		String apartmentName = findApartmentName(lotroLMCLoadPlugin.getPlugin());
		Apartment apartment = getApartment(apartmentName);
		if (apartment == null) {
			apartment = addApartment(apartmentName);
		}

		/* work in main thread space */
		Turbine.pushGlobals(_lua, apartment.getNamespace()); /* push env for thread */
		_lua.pushValue(-1);
		LuaTools.setfenv(_lua, -3); /* set thread env */

		/* work in thread space */
		thread.getGlobal(LuaTools.PCALL_ERR_FUNC_NAME);
		_lua.xMove(thread, 1); /* xMove env to thread */
		if ((error = Turbine.openPackage(thread, -1, -2)) != Lua.LuaError.OK)
			return new ImmutablePair<Lua, LuaError>(thread, error);
		if ((error = thread.load(
				LuaTools.loadBuffer(Paths.get("Turbine", "EventThread.lua"), LotroLuaModule.class),
				"Turbine.EventThread"
    )) != Lua.LuaError.OK) return new ImmutablePair<Lua, LuaError>(thread, error);
		thread.replace(-3); /* PCALL_ERR_FUNC <- chunk */
		LuaTools.setfenv(thread, -2); /* set chunk env */
		thread.pushValue(-1);
		error = thread.resume(0);
		
		return new ImmutablePair<Lua, LuaError>(thread, error);
	}
	
	@Override
  public void load(ModuleExecutorCommand command) {
  	LuaTools.handleLuaResult(_lua, bootstrapLotro());
  }

	@Override
	public Lua.LuaError executeEvent(Lua thread, LuaModuleEvent event) {
		Lua.LuaError error = Lua.LuaError.OK;

    /*Lua thread = ((LuaValue)event._args[0]).state();
    if (thread.status() != Lua.LuaError.YIELD) {
			LuaTools.handleLuaResult(thread, thread.status());
			return thread.status();
		}*/
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
	 * @return a set of known commands.
	 */
	public Set<String> getCommands() {
		return _shellCommands.keySet();
	}
	
	/**
	 * @param name .
	 * @param luaExecute .
	 * @param luaCommand .
	 */
	public void addCommand(String name, LuaValue luaExecute, LuaValue luaCommand) {
		_shellCommands.put(name, new ImmutablePair<LuaValue, LuaValue>(luaExecute, luaCommand));
	}
	
	/**
	 * @param name .
	 */
	public void removeCommand(String name) {
		_shellCommands.remove(name);
	}
	
	/**
	 * @param name .
	 * @return return a pair of {@link LuaValue} the Execute command instance method and the command instance.
	 */
	public ImmutablePair<LuaValue, LuaValue> findCommand(String name) {
		ImmutablePair<LuaValue, LuaValue> pair = _shellCommands.get(name);
		return (pair == null)?null:pair;
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
		
		error = _lua.load(LuaTools.loadBuffer(pluginDataPath.resolve(Paths.get(key))), key);
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
			writer.write("return");
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
}
