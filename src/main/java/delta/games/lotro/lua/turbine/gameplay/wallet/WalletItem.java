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
public class WalletItem
{
  public static LuaTable add(LuaState state, LuaTable gameplayEnv,
                             LuaFunction luaClass, LuaValue luaObjectClass) throws LuaError, UnwindThrowable {
    LuaTable luaWalletItemClass = luaClass.call(state, luaObjectClass).checkTable();
    RegisteredFunction.bind(luaWalletItemClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", WalletItem::constructor),
        RegisteredFunction.of("GetName", WalletItem::getName),
        RegisteredFunction.of("GetDescription", WalletItem::getDescription),
        RegisteredFunction.of("IsAccountItem", WalletItem::isAccountItem),
        RegisteredFunction.of("GetQuantity", WalletItem::getQuantity),
        RegisteredFunction.of("GetMaxQuantity", WalletItem::getMaxQuantity),
        
        RegisteredFunction.of("GetImage", WalletItem::getImage),
        RegisteredFunction.of("GetSmallImage", WalletItem::getSmallImage)
    });

    gameplayEnv.rawset("WalletItem", luaWalletItemClass);
    return luaWalletItemClass;
  }
  
  private static LuaValue constructor(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  private static LuaString getName(LuaState state, LuaValue self) {
    return Constants.EMPTYSTRING;
  }
  
  private static LuaValue getDescription(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  private static LuaBoolean isAccountItem(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }

  private static LuaNumber getQuantity(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  private static LuaNumber getMaxQuantity(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  private static LuaValue getImage(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  private static LuaValue getSmallImage(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
}
