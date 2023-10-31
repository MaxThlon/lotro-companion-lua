package delta.games.lotro.lua.turbine.gameplay.wallet;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class Wallet
{
  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = WalletItem.add(lua);
  	if (error != Lua.LuaError.OK) return error;

  	error = LuaObject.callInherit(lua, -3, "Turbine", "Object");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", Wallet::constructor);
    LuaTools.setFunction(lua, -1, -3, "GetSize", Wallet::getSize);
    LuaTools.setFunction(lua, -1, -3, "GetItem", Wallet::getItem);

    if ((error = lua.pCall(1, 1)) != Lua.LuaError.OK) return error;
    lua.setField(-2, "Wallet");
    return error;
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }
  
  private static int getSize(Lua lua) {
    return 1;
  }
  
  private static int getItem(Lua lua) {
    return 1;
  }
}
