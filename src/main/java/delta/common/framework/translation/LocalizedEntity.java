package delta.common.framework.translation;

import delta.common.utils.i18n.MultilocalesTranslator;

/**
 * @author MaxThlon
 */
public abstract class LocalizedEntity
{
  public String _name;

  /**
   * @param name .
   */
  public LocalizedEntity(String name)
  {
    _name = name;
  }
  
  public String getName() {
    return _name;
  }
  
  abstract public MultilocalesTranslator getMultilocalesTranslator();
}
