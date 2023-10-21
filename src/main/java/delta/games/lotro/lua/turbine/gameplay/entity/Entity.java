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
public class Entity
{
  public static LuaTable add(LuaState state, LuaTable gameplayEnv,
                             LuaFunction luaClass, LuaValue luaObjectClass) throws LuaError, UnwindThrowable {
    LuaTable luaEntityClass = luaClass.call(state, luaObjectClass).checkTable();
    RegisteredFunction.bind(luaEntityClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", Entity::constructor),
        RegisteredFunction.of("GetName", Entity::getName),
        RegisteredFunction.of("IsLocalPlayer", Entity::isLocalPlayer),
    });
    
    gameplayEnv.rawset("Entity", luaEntityClass);
    return luaEntityClass;
  }
  
  public static LuaValue constructor(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaString getName(LuaState state, LuaValue self) {
    return Constants.EMPTYSTRING;
  }
  
  public static LuaBoolean isLocalPlayer(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
}
