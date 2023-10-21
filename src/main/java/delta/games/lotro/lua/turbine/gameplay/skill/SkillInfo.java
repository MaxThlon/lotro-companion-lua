package delta.games.lotro.lua.turbine.gameplay.skill;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaBoolean;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaString;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.function.RegisteredFunction;

/**
 * SkillInfo library for lua scripts.
 * @author MaxThlon
 */
public class SkillInfo {
  public static void add(LuaState state, LuaTable gameplayEnv) {
    gameplayEnv.rawset("SkillInfo", RegisteredFunction.bind(new RegisteredFunction[]{
        RegisteredFunction.of("GetType", SkillInfo::getType),
        RegisteredFunction.of("GetName", SkillInfo::getName),
        RegisteredFunction.of("GetDescription", SkillInfo::getDescription),

        RegisteredFunction.of("GetIconImageID", SkillInfo::getIconImageID)
    }));
  }
  
  public static LuaValue getType(LuaState state, LuaValue self) {
    return Constants.NIL;
  }

  public static LuaString getName(LuaState state, LuaValue self) {
    return Constants.EMPTYSTRING;
  }
  
  public static LuaValue getDescription(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getIconImageID(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
}

