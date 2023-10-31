package delta.games.lotro.lua.turbine.gameplay.skill;

import delta.games.lotro.lua.turbine.object.LuaObject;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;

/**
 * ActiveSkill library for lua scripts.
 * @author MaxThlon
 */
public class ActiveSkill {
  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = LuaObject.callInherit(lua, -3, "Turbine", "Object");
  	if (error != Lua.LuaError.OK) return error;
    lua.push((JFunction)ActiveSkill::constructor);
    lua.setField(-2, "Constructor");
    lua.push((JFunction)ActiveSkill::isUsable);
    lua.setField(-2, "IsUsable");
    lua.push((JFunction)ActiveSkill::getBaseCooldown);
    lua.setField(-2, "GetBaseCooldown");
    lua.push((JFunction)ActiveSkill::getCooldown);
    lua.setField(-2, "GetCooldown");
    lua.push((JFunction)ActiveSkill::getResetTime);
    lua.setField(-2, "GetResetTime");

    lua.setField(-2, "ActiveSkill");
    return error;
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }

  private static int isUsable(Lua lua) {
    return 1;
  }
  
  private static int getBaseCooldown(Lua lua) {
    return 1;
  }
  
  private static int getCooldown(Lua lua) {
    return 1;
  }
  
  private static int getResetTime(Lua lua) {
    return 1;
  }
}

