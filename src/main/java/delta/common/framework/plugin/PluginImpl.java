package delta.common.framework.plugin;

import com.eleet.dragonconsole.CommandProcessor;

/**
 * @author MaxThlon
 */
public interface PluginImpl
{
  /**
   * @param event .
   * @return PluginResult .
   */
  PluginResult handleEvent(PluginEvent event);
  void close();
  /**
   * @return CommandProcessor.
   */
  CommandProcessor getCommandProcessor();
}
