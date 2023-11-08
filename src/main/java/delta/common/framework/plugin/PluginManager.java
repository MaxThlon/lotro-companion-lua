package delta.common.framework.plugin;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import delta.common.framework.console.ConsoleCommandEvent;
import delta.common.framework.lua.LuaModule;
import delta.common.framework.lua.command.LuaMTCCommand;
import delta.common.framework.module.ModuleExecutor;
import delta.common.framework.module.ModuleManager;
import delta.common.framework.module.command.ModuleExecutorCommand;
import delta.common.framework.module.event.ModuleEvent;
import delta.common.utils.application.config.main.MainApplicationConfiguration;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.client.plugin.Plugin;
import delta.games.lotro.client.plugin.io.xml.PluginXMLParser;
import delta.games.lotro.lua.LotroLuaModule;
import delta.games.lotro.lua.command.LotroLMCNewThread;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * PluginManager.
 * @author MaxThlon
 */
public class PluginManager  implements GenericEventsListener<ModuleEvent> {
  private static class PluginManagerHolder {
    private static final PluginManager PLUGIN_MANAGER = new PluginManager();
  }

  private static final Logger LOGGER=Logger.getLogger(PluginManager.class);

  private UUID _pluginModuleUuid;
  private PluginConfiguration _pluginConfiguration;
  private List<Plugin> _plugins;
  

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
  	_pluginModuleUuid = UUID.randomUUID();
    _pluginConfiguration=((PluginConfigurationHolder)MainApplicationConfiguration.getInstance()).getPluginConfiguration();
    _plugins=loadPluginList();
    EventsManager.addListener(ModuleEvent.class, this); 
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
   * @return plugin list
   */
  private List<Plugin> loadPluginList() {
    try {
      return findParallel(
          _pluginConfiguration.getPluginsPath(),
          FileSystems.getDefault().getPathMatcher("glob:**.plugin")
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

  private Plugin _plugin;
  public void bootstrapLotro(Plugin plugin, CharacterFile characterFile) {
  	_plugin = plugin;
  	LuaModule luaModule = new LotroLuaModule(
  			_pluginModuleUuid,
  			null,
  			characterFile,
  			_pluginConfiguration.getPluginsPath()
  	);
  	ModuleManager.getInstance().addModule(luaModule);
  	EventsManager.addListener(ConsoleCommandEvent.class, luaModule);
  }

	@Override
	public void eventOccurred(ModuleEvent event) {
		if ((event.getType() == ModuleExecutor.MEvent.STARTED) && 
				(event.getModule().getUuid() == _pluginModuleUuid)) {
			ModuleManager.getInstance().offer(new ModuleExecutorCommand(
	    		ModuleExecutor.Command.LOAD,
	    		_pluginModuleUuid
	    ));
			UUID threadUuid = UUID.randomUUID();
	  	ModuleManager.getInstance().offer(new ModuleExecutorCommand(
	    		ModuleExecutor.Command.EXECUTE,
	    		_pluginModuleUuid,
	    		new LotroLMCNewThread(threadUuid, _plugin),
	    		new LuaMTCCommand(threadUuid, "import", _plugin.getPackage())
	    ));
		}
	}
}
