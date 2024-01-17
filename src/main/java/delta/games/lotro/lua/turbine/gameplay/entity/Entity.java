package delta.games.lotro.lua.turbine.gameplay.entity;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class Entity
{
  /**
   * Initialize lua Entity package
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
  	LuaTools.pushClass(lua, "Turbine", "Gameplay", "EntityReference");
  	lua.push((JFunction)Entity::constructor);
    lua.setField(-2, "Constructor");
    lua.push((JFunction)Entity::getName);
    lua.setField(-2, "GetName");
    lua.push((JFunction)Entity::isLocalPlayer);
    lua.setField(-2, "IsLocalPlayer");

    lua.setField(-2, "Entity");
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }
  
  private static int getName(Lua lua) {
    return 1;
  }
  
  private static int isLocalPlayer(Lua lua) {
    return 1;
  }
}
