package delta.common.framework.plugin;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import delta.common.framework.console.ConsoleManager;
import delta.common.framework.lua.LuaModule;
import delta.common.framework.lua.command.LuaMTCCommandMethod;
import delta.common.framework.lua.command.LuaModuleCommandCaracterFile;
import delta.common.framework.lua.command.LuaModuleCommandPath;
import delta.common.framework.lua.command.LuaModuleThreadCommand;
import delta.common.framework.module.Module;
import delta.common.framework.module.ModuleExecutor;
import delta.common.framework.module.ModuleManager;
import delta.common.framework.module.command.ModuleCommand;
import delta.common.framework.module.command.ModuleExecutorCommand;
import delta.common.utils.application.config.main.MainApplicationConfiguration;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.client.plugin.Plugin;
import delta.games.lotro.client.plugin.io.xml.PluginXMLParser;
import delta.games.lotro.lua.LotroLuaModule;
import delta.games.lotro.lua.command.LotroLMCNewScriptState;
import delta.games.lotro.lua.command.LotroLMCUnloadScriptState;
import delta.games.lotro.lua.command.LuaMTCLoadLocalizedPlugin;
import delta.games.lotro.lua.command.LuaMTCLoadPlugin;
import delta.games.lotro.lua.command.LuaMTCRequireScriptFile;
import delta.games.lotro.lua.i18n.LuaMultilocalesTranslator;
import delta.games.lotro.lua.turbine.LotroLuaScriptState;

/**
 * PluginManager.
 * @author MaxThlon
 */
public class PluginManager {
  private static class PluginManagerHolder {
    private static final PluginManager PLUGIN_MANAGER = new PluginManager();
  }
  
	private static String PLUGIN_CONFIG_APARTMENT_TAG = "Apartment";
	private static String DEFAULT_SCRIPTE_STATE_NAME = "Default";

  private static final Logger LOGGER=Logger.getLogger(PluginManager.class);
  private List<UUID> _activesPluginModules;
  private UUID _pluginModuleUuid;
  private PluginConfiguration _pluginConfiguration;
  private List<Plugin> _plugins, _localizedPlugins;

  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static PluginManager getInstance()
  {
    return PluginManagerHolder.PLUGIN_MANAGER;
  }
  
  /**
   * Constructor.
   */
  protected PluginManager()
  {
  	_activesPluginModules = new ArrayList<UUID>();
  	_pluginModuleUuid = UUID.randomUUID();
    _pluginConfiguration=((PluginConfigurationHolder)MainApplicationConfiguration.getInstance()).getPluginConfiguration();
    refreshPluginList();
    //EventsManager.addListener(ModuleEvent.class, this); 
  }
  
  /**
   * @return actives modules uuid list.
   */
  public List<UUID> getActivesPluginModules() {
  	return _activesPluginModules;
  }

  /**
   * @return module uuid.
   */
  public UUID getModuleUuid() {
  	return _pluginModuleUuid;
  }
  
  /**
   * @return PluginConfiguration.
   */
  public PluginConfiguration getPluginConfiguration() {
  	return _pluginConfiguration;
  }
  
  /**
   * Get the plugin list.
   * @return a plugin list.
   */
  public List<Plugin> getPlugins()
  {
    return _plugins;
  }

  /**
   * Get the localized plugin list.
   * @return a plugin list.
   */
  public List<Plugin> getLocalizedPlugins()
  {
    return _localizedPlugins;
  }

  /**
   * Get the plugin.
   * @param pluginName plugin name.
   * @return a plugin.
   */
  public Plugin getPlugin(String pluginName)
  {
    return _plugins.stream()
        .filter(plugin -> pluginName.equals(plugin.getInformation()._name))
        .findAny()
        .orElse(null);
  }
  
  /**
   * find path with matcher.
   * @param path 
   * @param matcher 
   * @return Stream<Path>.
   * @throws IOException 
   */
  protected Stream<Path> findParallel(Path path, PathMatcher matcher) throws IOException {
    return Files.walk(path, 2).filter(matcher::matches); //.parallel();
  }

  /**
   * Load plugin list.
   * @param extention .
   * @return plugin list
   */
  private List<Plugin> loadPluginListByExtention(String extention) {
    try {
      return findParallel(
          _pluginConfiguration.getPluginsPath(),
          FileSystems.getDefault().getPathMatcher("glob:**."+ extention)
      ).map(path-> {
        return new PluginXMLParser().parsePluginData(path.toFile());
      })
          .collect(Collectors.toList());
    }
    catch (IOException e)
    {
      LOGGER.error(e);
      return new ArrayList<Plugin>();
    }
  }

  /**
   * Load plugin list.
   * @return plugin list
   */
  private List<Plugin> loadPluginList() {
    return loadPluginListByExtention("plugin");
  }
  
  /**
   * Load localized plugin list.
   * @return plugin list
   */
  private List<Plugin> loadLocalizedPluginList() {
    return loadPluginListByExtention("localize");
  }

  /**
   * refresh plugin list.
   */
  public void refreshPluginList() {
  	_plugins=loadPluginList();
  }
  
  /**
   * refresh localized plugin list.
   */
  public void refreshLocalizedPluginList() {
    _localizedPlugins=loadLocalizedPluginList();
  }

  /**
   * bootstrapLotro with a CharacterFile.
   * @param characterFile
   */
  public void bootstrapLotro(CharacterFile characterFile) {
  	ModuleManager moduleManager = ModuleManager.getInstance();
  	if (!moduleManager.containModule(_pluginModuleUuid)) {
  		LuaModule luaModule = new LotroLuaModule(_pluginModuleUuid);
  		_activesPluginModules.add(_pluginModuleUuid);
  		moduleManager.addModule(luaModule);	
    	moduleManager.offer(new ModuleExecutorCommand(
	    		ModuleExecutor.Command.LOAD,
	    		_pluginModuleUuid,
	    		new ModuleCommand[] {
	    				new LuaModuleCommandPath(_pluginConfiguration.getPluginsPath()),
	    				new LuaModuleCommandCaracterFile(characterFile)
	    		}
	    ));
  	}
  }
  
  /**
   * bootstrap localization.
   */
  public void bootstrapLocalization() {
    ModuleManager moduleManager = ModuleManager.getInstance();
    if (!moduleManager.containModule(_pluginModuleUuid)) {
      LuaModule luaModule = new LotroLuaModule(_pluginModuleUuid);
      _activesPluginModules.add(_pluginModuleUuid);
      moduleManager.addModule(luaModule); 
      moduleManager.offer(new ModuleExecutorCommand(
          ModuleExecutor.Command.LOAD,
          _pluginModuleUuid,
          new ModuleCommand[] {
              new LuaModuleCommandPath(_pluginConfiguration.getPluginsPath())
          }
      ));
    }
  }
  
  /**
	 * Get an scriptState name from plugin or return {@link #DEFAULT_SCRIPTE_STATE_NAME}
	 * @param plugin .
	 * @return apartment name.
	 */
	public String getScriptStateName(Plugin plugin) {
		String scriptStateName = null;
		Map<String, String> configuration = plugin.getConfiguration();
		if (configuration != null) {
			scriptStateName = configuration.get(PLUGIN_CONFIG_APARTMENT_TAG);
		}

		return (scriptStateName != null)?scriptStateName:DEFAULT_SCRIPTE_STATE_NAME;
	}

  /**
   * @param scriptStateName .
   * @return a scriptState.
   */
  public LotroLuaScriptState findScriptState(String scriptStateName) {
  	Module module = ModuleManager.getInstance().findModule(_pluginModuleUuid);
  	if (module instanceof LotroLuaModule) {
  		LotroLuaModule lotroLuaModule = (LotroLuaModule)module;
  		UUID threadUuid = lotroLuaModule.findThreadUuidFromScriptStateName(scriptStateName);
  		if (threadUuid != null) {
  			return (LotroLuaScriptState)lotroLuaModule.findThreadState(threadUuid);
  		}
  	}
  	return null;
  }

  /**
   * create a ScriptState.
   * @param threadUuid .
   * @param scriptState .
   */
  public void createScriptState(UUID threadUuid, String scriptState) {
  	ModuleManager.getInstance().offer(new ModuleExecutorCommand(
    		ModuleExecutor.Command.EXECUTE,
    		_pluginModuleUuid,
    		new LotroLMCNewScriptState(threadUuid, scriptState) // new ScriptState
    ));
  }

  /**
   * unload a ScriptState.
   * @param scriptStateName .
   */
  public void unloadScriptState(String scriptStateName) {
  	Module module = ModuleManager.getInstance().findModule(_pluginModuleUuid);
  	if (module instanceof LotroLuaModule) {
  		LotroLuaModule lotroLuaModule = (LotroLuaModule)module;
  		UUID threadUuid = lotroLuaModule.findThreadUuidFromScriptStateName(scriptStateName);
  		ModuleManager.getInstance().offer(new ModuleExecutorCommand(
      		ModuleExecutor.Command.EXECUTE,
      		_pluginModuleUuid,
      		new LotroLMCUnloadScriptState(threadUuid, scriptStateName)
      ));
  	}
  }
  
  /**
   * load a plugin.
   * @param plugin .
   */
  public void loadPlugin(Plugin plugin) {
  	String scriptStateName = getScriptStateName(plugin);
  	LotroLuaScriptState scriptState = findScriptState(scriptStateName);
  	UUID threadUuid;

  	if (scriptState == null) {
  		threadUuid = UUID.randomUUID();
  		createScriptState(threadUuid, scriptStateName);
  	} else {
  		threadUuid = scriptState.getTheadUuid();
  	}
  	ModuleManager.getInstance().offer(new ModuleExecutorCommand(
    		ModuleExecutor.Command.EXECUTE,
    		_pluginModuleUuid,
    		new LuaMTCLoadPlugin(threadUuid, plugin),
    		new LuaMTCCommandMethod(threadUuid, plugin, "Load")
    ));
  }
  
  /**
   * load a localizedPlugin.
   * @param localizedPlugin .
   */
  public void loadLocalizedPlugin(LocalizedPlugin localizedPlugin) {
    UUID threadUuid = UUID.randomUUID();

    ModuleManager.getInstance().offer(new ModuleExecutorCommand(
        ModuleExecutor.Command.EXECUTE,
        _pluginModuleUuid,
        new LuaModuleThreadCommand(LuaModule.Command.NEW_THREAD, threadUuid),
        new LuaMTCRequireScriptFile(threadUuid, "metalua.loader"),
        new LuaMTCLoadLocalizedPlugin(threadUuid, localizedPlugin)
    ));
  }

  /**
   * unload a plugin (not implemented for lotro).
   * @param plugin .
   */
  public void unloadPlugin(Plugin plugin) { /* not implemented */  }
  
  /**
   * @param localizedPlugin .
   */
  public void loadPluginMultilocalesTranslator(LocalizedPlugin localizedPlugin) {
    ConsoleManager.getInstance().activateByModule();

    bootstrapLocalization();
    loadLocalizedPlugin(localizedPlugin);
  }
}
