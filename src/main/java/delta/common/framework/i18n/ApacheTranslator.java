package delta.common.framework.i18n;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import delta.common.utils.i18n.Translator;

/**
 * @author MaxThlon
 */
public class ApacheTranslator extends Translator
{
  private PropertiesConfiguration _propertiesConfiguration;
  
  /**
   * @param baseName of the <tt>ResourceBundle</tt> to use.
   * @param parent translator.
   * @param locale Locale.
   */
  public ApacheTranslator(String baseName, Translator parent, Locale locale)
  {
    super(baseName, parent, locale);
    _propertiesConfiguration = new PropertiesConfiguration();
  }

  /**
   * Get the application locale url.
   * @param LabelsClazz .
   * @param locale .
   * @return a locale file url.
   */
  private URL getApplicationLocaleUrl(Class<?> LabelsClazz, Locale locale) {
    ResourceBundle.Control control=ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES);

    Locale usedLocale;
    if (locale.getLanguage() == "en") {
      usedLocale=Locale.ROOT;
    } else {
      usedLocale=locale;
    }

    return LabelsClazz.getClassLoader().getResource(
        control.toResourceName(
            control.toBundleName(LabelsClazz.getName(), usedLocale),
            "properties"
        )
    );
  }

  /**
   * @param key .
   * @return a string comment.
   */
  public String getComment(String key) {
    return _propertiesConfiguration.getLayout().getComment(key);
  }
  
  /**
   * @param key .
   * @param comment .
   */
  public void setComment(String key, String comment) {
    _propertiesConfiguration.getLayout().setComment(key, comment);
  }

  @Override
  public String unsafeTranslate(String key) {
    return _propertiesConfiguration.getString(key);
  }
  
  /**
   * @param key .
   * @param value .
   */
  public void setTranslation(String key, String value) {
    _propertiesConfiguration.setProperty(key, value);
  }
  
  @Override
  public Set<String> getKeys() {
    return _propertiesConfiguration.getLayout().getKeys();
  }
  
  /**
   * load translator.
   * @param LabelsClazz .
   */
  public void load(Class<?> LabelsClazz) {
    try
    {
      _propertiesConfiguration.getLayout().load(
          _propertiesConfiguration,
          new InputStreamReader(getApplicationLocaleUrl(LabelsClazz, _locale).openStream())
      );
    }
    catch (ConfigurationException|IOException e)
    {
      LOGGER.error(e);
    }
  }
}
