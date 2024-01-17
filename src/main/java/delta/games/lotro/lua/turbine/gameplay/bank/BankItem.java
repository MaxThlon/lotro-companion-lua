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
   */
  public static void add(Lua lua, int envIndex) {
  	LuaTools.pushClass(lua, "Turbine", "Object");
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", BankItem::constructor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetChest", BankItem::getChest);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetBoundOwner", BankItem::getBoundOwner);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetQuantity", BankItem::getQuantity);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetWearState", BankItem::getWearState);

    lua.setField(-2, "BankItem");
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
