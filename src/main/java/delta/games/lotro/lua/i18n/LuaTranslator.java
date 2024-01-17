package delta.games.lotro.lua.i18n;

import java.util.Locale;

import delta.common.framework.i18n.ApacheTranslator;
import delta.common.utils.i18n.Translator;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;
import party.iroiro.luajava.value.LuaValue;

/**
 * @author MaxThlon
 */
public class LuaTranslator extends ApacheTranslator
{
  /**
   * Initialize lua LuaMultilocalesTranslator package
   * 
   * @param lua            .
   * @param envIndex       .
   */
  public static void openPackage(Lua lua, int envIndex) {
    LuaTools.pushClass(lua, "Turbine", "Object");
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetTranslation", LuaTranslator::setTranslation);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetComment", LuaTranslator::setComment);
    lua.setGlobal("Translator");
  }

  /**
   * @param baseName of the <tt>ResourceBundle</tt> to use.
   * @param parent translator.
   * @param locale Locale.
   */
  public LuaTranslator(String baseName, Translator parent, Locale locale) {
    super(baseName,parent, locale);
  }
  
  /**
   * Retrieve java LuaMultilocalesTranslator instance from a LuaValue.
   * 
   * @param lua   .
   * @param index .
   * @return a LuaMultilocalesTranslator.
   */
  public static LuaTranslator translatorSelf(Lua lua, int index) {
    return LuaObject.objectSelf(lua, index, LuaTranslator.class);
  }

  /**
   * Push a already existing or new lua localizedPlugin instance.
   * 
   * @param lua .
   * @param envIndex .
   * @param translator .
   */
  public static void pushLuaTranslator(Lua lua, int envIndex, Translator translator) {
    LuaValue luaTranslator = LuaObject.findLuaObjectFromObject(translator);

    if (luaTranslator != null) {
      lua.push(luaTranslator, Conversion.NONE);
    } else {
      LuaTools.newClassInstance(lua, envIndex, (relativeEnvIndex) -> {
        LuaTools.pushValue(lua, relativeEnvIndex.intValue(), "Translator");
      });
      LuaObject.ObjectInheritedConstructor(lua, -1, translator, null, null);
      //lua.pushValue(-1);
      //luaTranslator.getContext().setValue(LuaControl.jComponentKey_luaObjectSelf, lua.get());
    }
  }

  static int setTranslation(Lua lua) {
    LuaTranslator translator = translatorSelf(lua, 1);
    String key = lua.toString(2);
    String value = lua.toString(3);
    translator.setTranslation(key, value);

    return 0;
  }
  
  static int setComment(Lua lua) {
    LuaTranslator translator = translatorSelf(lua, 1);
    String key = lua.toString(2);
    String value = lua.toString(3);
    translator.setComment(key, value);
    
    return 0;
  }
}
