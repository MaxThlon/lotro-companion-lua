package delta.games.lotro.lua.turbine.gameplay.attribute;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class Attributes
{
  /**
   * Initialize lua Entity package
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
  	LuaTools.pushClass(lua, "Turbine", "Gameplay", "EntityReference");
  	lua.push((JFunction)Attributes::constructor);
    lua.setField(-2, "Constructor");

    lua.setField(-2, "Entity");
  }
  
  /**
   * Push a already existing or new Attributes instance.
   * @param lua .
   * @param envIndex .
   * @param errfunc .
   */
  public static void pushAttributes(Lua lua, int envIndex, int errfunc) {
  	LuaTools.newClassInstance(lua, envIndex, (relativeEnvIndex) -> {
  		LuaTools.pushValue(lua, relativeEnvIndex.intValue(), "Turbine", "Gameplay", "Attributes");
		});
    /*LuaObject.ObjectInheritedConstructor(
        lua,
        -1,
        plugin,
        null,
        null
    );*/
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }
}
