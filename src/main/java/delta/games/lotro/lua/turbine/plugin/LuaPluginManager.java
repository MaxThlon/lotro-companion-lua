package delta.games.lotro.lua.turbine.plugin;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class LuaPluginManager
{
  /**
   * Initialize lua PluginManager package
   * @param lua .
   * @param envIndex .
   * @param errfunc .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError add(Lua lua, int envIndex, int errfunc) {
  	Lua.LuaError error;
  	error = LuaTools.pushClass(lua, errfunc, "Turbine", "Object");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", LuaPluginManager::constructor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetAvailablePlugins", LuaPluginManager::getAvailablePlugins);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetLoadedPlugins", LuaPluginManager::getLoadedPlugins);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "LoadPlugin", LuaPluginManager::loadPlugin);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "RefreshAvailablePlugins", LuaPluginManager::refreshAvailablePlugins);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "ShowOptions", LuaPluginManager::showOptions);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "UnloadScriptState", LuaPluginManager::unloadScriptState);

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
