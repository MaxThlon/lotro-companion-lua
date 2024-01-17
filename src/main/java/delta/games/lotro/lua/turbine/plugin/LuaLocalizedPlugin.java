package delta.games.lotro.lua.turbine.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.SwingUtilities;

import delta.common.framework.i18n.event.LocalizedEntityUpdatedEvent;
import delta.common.framework.plugin.LocalizedPlugin;
import delta.common.framework.plugin.PluginManager;
import delta.common.utils.i18n.MultilocalesTranslator;
import delta.games.lotro.lua.i18n.LuaMultilocalesTranslator;
import delta.games.lotro.lua.turbine.TurbineLanguage;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import delta.games.lotro.utils.events.EventsManager;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;
import party.iroiro.luajava.lua51.Lua51Consts;
import party.iroiro.luajava.value.LuaValue;

/**
 * @author MaxThlon
 */
public class LuaLocalizedPlugin {
  /**
   * Initialize lua Plugin package
   * 
   * @param lua            .
   * @param envIndex       .
   * @param namespaceIndex .
   */
  public static void openPackage(Lua lua, int envIndex, int namespaceIndex) {
    LuaTools.pushClass(lua, "Turbine", "Object");
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetPluginsPath", LuaLocalizedPlugin::getPluginsPath);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "InitMultilocalesTranslator", LuaLocalizedPlugin::initMultilocalesTranslator);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "InvokeUpdatedEvent", LuaLocalizedPlugin::invokeUpdatedEvent);
    lua.setField(LuaTools.relativizeIndex(namespaceIndex, -1), "LocalizedPlugin");
  }

  /**
   * Retrieve java localizedPlugin instance from a LuaValue.
   * 
   * @param lua   .
   * @param index .
   * @return a plugin.
   */
  public static LocalizedPlugin localizedPluginSelf(Lua lua, int index) {
    return LuaObject.objectSelf(lua, index, LocalizedPlugin.class);
  }

  /**
   * Push a already existing or new lua localizedPlugin instance.
   * 
   * @param lua      .
   * @param envIndex .
   * @param localizedPlugin   .
   */
  public static void pushLuaLocalizedPlugin(Lua lua, int envIndex, LocalizedPlugin localizedPlugin) {
    LuaValue luaLocalizedPlugin = LuaObject.findLuaObjectFromObject(localizedPlugin);

    if (luaLocalizedPlugin != null) {
      lua.push(luaLocalizedPlugin, Conversion.NONE);
    } else {
      LuaTools.newClassInstance(lua, envIndex, (relativeEnvIndex) -> {
        LuaTools.pushValue(lua, relativeEnvIndex.intValue(), "Turbine", "LocalizedPlugin");
      });
      LuaObject.ObjectInheritedConstructor(lua, -1, localizedPlugin, null, null);
      //lua.pushValue(-1);
      //plugin.getContext().setValue(LuaControl.jComponentKey_luaObjectSelf, lua.get());
    }
  }

  private static int getPluginsPath(Lua lua) {
    lua.push(
        PluginManager.getInstance().getPluginConfiguration().getPluginsPath().toString() + "\\"
    );
    return 1;
  }
  
  private static int initMultilocalesTranslator(Lua lua) {
    LocalizedPlugin localizedPlugin = localizedPluginSelf(lua, 1);

    List<Locale> locales = new ArrayList<Locale>();
    for (int i=2; i != lua.getTop()+1; i++) {
      locales.add(TurbineLanguage.ToLocale(TurbineLanguage.valueOfNumber(Integer.valueOf((int)lua.toNumber(i)))));
    }
    MultilocalesTranslator multilocalesTranslator = new LuaMultilocalesTranslator(localizedPlugin.getName(), locales);
    localizedPlugin.setMultilocalesTranslator(multilocalesTranslator);
    LuaMultilocalesTranslator.pushLuaMultilocalesTranslator(lua, Lua51Consts.LUA_ENVIRONINDEX, multilocalesTranslator);
    return 1;
  }
  
  private static int invokeUpdatedEvent(Lua lua) {    
    LocalizedPlugin localizedPlugin = localizedPluginSelf(lua, 1);
    SwingUtilities.invokeLater(() ->
      EventsManager.invokeEvent(new LocalizedEntityUpdatedEvent(localizedPlugin))
    );
    return 0;
  }
}
