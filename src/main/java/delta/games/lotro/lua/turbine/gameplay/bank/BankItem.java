package delta.games.lotro.lua.turbine.gameplay.bank;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class BankItem
{
  /**
   * Initialize lua BankItem package
   * @param lua .
   * @param envIndex .
   * @param errfunc .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError add(Lua lua, int envIndex, int errfunc) {
  	Lua.LuaError error;
  	error = LuaTools.pushClass(lua, errfunc, "Turbine", "Object");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", BankItem::constructor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetChest", BankItem::getChest);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetBoundOwner", BankItem::getBoundOwner);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetQuantity", BankItem::getQuantity);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetWearState", BankItem::getWearState);

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
