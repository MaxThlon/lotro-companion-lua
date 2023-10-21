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
public class BankItem
{
  public static LuaTable add(LuaState state, LuaTable gameplayEnv,
                             LuaFunction luaClass, LuaValue luaObjectClass) throws LuaError, UnwindThrowable {
    LuaTable luaBankClass = luaClass.call(state, luaObjectClass).checkTable();
    RegisteredFunction.bind(luaBankClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", BankItem::constructor),
        RegisteredFunction.of("GetChest", BankItem::getChest),
        RegisteredFunction.of("GetBoundOwner", BankItem::getBoundOwner),
        RegisteredFunction.of("GetQuantity", BankItem::getQuantity),
        RegisteredFunction.of("GetItem", BankItem::GetWearState)
    });
    
    gameplayEnv.rawset("Bank", luaBankClass);
    return luaBankClass;
  }
  
  private static LuaValue constructor(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  private static LuaValue getChest(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  private static LuaValue getBoundOwner(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  private static LuaNumber getQuantity(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  private static LuaValue GetWearState(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
}
