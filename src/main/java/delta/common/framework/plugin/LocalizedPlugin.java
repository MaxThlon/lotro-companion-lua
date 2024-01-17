package delta.common.framework.plugin;

import delta.common.framework.translation.LocalizedEntity;
import delta.common.utils.i18n.MultilocalesTranslator;
import delta.games.lotro.client.plugin.Plugin;

/**
 * @author MaxThlon
 */
public class LocalizedPlugin extends LocalizedEntity
{
  Plugin _plugin;
  MultilocalesTranslator _multilocalesTranslator;
  /**
   * @param plugin .
   */
  public LocalizedPlugin(Plugin plugin)
  {
    super(plugin.getInformation()._name);
    _plugin=plugin;
    _multilocalesTranslator=null;
  }
  
  /**
   * @return a plugin.
   */
  public Plugin getPlugin() {
    return _plugin;
  }

  public MultilocalesTranslator getMultilocalesTranslator() {
    return _multilocalesTranslator;
  }
  
  /**
   * @param multilocalesTranslator .
   */
  public void setMultilocalesTranslator(MultilocalesTranslator multilocalesTranslator) {
    _multilocalesTranslator=multilocalesTranslator;
  }
}
