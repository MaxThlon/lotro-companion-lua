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
   */
  public static void add(Lua lua, int envIndex) {
  	WalletItem.add(lua, envIndex);

  	LuaTools.newClassInstance(lua, envIndex, (relativeEnvIndex) -> {
  		LuaTools.pushClass(lua, "Turbine", "Object");
  		LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", Wallet::constructor);
      LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetSize", Wallet::getSize);
      LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetItem", Wallet::getItem);
  	});
    lua.setField(-2, "Wallet");
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
