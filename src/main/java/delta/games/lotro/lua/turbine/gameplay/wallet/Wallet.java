package delta.games.lotro.lua.turbine.gameplay.wallet;

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
public class Wallet
{
  public static LuaTable add(LuaState state, LuaTable gameplayEnv,
                             LuaFunction luaClass, LuaValue luaObjectClass) throws LuaError, UnwindThrowable {
    
    WalletItem.add(state, gameplayEnv, luaClass, luaObjectClass);
    
    LuaTable luaWalletClass = luaClass.call(state, luaObjectClass).checkTable();
    RegisteredFunction.bind(luaWalletClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", Wallet::constructor),
        RegisteredFunction.of("GetSize", Wallet::getSize),
        RegisteredFunction.of("GetItem", Wallet::getItem)
    });
    
    gameplayEnv.rawset("Wallet", luaWalletClass);
    return luaWalletClass;
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
}
