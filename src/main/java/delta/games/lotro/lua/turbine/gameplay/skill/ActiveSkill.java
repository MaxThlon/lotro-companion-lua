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
public class ActiveSkill {
  public static void add(LuaState state, LuaTable gameplayEnv) {
    gameplayEnv.rawset("ActiveSkill", RegisteredFunction.bind(new RegisteredFunction[]{
        RegisteredFunction.of("IsUsable", ActiveSkill::isUsable),
        RegisteredFunction.of("GetBaseCooldown", ActiveSkill::getBaseCooldown),
        RegisteredFunction.of("GetCooldown", ActiveSkill::getCooldown),
        RegisteredFunction.of("GetResetTime", ActiveSkill::getResetTime)
    }));
  }
  
  public static LuaBoolean isUsable(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
  
  public static LuaValue getBaseCooldown(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaValue getCooldown(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaValue getResetTime(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
}

