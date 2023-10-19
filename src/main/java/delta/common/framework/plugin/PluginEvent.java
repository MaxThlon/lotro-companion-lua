package delta.common.framework.plugin;

import javax.annotation.Nullable;

/**
 * @author MaxThlon
 */
public class PluginEvent
{
  public String _name;
  public @Nullable Object[] _args;
  
  public PluginEvent(String name, Object[] args) {
    _name=name;
    _args=args;
  }
}
