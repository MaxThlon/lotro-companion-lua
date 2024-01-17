package delta.games.lotro.lua.turbine.gameplay.attribute;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class ClassAttributes
{
  /**
   * Initialize lua Entity package
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
  	LuaTools.pushClass(lua, "Turbine", "Gameplay", "Attributes");
  	lua.push((JFunction)ClassAttributes::constructor);
    lua.setField(-2, "Constructor");

    lua.setField(-2, "ClassAttributes");
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }
}
