package delta.games.lotro.lua.turbine.plugin;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.function.RegisteredFunction;

/**
 * @author MaxThlon
 */
public class PluginManager
{
  public static void add(LuaState state, LuaTable turbine) {

    LuaTable pluginManager = RegisteredFunction.bind(new RegisteredFunction[]{
        RegisteredFunction.of("GetAvailablePlugins", PluginManager::getAvailablePlugins),
        RegisteredFunction.of("GetLoadedPlugins", PluginManager::getLoadedPlugins),
        RegisteredFunction.of("LoadPlugin", PluginManager::loadPlugin),
        RegisteredFunction.of("RefreshAvailablePlugins", PluginManager::refreshAvailablePlugins),
        RegisteredFunction.of("ShowOptions", PluginManager::showOptions),
        RegisteredFunction.of("UnloadScriptState", PluginManager::unloadScriptState)
    });
    turbine.rawset("PluginManager", pluginManager);
  }
  
  public static LuaValue getAvailablePlugins(LuaState state, LuaValue self) {

    return Constants.NIL;
  }
  
  public static LuaValue getLoadedPlugins(LuaState state, LuaValue self) {

    return Constants.NIL;
  }
  
  public static LuaValue loadPlugin(LuaState state, LuaValue self) {

    return Constants.NIL;
  }
  
  public static LuaValue refreshAvailablePlugins(LuaState state, LuaValue self) {

    return Constants.NIL;
  }
  
  public static LuaValue showOptions(LuaState state, LuaValue self) {

    return Constants.NIL;
  }
  
  public static LuaValue unloadScriptState(LuaState state, LuaValue self) {

    return Constants.NIL;
  }
}
