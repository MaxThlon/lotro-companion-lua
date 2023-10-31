package delta.games.lotro.lua.turbine.plugin;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class PluginManager
{
  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = LuaObject.callInherit(lua, -3, "Turbine", "Object");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", PluginManager::constructor);
    LuaTools.setFunction(lua, -1, -3, "GetAvailablePlugins", PluginManager::getAvailablePlugins);
    LuaTools.setFunction(lua, -1, -3, "GetLoadedPlugins", PluginManager::getLoadedPlugins);
    LuaTools.setFunction(lua, -1, -3, "LoadPlugin", PluginManager::loadPlugin);
    LuaTools.setFunction(lua, -1, -3, "RefreshAvailablePlugins", PluginManager::refreshAvailablePlugins);
    LuaTools.setFunction(lua, -1, -3, "ShowOptions", PluginManager::showOptions);
    LuaTools.setFunction(lua, -1, -3, "UnloadScriptState", PluginManager::unloadScriptState);

    lua.setField(-2, "PluginManager");
    return error;
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }
  
  private static int getAvailablePlugins(Lua lua) {

    return 1;
  }
  
  private static int getLoadedPlugins(Lua lua) {

    return 1;
  }
  
  private static int loadPlugin(Lua lua) {

    return 1;
  }
  
  private static int refreshAvailablePlugins(Lua lua) {

    return 1;
  }
  
  private static int showOptions(Lua lua) {

    return 1;
  }
  
  private static int unloadScriptState(Lua lua) {

    return 1;
  }
}
