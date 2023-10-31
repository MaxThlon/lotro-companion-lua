package delta.games.lotro.lua.turbine.plugin;

import delta.games.lotro.client.plugin.Plugin;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;

/**
 * @author MaxThlon
 */
public class LuaPlugin {

  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = PluginData.add(lua);
  	if (error != Lua.LuaError.OK) return error;

  	error = LuaObject.callInherit(lua, -3, "Turbine", "Object");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", LuaPlugin::constructor);
    LuaTools.setFunction(lua, -1, -3, "GetName", LuaPlugin::getName);
    LuaTools.setFunction(lua, -1, -3, "GetVersion", LuaPlugin::getVersion);
    LuaTools.setFunction(lua, -1, -3, "GetAuthor", LuaPlugin::getAuthor);
    LuaTools.setFunction(lua, -1, -3, "GetConfiguration", LuaPlugin::getConfiguration);
    LuaTools.setFunction(lua, -1, -3, "Load", LuaPlugin::load);
    LuaTools.setFunction(lua, -1, -3, "Unload", LuaPlugin::unload);
    LuaTools.setFunction(lua, -1, -3, "GetOptionsPanel", LuaPlugin::getOptionsPanel);

    lua.setField(-2, "Plugin");
    
    error = PluginManager.add(lua);
    return error;
  }
  
  public static Plugin pluginSelf(Lua lua, int index) {
    return LuaObject.objectSelf(lua, index, Plugin.class);
  }

  public static int newLuaPlugin(Lua lua, int envIndex, Plugin plugin) {
  	LuaTools.pushValue(lua, envIndex, "Turbine", "Object");
    lua.push(plugin, Conversion.NONE);
    lua.pCall(1,1);
    
    return 1;
  }
  
  public static int constructor(Lua lua) {
    /*Turbine.ObjectInheritedConstructor(
        lua,
        self,
        LuaTools.optUserdata(plugin, Plugin.class, null),
        null,
        null
    );*/
    return 1;
  }
  
  private static int getName(Lua lua) {
    return 1;
  }
  
  private static int getVersion(Lua lua) {
    return 1;
  }

  private static int getAuthor(Lua lua) {
    return 1;
  }
  
  private static int getConfiguration(Lua lua) {
    return 1;
  }
  
  private static int load(Lua lua) {
    return 1;
  }
  
  private static int unload(Lua lua) {
    return 1;
  }
  
  private static int getOptionsPanel(Lua lua) {
    return 1;
  }
}
