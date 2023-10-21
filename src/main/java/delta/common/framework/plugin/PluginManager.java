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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.eleet.dragonconsole.CommandProcessor;
import com.eleet.dragonconsole.DragonConsole;

import delta.common.framework.jobs.Job;
import delta.common.framework.jobs.JobPool;
import delta.common.framework.jobs.JobResult;
import delta.common.framework.jobs.MultiThreadedJobExecutor;
import delta.common.ui.swing.JFrame;
import delta.common.ui.swing.GuiFactory;
import delta.common.utils.application.config.main.MainApplicationConfiguration;
import delta.games.lotro.client.plugin.Plugin;
import delta.games.lotro.client.plugin.io.xml.PluginXMLParser;
import delta.games.lotro.lua.LuaRunner;

/**
 * PluginManager.
 * @author MaxThlon
 */
public class PluginManager
{
  private static class PluginManagerHolder {
    private static final PluginManager PLUGIN_MANAGER = new PluginManager();
  }
  
  interface PluginListener {
    public void producedResult(JobResult<?> jobResult);
  }

  private static final Logger LOGGER=Logger.getLogger(PluginManager.class);

  private PluginConfiguration _pluginConfiguration;
  private List<Plugin> _plugins;

  MultiThreadedJobExecutor _jobExecutor;
  PluginExecutor _pluginExecutor;
  Job _job;



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
    _pluginConfiguration=((PluginConfigurationHolder)MainApplicationConfiguration.getInstance()).getPluginConfiguration();
    _plugins=loadPluginList();
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
        String apartmentName=path.getParent().getFileName().toString();
        return new PluginXMLParser().parsePluginData(path.toFile(), apartmentName);
      })
          .collect(Collectors.toList());
    }
    catch (IOException e)
    {
      LOGGER.error(e);
      return List.of();
    }
  }
  
  protected File pluginFindScriptFile(Plugin plugin) {
    return _pluginConfiguration.getPluginsPath().resolve(plugin._package.replaceAll("\\.", "/")+".lua").toFile();
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
  
  public PluginImpl createLuaRunner() {
    JFrame frame=GuiFactory.buildFrame();
    
    DragonConsole dragonConsole = new DragonConsole(false, false);
    frame.add(dragonConsole);
    frame.pack();
    frame.setVisible(true);
    frame.toFront();

    CommandProcessor commandProcessor=new CommandProcessor();
    commandProcessor.install(dragonConsole);
    LuaRunner luaRunner=new LuaRunner(commandProcessor);
    luaRunner.initPackageLib(_pluginConfiguration.getPluginsPath());
    luaRunner.bootstrapSandBoxedLotro();
    return luaRunner;
  }

  protected void initJobExecutor() {
    if (_jobExecutor == null) {
      _jobExecutor=new MultiThreadedJobExecutor(new JobPool(), 1);
    }
  }

  /**
   * start Lua.
   */
  public void startLua() {
    initJobExecutor();
    _pluginExecutor=new PluginExecutor(this);
    _job=_jobExecutor.getPool().addJob(_pluginExecutor);
    _jobExecutor.start();
  }

  /**
   * execute plugin.
   * @param plugin .
   */
  public void execute(Plugin plugin) {
    _pluginExecutor.queueEvent("loadPlugin", new Object[]{ plugin });
  }
  
  /**
   * @param eventName .
   * @param arguments .
   */
  public void event(String eventName, Object[] arguments) {
    _pluginExecutor.queueEvent(eventName, arguments);
  }
}
