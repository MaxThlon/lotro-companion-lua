package delta.games.lotro.lua.turbine.gameplay.skill;

import delta.games.lotro.lua.turbine.object.LuaObject;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;

/**
 * ActiveSkill library for lua scripts.
 * @author MaxThlon
 */
public class Skill {
  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = SkillInfo.add(lua);
  	if (error != Lua.LuaError.OK) return error;
  	error = ActiveSkill.add(lua);
    if (error != Lua.LuaError.OK) return error;
    
    error = LuaObject.callInherit(lua, -3, "Turbine", "Object");
    if (error != Lua.LuaError.OK) return error;
    lua.push((JFunction)Skill::constructor);
    lua.setField(-2, "Constructor");
    lua.push((JFunction)Skill::getSkillInfo);
    lua.setField(-2, "GetSkillInfo");
    
    lua.setField(-2, "Skill");
    return error;
  }

  public static int constructor(Lua lua) {
    return 1;
  }

  private static int getSkillInfo(Lua lua) {
    return 1;
  }
}

