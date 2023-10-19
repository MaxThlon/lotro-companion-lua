package delta.games.lotro.lua;

import java.util.List;
import java.util.Locale;

import delta.common.utils.i18n.MultilocalesTranslator;
import delta.common.utils.i18n.Translator;

/**
 * @author MaxThlon
 */
public class LuaMultilocalesTranslator extends MultilocalesTranslator
{
  /**
   * Constructor.
   * @param baseName of the <tt>ResourceBundle</tt> to use.
   * @param locales Locales to use.
   */
  public LuaMultilocalesTranslator(String baseName, List<Locale> locales)
  {
    super(baseName, locales);
  }
  
  @Override
  protected Translator newTranslator(String baseName, Translator parent, Locale locale) {
    return new LuaTranslator(baseName, null, locale);
  }
}
