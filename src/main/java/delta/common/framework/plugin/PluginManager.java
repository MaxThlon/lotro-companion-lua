package delta.common.framework.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

import delta.common.framework.module.ModuleEvent;
import delta.common.framework.module.ModuleExecutor;
import delta.common.framework.module.ModuleManager;
import delta.common.utils.application.config.main.MainApplicationConfiguration;
import delta.games.lotro.client.plugin.Plugin;
import delta.games.lotro.client.plugin.io.xml.PluginXMLParser;
import delta.games.lotro.lua.LuaLotro;
import delta.games.lotro.lua.LuaModule;
import delta.games.lotro.utils.events.EventsManager;

/**
 * PluginManager.
 * @author MaxThlon
 */
public class PluginManager 
{
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
    
  }
  
  public UUID getModuleUuid() {
  	return _pluginModuleUuid;
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
        .filter(plugin -> pluginName.equals(plugin._information._name))
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
  
  protected File pluginFindScriptFile(Plugin plugin) {
    return _pluginConfiguration.getPluginsPath()
    		.resolve(plugin._package.replace('.', File.separatorChar) + ".lua").toFile();
  }
  
	protected InputStream pluginOpenScript(File scriptFile) {
    FileInputStream inputStream;
    try
    {
      inputStream=new FileInputStream(scriptFile);
    }
    catch (FileNotFoundException e)
    {
      LOGGER.error(e);
      inputStream=null;
    }
    return inputStream;
  }

  public LuaModule addModule(UUID moduleUuid) {
  	LuaModule luaModule = new LuaModule(moduleUuid, null, new LuaLotro(), _pluginConfiguration.getPluginsPath());
  	ModuleManager.getInstance().addModule(luaModule);
  	
    return luaModule;
  }

  public void bootstrapLotro(Plugin plugin) {
  	addModule(_pluginModuleUuid);
  	EventsManager.invokeEvent(new  ModuleEvent(
    		ModuleExecutor.ExecutorEvent.LOAD,
    		_pluginModuleUuid,
    		ModuleExecutor.ExecutorEvent.LOAD.name(),
    		new Object[]{ LuaModule.LuaBootstrap.Lotro }
    ));
  	/*EventsManager.invokeEvent(new  ModuleEvent(
    		ModuleExecutor.ExecutorEvent.EXECUTE,
    		moduleUuid,
    		"debug",
    		null
    ));*/
  	EventsManager.invokeEvent(new  ModuleEvent(
    		ModuleExecutor.ExecutorEvent.EXECUTE,
    		_pluginModuleUuid,
    		"load",
    		new Object[]{ plugin }
    ));
  }
  
  public void bootstrapSandBoxedLotro() {
  	addModule(_pluginModuleUuid);

  	EventsManager.invokeEvent(new  ModuleEvent(
    		ModuleExecutor.ExecutorEvent.LOAD,
    		_pluginModuleUuid,
    		ModuleExecutor.ExecutorEvent.LOAD.name(),
    		new Object[]{ LuaModule.LuaBootstrap.LotroSandBox }
    ));
  }

  /**
   * execute plugin.
   * @param plugin .
   */
  public void loadPlugin(Plugin plugin) {
  	EventsManager.invokeEvent(new  ModuleEvent(
    		ModuleExecutor.ExecutorEvent.LOAD,
    		_pluginModuleUuid,
    		ModuleExecutor.ExecutorEvent.LOAD.name(),
    		new Object[]{ plugin }
    ));
  }
  
  /**
   * process command.
   * @param input .
   */
  public void processCommand(String input) {
  	EventsManager.invokeEvent(new ModuleEvent(
    		ModuleExecutor.ExecutorEvent.EXECUTE,
    		_pluginModuleUuid,
    		"processCommand",
    		new Object[]{ input }
    ));
  }
}
