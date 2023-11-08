package delta.games.lotro.lua.turbine.gameplay.wallet;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class Wallet
{
  /**
   * Initialize lua Wallet package
   * @param lua .
   * @param envIndex .
   * @param errfunc .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError add(Lua lua, int envIndex, int errfunc) {
  	Lua.LuaError error;
  	error = WalletItem.add(lua, envIndex, errfunc);
  	if (error != Lua.LuaError.OK) return error;

  	error = LuaTools.pushClass(lua, errfunc, "Turbine", "Object");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", Wallet::constructor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetSize", Wallet::getSize);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetItem", Wallet::getItem);

    if ((error = LuaTools.pCall(lua, 1, 1, LuaTools.relativizeIndex(errfunc, -1))) != Lua.LuaError.OK) return error;
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
