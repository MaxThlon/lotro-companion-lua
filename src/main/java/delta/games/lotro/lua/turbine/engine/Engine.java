package delta.games.lotro.lua.turbine.engine;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * Engine library for lua scripts.
 * @author MaxThlon
 */
public class Engine {
  /**
   * Initialize lua Engine package
   * @param lua .
   * @param envIndex .
   * @param errfunc .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError openPackage(Lua lua, int envIndex, int errfunc) {
  	Lua.LuaError error;
  	if ((error = LuaTools.pushClass(lua, errfunc, "Turbine", "Object")) != Lua.LuaError.OK) return error;
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", Engine::constructor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetCallStack", Engine::getCallStack);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetDate", Engine::getDate);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetGameTime", Engine::getGameTime);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetLanguage", Engine::getLanguage);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetLocale", Engine::getLocale);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetLocalTime", Engine::getLocalTime);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetScriptVersion", Engine::getScriptVersion);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "ScriptLog", Engine::scriptLog);
    LuaTools.pCall(lua, 0, 1, LuaTools.relativizeIndex(errfunc, -1));
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

