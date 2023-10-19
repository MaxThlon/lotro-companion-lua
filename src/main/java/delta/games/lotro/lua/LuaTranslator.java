package delta.games.lotro.lua;

import java.util.Locale;
import java.util.Set;

import delta.common.utils.i18n.Translator;

/**
 * @author MaxThlon
 */
public class LuaTranslator extends Translator// implements TranslatorEx
{ 
  /**
   * @param baseName of the <tt>ResourceBundle</tt> to use.
   * @param parent translator.
   * @param locale Locale.
   */
  public LuaTranslator(String baseName, Translator parent, Locale locale) {
    super(baseName,parent, locale);
  }
  
  //@Override
  public String getComment(String key) {
    return null;
  }
  
  @Override
  public String unsafeTranslate(String key) {
    return null; //_propertiesConfiguration.getString(key);
  }
  
  /**
   * @param key
   * @param value
   */
  public void setTranslation(String key, String value) {
    //_propertiesConfiguration.setProperty(key, value);
  }
  
  @Override
  public Set<String> getKeys() {
    return null; //_propertiesConfiguration.getLayout().getKeys();
  }
}
