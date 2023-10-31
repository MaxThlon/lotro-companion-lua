package delta.games.lotro.lua.turbine.gameplay.wallet;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class WalletItem
{
  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;

  	if ((error = LuaObject.callInherit(lua, -3, "Turbine", "Object")) != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", WalletItem::constructor);
    LuaTools.setFunction(lua, -1, -3, "GetName", WalletItem::getName);
    LuaTools.setFunction(lua, -1, -3, "GetDescription", WalletItem::getDescription);
    LuaTools.setFunction(lua, -1, -3, "IsAccountItem", WalletItem::isAccountItem);
    LuaTools.setFunction(lua, -1, -3, "GetQuantity", WalletItem::getQuantity);
    LuaTools.setFunction(lua, -1, -3, "GetMaxQuantity", WalletItem::getMaxQuantity);

    LuaTools.setFunction(lua, -1, -3, "GetImage", WalletItem::getImage);
    LuaTools.setFunction(lua, -1, -3, "GetSmallImage", WalletItem::getSmallImage);

    lua.setField(-2, "WalletItem");
    return error;
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
