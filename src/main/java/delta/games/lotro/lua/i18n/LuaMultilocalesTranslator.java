package delta.games.lotro.lua.i18n;

import java.util.List;
import java.util.Locale;

import delta.common.framework.i18n.ApacheMultilocalesTranslator;
import delta.common.utils.i18n.MultilocalesTranslator;
import delta.common.utils.i18n.Translator;
import delta.games.lotro.lua.turbine.TurbineLanguage;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;
import party.iroiro.luajava.lua51.Lua51Consts;
import party.iroiro.luajava.value.LuaValue;

/**
 * @author MaxThlon
 */
public class LuaMultilocalesTranslator extends ApacheMultilocalesTranslator
{
  /**
   * Initialize lua LuaMultilocalesTranslator package
   * 
   * @param lua            .
   * @param envIndex       .
   */
  public static void openPackage(Lua lua, int envIndex) {
    LuaTools.pushClass(lua, "Turbine", "Object");
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetTranslator", LuaMultilocalesTranslator::getTranslator);
    lua.setGlobal("MultilocalesTranslator");
  }

  /**
   * Constructor.
   * @param baseName of the <tt>ResourceBundle</tt> to use.
   * @param locales to use.
   */
  public LuaMultilocalesTranslator(String baseName, List<Locale> locales)
  {
    super(baseName, locales);
  }
  
  @Override
  protected Translator newTranslator(String baseName, Translator parent, Locale locale) {
    return new LuaTranslator(baseName, null, locale);
  }
  
  /**
   * Retrieve java LuaMultilocalesTranslator instance from a LuaValue.
   * 
   * @param lua   .
   * @param index .
   * @return a LuaMultilocalesTranslator.
   */
  public static LuaMultilocalesTranslator multilocalesTranslatorSelf(Lua lua, int index) {
    return LuaObject.objectSelf(lua, index, LuaMultilocalesTranslator.class);
  }
  
  /**0
   * Push a already existing or new lua MultilocalesTranslator instance.
   * 
   * @param lua      .
   * @param envIndex .
   * @param multilocalesTranslator   .
   */
  public static void pushLuaMultilocalesTranslator(Lua lua, int envIndex, MultilocalesTranslator multilocalesTranslator) {
    LuaValue luaMultilocalesTranslator = LuaObject.findLuaObjectFromObject(multilocalesTranslator);

    if (luaMultilocalesTranslator != null) {
      lua.push(luaMultilocalesTranslator, Conversion.NONE);
    } else {
      LuaTools.newClassInstance(lua, envIndex, (relativeEnvIndex) -> {
        LuaTools.pushValue(lua, relativeEnvIndex.intValue(), "MultilocalesTranslator");
      });
      LuaObject.ObjectInheritedConstructor(lua, -1, multilocalesTranslator, null, null);
      //lua.pushValue(-1);
      //luaMultilocalesTranslator.getContext().setValue(LuaControl.jComponentKey_luaObjectSelf, lua.get());
    }
  }

  private static int getTranslator(Lua lua) {
    LuaMultilocalesTranslator multilocalesTranslator = multilocalesTranslatorSelf(lua, 1);
    Translator translator = multilocalesTranslator.getTranslator(TurbineLanguage.ToLocale(TurbineLanguage.valueOfNumber(Integer.valueOf((int)lua.toNumber(2)))));
    
    LuaTranslator.pushLuaTranslator(lua, Lua51Consts.LUA_ENVIRONINDEX, translator);
    return 1;
  }
}
