package delta.games.lotro.lua.utils;

import static org.squiddev.cobalt.ValueFactory.valueOf;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.ErrorFactory;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaUserdata;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.function.LibFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

/**
 * LuaTools library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaTools {
  
  /**
   * Add GetFromLuaPath function to lua Globals
   * @param state 
   * @param env 
   * @throws LuaError 
   */
  public static void add(LuaState state, LuaTable env) throws LuaError {
    LibFunction.setGlobalLibrary(state, env, "URLToolsLua", RegisteredFunction.bind(new RegisteredFunction[]{
        RegisteredFunction.of("GetFromLuaPath", LuaTools::luaGetFromLuaPath)
    }));
  }
  
  static public Object checkUserdata(LuaValue value) {
    return (value.type() == Constants.TUSERDATA)?((LuaUserdata)value).instance : null;
  }

  static public <T> T checkUserdata(LuaValue value, Class<T> c) throws LuaError {
    Object instance = checkUserdata(value);
    if (!c.isAssignableFrom(instance.getClass())) throw ErrorFactory.typeError(value, c.getName());
    return c.cast(instance);
  }
  
  static public <T> T optUserdata(LuaValue value, Class<T> c, T defval) throws LuaError {
    
    return value == Constants.NIL ? defval : checkUserdata(value, c);
  }
  
  public static String getFromLuaPath(String name)
  {
    return "/src/lua/" + name;
  }
  
  public static LuaValue luaGetFromLuaPath(LuaState state, LuaValue name) throws LuaError {
    return valueOf(getFromLuaPath(name.checkString()));
  }
}
