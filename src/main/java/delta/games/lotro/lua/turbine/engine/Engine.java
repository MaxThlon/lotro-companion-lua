package delta.games.lotro.lua.turbine.engine;

import delta.games.lotro.lua.turbine.object.LuaObject;
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
   */
  public static void openPackage(Lua lua, int envIndex) {
  	lua.createTable(0, 10);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsA", LuaObject::isA);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetCallStack", Engine::getCallStack);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetDate", Engine::getDate);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetGameTime", Engine::getGameTime);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetLanguage", Engine::getLanguage);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetLocale", Engine::getLocale);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetLocalTime", Engine::getLocalTime);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetScriptVersion", Engine::getScriptVersion);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "ScriptLog", Engine::scriptLog);
    lua.setField(-2, "Engine");
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

