package delta.games.lotro.lua.turbine.plugin;

import delta.games.lotro.client.plugin.Plugin;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.turbine.ui.LuaControl;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;
import party.iroiro.luajava.value.LuaValue;

/**
 * @author MaxThlon
 */
public class LuaPlugin {
  /**
   * Initialize lua Plugin package
   * 
   * @param lua            .
   * @param envIndex       .
   * @param namespaceIndex .
   */
  public static void openPackage(Lua lua, int envIndex, int namespaceIndex) {
    lua.createTable(0, 0);
    for (DataScope dataScope : DataScope.values()) {
      lua.push(dataScope.getValue());
      lua.setField(-2, dataScope.getLabel());
    }
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsA", LuaObject::isA);
    lua.setField(LuaTools.relativizeIndex(namespaceIndex, -1), "DataScope");

    LuaPluginData.add(lua, envIndex);

    LuaTools.pushClass(lua, "Turbine", "Object");
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetName", LuaPlugin::getName);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetVersion", LuaPlugin::getVersion);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetAuthor", LuaPlugin::getAuthor);
    // LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetConfiguration", LuaPlugin::getConfiguration);
    lua.setField(LuaTools.relativizeIndex(namespaceIndex, -1), "Plugin");

    LuaPluginManager.add(lua, envIndex, namespaceIndex);
  }

  /**
   * Retrieve java plugin instance from a LuaValue.
   * 
   * @param lua   .
   * @param index .
   * @return a plugin.
   */
  public static Plugin pluginSelf(Lua lua, int index) {
    return LuaObject.objectSelf(lua, index, Plugin.class);
  }

  /**
   * Push a already existing or new lua plugin instance.
   * 
   * @param lua      .
   * @param envIndex .
   * @param plugin   .
   */
  public static void pushLuaPlugin(Lua lua, int envIndex, Plugin plugin) {
    LuaValue luaPlugin = LuaObject.findLuaObjectFromObject(plugin);

    if (luaPlugin != null) {
      lua.push(luaPlugin, Conversion.NONE);
    } else {
      LuaTools.newClassInstance(lua, envIndex, (relativeEnvIndex) -> {
        LuaTools.pushValue(lua, relativeEnvIndex.intValue(), "Turbine", "Plugin");
      });
      LuaObject.ObjectInheritedConstructor(lua, -1, plugin, null, null);
      lua.pushValue(-1);
      plugin.getContext().setValue(LuaControl.jComponentKey_luaObjectSelf, lua.get());
    }
  }

  private static int getName(Lua lua) {
    Plugin plugin = pluginSelf(lua, 1);
    lua.push(plugin.getInformation()._name);
    return 1;
  }

  private static int getVersion(Lua lua) {
    Plugin plugin = pluginSelf(lua, 1);
    lua.push(plugin.getInformation()._version);
    return 1;
  }

  private static int getAuthor(Lua lua) {
    Plugin plugin = pluginSelf(lua, 1);
    lua.push(plugin.getInformation()._author);
    return 1;
  }

  /*
   * private static int getConfiguration(Lua lua) { Plugin plugin =
   * pluginSelf(lua, 1); lua.push(plugin.getConfiguration(), Conversion.FULL);
   * return 1; }
   */
}
