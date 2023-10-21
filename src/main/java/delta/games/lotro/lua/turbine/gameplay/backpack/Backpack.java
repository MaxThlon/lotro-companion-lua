package delta.games.lotro.lua.turbine.gameplay.backpack;

import org.squiddev.cobalt.Constants;
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
public class Backpack
{
  public static LuaTable add(LuaState state, LuaTable gameplayEnv,
                             LuaFunction luaClass, LuaValue luaObjectClass) throws LuaError, UnwindThrowable {
    LuaTable luaBackpackClass = luaClass.call(state, luaObjectClass).checkTable();
    RegisteredFunction.bind(luaBackpackClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", Backpack::constructor),
        RegisteredFunction.of("GetSize", Backpack::getSize),
        RegisteredFunction.of("GetItem", Backpack::getItem),
        RegisteredFunction.of("PerformItemDrop", Backpack::performItemDrop),
        RegisteredFunction.of("PerformShortcutDrop", Backpack::performShortcutDrop)
    });
    
    gameplayEnv.rawset("Backpack", luaBackpackClass);
    return luaBackpackClass;
  }
  
  public static LuaValue constructor(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaNumber getSize(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaValue getItem(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue performItemDrop(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue performShortcutDrop(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
}
