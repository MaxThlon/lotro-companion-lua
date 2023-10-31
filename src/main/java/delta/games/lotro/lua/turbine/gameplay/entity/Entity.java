package delta.games.lotro.lua.turbine.gameplay.entity;

import delta.games.lotro.lua.turbine.object.LuaObject;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class Entity
{
  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = LuaObject.callInherit(lua, -3, "Turbine", "Object");
  	if (error != Lua.LuaError.OK) return error;
    lua.push((JFunction)Entity::constructor);
    lua.setField(-2, "Constructor");
    lua.push((JFunction)Entity::getName);
    lua.setField(-2, "GetName");
    lua.push((JFunction)Entity::isLocalPlayer);
    lua.setField(-2, "IsLocalPlayer");

    lua.setField(-2, "Entity");
    
    return error;
  }
  
  public static int constructor(Lua lua) {
    return 1;
  }
  
  public static int getName(Lua lua) {
    return 1;
  }
  
  public static int isLocalPlayer(Lua lua) {
    return 1;
  }
}
