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
   */
  public static void add(Lua lua, int envIndex) {
  	SkillInfo.add(lua, envIndex);
  	ActiveSkill.add(lua, envIndex);
    
  	LuaTools.pushClass(lua, "Turbine", "Object");
    lua.push((JFunction)Skill::constructor);
    lua.setField(-2, "Constructor");
    lua.push((JFunction)Skill::getSkillInfo);
    lua.setField(-2, "GetSkillInfo");
    
    lua.setField(-2, "Skill");
  }

  private static int constructor(Lua lua) {
    return 1;
  }

  private static int getSkillInfo(Lua lua) {
    return 1;
  }
}

