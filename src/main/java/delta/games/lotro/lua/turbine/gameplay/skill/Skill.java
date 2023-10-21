package delta.games.lotro.lua.turbine.gameplay.skill;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaBoolean;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.function.RegisteredFunction;

/**
 * ActiveSkill library for lua scripts.
 * @author MaxThlon
 */
public class Skill {
  public static void add(LuaState state, LuaTable gameplayEnv) {
    SkillInfo.add(state, gameplayEnv);
    ActiveSkill.add(state, gameplayEnv);
    
    gameplayEnv.rawset("Skill", RegisteredFunction.bind(new RegisteredFunction[]{
        RegisteredFunction.of("GetSkillInfo", Skill::getSkillInfo)
    }));
  }

  public static LuaValue getSkillInfo(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
}

