package delta.games.lotro.lua.turbine.gameplay.bank;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class BankItem
{
  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = LuaObject.callInherit(lua, -3, "Turbine", "Object");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", BankItem::constructor);
    LuaTools.setFunction(lua, -1, -3, "GetChest", BankItem::getChest);
    LuaTools.setFunction(lua, -1, -3, "GetBoundOwner", BankItem::getBoundOwner);
    LuaTools.setFunction(lua, -1, -3, "GetQuantity", BankItem::getQuantity);
    LuaTools.setFunction(lua, -1, -3, "GetWearState", BankItem::getWearState);

    lua.setField(-2, "BankItem");
    
    return error;
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }
  
  private static int getChest(Lua lua) {
    return 1;
  }
  
  private static int getBoundOwner(Lua lua) {
    return 1;
  }
  
  private static int getQuantity(Lua lua) {
    return 1;
  }
  
  private static int getWearState(Lua lua) {
    return 1;
  }
}
