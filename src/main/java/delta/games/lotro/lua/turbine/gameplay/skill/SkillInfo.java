package delta.games.lotro.lua.turbine.gameplay.skill;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;

/**
 * SkillInfo library for lua scripts.
 * @author MaxThlon
 */
public class SkillInfo {
  public static Lua.LuaError add(Lua lua, int envIndex, int errfunc) {
  	Lua.LuaError error;
  	error = LuaTools.pushClass(lua, errfunc, "Turbine", "Object");
  	if (error != Lua.LuaError.OK) return error;
    lua.push((JFunction)SkillInfo::constructor);
    lua.setField(-2, "Constructor");
    lua.push((JFunction)SkillInfo::getType);
    lua.setField(-2, "GetType");
    lua.push((JFunction)SkillInfo::getName);
    lua.setField(-2, "GetName");
    lua.push((JFunction)SkillInfo::getDescription);
    lua.setField(-2, "GetDescription");
    lua.push((JFunction)SkillInfo::getIconImageID);
    lua.setField(-2, "GetIconImageID");

    lua.setField(-2, "SkillInfo");
    return error;
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }

  private static int getType(Lua lua) {
    return 1;
  }

  private static int getName(Lua lua) {
    return 1;
  }
  
  private static int getDescription(Lua lua) {
    return 1;
  }
  
  private static int getIconImageID(Lua lua) {
    return 1;
  }
}

