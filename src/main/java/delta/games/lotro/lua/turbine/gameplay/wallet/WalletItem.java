package delta.games.lotro.lua.turbine.gameplay.wallet;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class WalletItem
{
  /**
   * Initialize lua WalletItem package
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
  	LuaTools.pushClass(lua, "Turbine", "Object");
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", WalletItem::constructor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetName", WalletItem::getName);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetDescription", WalletItem::getDescription);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsAccountItem", WalletItem::isAccountItem);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetQuantity", WalletItem::getQuantity);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetMaxQuantity", WalletItem::getMaxQuantity);

    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetImage", WalletItem::getImage);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetSmallImage", WalletItem::getSmallImage);

    lua.setField(-2, "WalletItem");
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }
  
  private static int getName(Lua lua) {
    return 1;
  }
  
  private static int getDescription(Lua lua) {
    return 1;
  }
  
  private static int isAccountItem(Lua lua) {
    return 1;
  }

  private static int getQuantity(Lua lua) {
    return 1;
  }
  
  private static int getMaxQuantity(Lua lua) {
    return 1;
  }
  
  private static int getImage(Lua lua) {
    return 1;
  }
  
  private static int getSmallImage(Lua lua) {
    return 1;
  }
}
