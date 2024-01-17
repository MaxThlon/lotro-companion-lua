package delta.games.lotro.lua.turbine.gameplay.entity;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class EntityReference
{
  /**
   * Initialize lua EntityReference package
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
  	LuaTools.pushClass(lua, "Turbine", "Object");
    lua.push((JFunction)EntityReference::constructor);
    lua.setField(-2, "Constructor");

    lua.setField(-2, "EntityReference");
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }
}
