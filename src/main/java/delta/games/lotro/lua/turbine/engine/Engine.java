package delta.games.lotro.lua.turbine.engine;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * Engine library for lua scripts.
 * @author MaxThlon
 */
public class Engine {
  public static Lua.LuaError openPackage(Lua lua) {
  	Lua.LuaError error;
  	if ((error = LuaObject.callInherit(lua, -3, "Turbine", "Object")) != Lua.LuaError.OK) return error;
    LuaTools.setFunction(lua, -1, -3, "Constructor", Engine::constructor);
    LuaTools.setFunction(lua, -1, -3, "GetCallStack", Engine::getCallStack);
    LuaTools.setFunction(lua, -1, -3, "GetDate", Engine::getDate);
    LuaTools.setFunction(lua, -1, -3, "GetGameTime", Engine::getGameTime);
    LuaTools.setFunction(lua, -1, -3, "GetLanguage", Engine::getLanguage);
    LuaTools.setFunction(lua, -1, -3, "GetLocale", Engine::getLocale);
    LuaTools.setFunction(lua, -1, -3, "GetLocalTime", Engine::getLocalTime);
    LuaTools.setFunction(lua, -1, -3, "GetScriptVersion", Engine::getScriptVersion);
    LuaTools.setFunction(lua, -1, -3, "ScriptLog", Engine::scriptLog);
    lua.pCall(0, 1);
    lua.setField(-2, "Engine");
    return error;
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }
  
  private static int getCallStack(Lua lua) {
    return 1;
  }
  
  private static int getDate(Lua lua) {
    return 1;
  }
  
  private static int getGameTime(Lua lua) {
    return 1;
  }
  
  private static int getLanguage(Lua lua) {
    return 1;
  }
  
  private static int getLocale(Lua lua) {
    return 1;
  }
  
  private static int getLocalTime(Lua lua) {
    return 1;
  }
  
  private static int getScriptVersion(Lua lua) {
    return 1;
  }
  
  private static int scriptLog(Lua lua) {
    return 1;
  }
}

