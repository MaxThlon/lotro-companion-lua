package delta.games.lotro.lua.turbine.gameplay.effect;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaBoolean;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaNumber;
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
public class Effect
{
  public static LuaTable add(LuaState state, LuaTable gameplayEnv,
                             LuaFunction luaClass, LuaValue luaObjectClass) throws LuaError, UnwindThrowable {
    LuaTable luaEffectClass = luaClass.call(state, luaObjectClass).checkTable();
    RegisteredFunction.bind(luaEffectClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", Effect::constructor),
        RegisteredFunction.of("GetID", Effect::getID),
        RegisteredFunction.of("GetName", Effect::getName),
        RegisteredFunction.of("GetDescription", Effect::getDescription),
        RegisteredFunction.of("GetCategory", Effect::getCategory),
        RegisteredFunction.of("IsDebuff", Effect::isDebuff),
        RegisteredFunction.of("IsCurable", Effect::isCurable),
        RegisteredFunction.of("IsDispellable", Effect::isDispellable),
        RegisteredFunction.of("GetDuration", Effect::getDuration),
        RegisteredFunction.of("GetStartTime", Effect::getStartTime),
        RegisteredFunction.of("GetIcon", Effect::getIcon)
    });
    gameplayEnv.rawset("Effect", luaEffectClass);
    
    EffectList.add(state, gameplayEnv, luaClass, luaObjectClass);
    
    return luaEffectClass;
  }
  
  public static LuaValue constructor(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getID(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaString getName(LuaState state, LuaValue self) {
    return Constants.EMPTYSTRING;
  }
  
  public static LuaValue getDescription(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getCategory(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaBoolean isDebuff(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
  
  public static LuaBoolean isCurable(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
  
  public static LuaBoolean isDispellable(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
  
  public static LuaNumber getDuration(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaNumber getStartTime(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaValue getIcon(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
}
