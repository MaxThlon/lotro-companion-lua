package delta.games.lotro.lua.utils;

import static org.squiddev.cobalt.ValueFactory.valueOf;

import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.LibFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.common.utils.url.URLTools;
import delta.games.lotro.lua.LuaJRunner;

/**
 * Tool methods related to URLs management of lua requires.
 * @author DAM
 */
public abstract class URLToolsLua
{

  public static void add(LuaState state, LuaTable env) throws LuaError {
    LibFunction.setGlobalLibrary(state, env, "URLToolsLua", RegisteredFunction.bind(new RegisteredFunction[]{
        RegisteredFunction.of("getFromClassPath", URLToolsLua::luaGetFromClassPath)
    }));
  }
  
  public static String getFromClassPath(String name)
  {
    return URLToolsLua.getFromClassPath(name, LuaJRunner.class);
  }
  
  public static String getFromClassPath(String name, Class<?> clazz)
  {
    return URLTools.getFromClassPath(name,clazz).getPath();
  }
  
  public static LuaValue luaGetFromClassPath(LuaState state, LuaValue name) throws LuaError, UnwindThrowable {
    return valueOf(getFromClassPath(name.checkString()));
  }
}
