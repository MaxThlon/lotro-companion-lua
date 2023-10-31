package delta.games.lotro.lua.utils;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import delta.common.utils.url.URLTools;
import delta.games.lotro.lua.LuaModule;

/**
 * Tool methods related to URLs management of lua requires.
 * @author MaxThlon
 */
public abstract class URLToolsLua
{

  /*public static void add(Lua lua, LuaValue env) {
    LibFunction.setGlobalLibrary(state, env, "URLToolsLua", RegisteredFunction.bind(new RegisteredFunction[]{
        RegisteredFunction.of("getFromClassPath", URLToolsLua::luaGetFromClassPath)
    }));
  }*/
  
  public static String getFromClassPath(String name)
  {
    return URLToolsLua.getFromClassPath(name, LuaModule.class).toString();
  }
  
  public static InputStream getFromClassPathAsStream(String name)
  {
    return getFromClassPathAsStream(name, LuaModule.class);
  }
  
  public static InputStream getFromClassPathAsStream(String name, Class<?> clazz)
  {
    return clazz.getResourceAsStream(name);
  }
  
  public static Path getFromClassPath(String name, Class<?> clazz)
  {
    try
    {
      return Paths.get(URLTools.getFromClassPath(name,clazz).toURI());
    }
    catch (URISyntaxException e)
    {
      return null;
    }
  }
}
