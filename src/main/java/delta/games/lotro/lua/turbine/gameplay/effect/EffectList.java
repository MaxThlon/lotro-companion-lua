package delta.games.lotro.lua.turbine.gameplay.effect;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaBoolean;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaNumber;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

/**
 * @author MaxThlon
 */
public class EffectList
{
  public static LuaTable add(LuaState state, LuaTable gameplayEnv,
                             LuaFunction luaClass, LuaValue luaObjectClass) throws LuaError, UnwindThrowable {
    LuaTable luaEffectListClass = luaClass.call(state, luaObjectClass).checkTable();
    RegisteredFunction.bind(luaEffectListClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", EffectList::constructor),
        RegisteredFunction.of("GetCount", EffectList::getCount),
        RegisteredFunction.of("Get", EffectList::get),
        RegisteredFunction.of("Contains", EffectList::contains),
        RegisteredFunction.of("IndexOf", EffectList::indexOf)
    });

    gameplayEnv.rawset("EffectList", luaEffectListClass);
    return luaEffectListClass;
  }
  
  public static LuaValue constructor(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaNumber getCount(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaValue get(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaBoolean contains(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
  
  public static LuaValue indexOf(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
}
