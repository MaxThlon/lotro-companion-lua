package delta.games.lotro.lua.turbine.engine;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.function.RegisteredFunction;

/**
 * Engine library for lua scripts.
 * @author MaxThlon
 */
public class Engine {
  public static void add(LuaState state, LuaTable turbineEnv) {
    LuaTable engine=RegisteredFunction.bind(new RegisteredFunction[]{
        RegisteredFunction.of("GetCallStack", Engine::getCallStack),
        RegisteredFunction.of("GetDate", Engine::getDate),
        RegisteredFunction.of("GetGameTime", Engine::getGameTime),
        RegisteredFunction.of("GetLanguage", Engine::getLanguage),
        RegisteredFunction.of("GetLocale", Engine::getLocale),
        RegisteredFunction.of("GetLocalTime", Engine::getLocalTime),
        RegisteredFunction.of("GetScriptVersion", Engine::getScriptVersion),
        RegisteredFunction.of("ScriptLog", Engine::scriptLog)
    });
    turbineEnv.rawset("Engine", engine);
  }
  
  public static LuaValue getCallStack(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getDate(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getGameTime(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getLanguage(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getLocale(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getLocalTime(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getScriptVersion(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue scriptLog(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
}

