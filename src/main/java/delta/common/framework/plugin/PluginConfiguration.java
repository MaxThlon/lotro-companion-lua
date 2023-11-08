package delta.common.framework.plugin;

import java.nio.file.Path;
import java.nio.file.Paths;

import delta.common.utils.application.config.path.ApplicationPathConfiguration;
import delta.common.utils.misc.Preferences;
import delta.common.utils.misc.TypedProperties;

/**
 * Configuration for user paths.
 * @author MaxThlon
 */
public class PluginConfiguration
{
  private static final String PLUGIN_CONFIGURATION="Plugin";
  private static final Path PLUGINS_PATH=Paths.get("plugins");
  protected static final Path PLUGIN_DATA_PATH=Paths.get("PluginData");
  public static final String PLUGIN_DATA_FILE_EXT = ".plugindata";
  public static final String PLUGIN_DATA_PATH_SERVER_ALL="AllServers";
  public static final String PLUGIN_DATA_PATH_CHARACTER_ALL="AllCharacters";
  
  protected ApplicationPathConfiguration _appPathConfig;

  /**
   * Constructor.
   * @param appPathConfig .
   */
  public PluginConfiguration(ApplicationPathConfiguration appPathConfig)
  {
    _appPathConfig=appPathConfig;
  }
  
  /**
   * Get the user data path for skins.
   * @return a directory path.
   */
  public Path getPluginsPath()
  {
    return _appPathConfig.getUserPath().resolve(PLUGINS_PATH);
  }
  
  /**
   * Get the user data path for skins.
   * @return a directory path.
   */
  public Path getPluginDataPath()
  {
    return _appPathConfig.getUserPath().resolve(PLUGIN_DATA_PATH);
  }

  /**
   * Initialize from preferences.
   * @param preferences Preferences to use.
   */
  public void fromPreferences(Preferences preferences)
  {
    TypedProperties props=preferences.getPreferences(PLUGIN_CONFIGURATION);
    FromProperties(props);
  }
  
  /**
   * Initialize from typed properties.
   * @param props
   */
  protected void FromProperties(TypedProperties props) {
  }

  /**
   * Save configuration.
   * @param preferences Preferences to use.
   */
  public void save(Preferences preferences)
  {
    TypedProperties props=preferences.getPreferences(PLUGIN_CONFIGURATION);
    save(props);
  }
  
  /**
   * Save typed properties.
   * @param props typed properties to use.
   */
  protected void save(TypedProperties props) {}
}
