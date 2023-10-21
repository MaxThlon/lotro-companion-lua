package delta.games.lotro.lua.turbine.gameplay.entity;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaBoolean;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaString;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

/**
 * @author MaxThlon
 */
public class Actor
{
  public static LuaTable add(LuaState state, LuaTable gameplayEnv,
                             LuaFunction luaClass, LuaValue luaEntityClass) throws LuaError, UnwindThrowable {
    LuaTable luaActorClass = luaClass.call(state, luaEntityClass).checkTable();
    RegisteredFunction.bind(luaActorClass, new RegisteredFunction[]{
      RegisteredFunction.of("Constructor", Actor::constructor),
      RegisteredFunction.of("GetLevel", Actor::getLevel),
      RegisteredFunction.of("GetTarget", Actor::getTarget),
      RegisteredFunction.of("GetEffects", Actor::getEffects),

      RegisteredFunction.of("GetMorale", Actor::getMorale),
      RegisteredFunction.of("GetBaseMaxMorale", Actor::getBaseMaxMorale),
      RegisteredFunction.of("GetTemporaryMorale", Actor::getTemporaryMorale),
      RegisteredFunction.of("GetMaxTemporaryMorale", Actor::getMaxTemporaryMorale),
      
      RegisteredFunction.of("GetPower", Actor::getPower),
      RegisteredFunction.of("GetBaseMaxPower", Actor::getBaseMaxPower),
      RegisteredFunction.of("GetTemporaryPower", Actor::getTemporaryPower),
      RegisteredFunction.of("GetMaxTemporaryPower", Actor::getMaxTemporaryPower)
    });
    gameplayEnv.rawset("Actor", luaActorClass);

    return luaActorClass;
  }
  
  public static LuaValue constructor(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaString getLevel(LuaState state, LuaValue self) {
    return Constants.EMPTYSTRING;
  }
  
  public static LuaBoolean getTarget(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
  
  public static LuaBoolean getEffects(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
  
  public static LuaBoolean getMorale(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
  
  public static LuaBoolean getBaseMaxMorale(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
  
  public static LuaBoolean getTemporaryMorale(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
  
  public static LuaBoolean getMaxTemporaryMorale(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
  
  public static LuaBoolean getPower(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
  
  public static LuaBoolean getBaseMaxPower(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
  
  public static LuaBoolean getTemporaryPower(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
  
  public static LuaBoolean getMaxTemporaryPower(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
}
