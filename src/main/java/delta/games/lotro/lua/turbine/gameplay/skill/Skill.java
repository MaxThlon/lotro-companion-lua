package delta.games.lotro.lua.turbine.gameplay.skill;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;

/**
 * ActiveSkill library for lua scripts.
 * @author MaxThlon
 */
public class Skill {
  /**
   * Initialize lua Skill package
   * @param lua .
   * @param envIndex .
   * @param errfunc .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError add(Lua lua, int envIndex, int errfunc) {
  	Lua.LuaError error;
  	if ((error = SkillInfo.add(lua, envIndex, errfunc)) != Lua.LuaError.OK) return error;
  	if ((error = ActiveSkill.add(lua, envIndex, errfunc)) != Lua.LuaError.OK) return error;
    
  	if ((error = LuaTools.pushClass(lua, errfunc, "Turbine", "Object")) != Lua.LuaError.OK) return error;
    lua.push((JFunction)Skill::constructor);
    lua.setField(-2, "Constructor");
    lua.push((JFunction)Skill::getSkillInfo);
    lua.setField(-2, "GetSkillInfo");
    
    lua.setField(-2, "Skill");
    return error;
  }

  private static int constructor(Lua lua) {
    return 1;
  }

  private static int getSkillInfo(Lua lua) {
    return 1;
  }
}

