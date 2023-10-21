package delta.games.lotro.lua.turbine.gameplay.bank;

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
public class Bank
{
  public static LuaTable add(LuaState state, LuaTable gameplayEnv,
                             LuaFunction luaClass, LuaValue luaObjectClass) throws LuaError, UnwindThrowable {
    
    BankItem.add(state, gameplayEnv, luaClass, luaObjectClass);
    
    LuaTable luaBankClass = luaClass.call(state, luaObjectClass).checkTable();
    RegisteredFunction.bind(luaBankClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", Bank::constructor),
        RegisteredFunction.of("IsAvailable", Bank::isAvailable),
        RegisteredFunction.of("GetCapacity", Bank::getCapacity),
        RegisteredFunction.of("GetCount", Bank::getCount),
        RegisteredFunction.of("GetItem", Bank::getItem),
        
        RegisteredFunction.of("GetChestCount", Bank::getChestCount),
        RegisteredFunction.of("GetChestName", Bank::getChestName)
    });
    
    gameplayEnv.rawset("Bank", luaBankClass);
    return luaBankClass;
  }
  
  public static LuaValue constructor(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaBoolean isAvailable(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
  
  public static LuaNumber getCapacity(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaNumber getCount(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaValue getItem(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaNumber getChestCount(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaString getChestName(LuaState state, LuaValue self) {
    return Constants.EMPTYSTRING;
  }
}
