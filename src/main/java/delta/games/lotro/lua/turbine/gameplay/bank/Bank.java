package delta.games.lotro.lua.turbine.gameplay.bank;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class Bank
{
  /**
   * Initialize lua Bank package
   * @param lua .
   * @param envIndex .
   * @param errfunc .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError add(Lua lua, int envIndex, int errfunc) {
  	Lua.LuaError error;
  	error = BankItem.add(lua, envIndex, errfunc);
  	if (error != Lua.LuaError.OK) return error;

    LuaTools.pushClass(lua, errfunc, "Turbine", "Object");
    if (error != Lua.LuaError.OK) return error;
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", Bank::constructor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsAvailable", Bank::isAvailable);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetCapacity", Bank::getCapacity);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetCount", Bank::getCount);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetItem", Bank::getItem);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetChestCount", Bank::getChestCount);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetChestName", Bank::getChestName);

    LuaTools.pCall(lua, 0, 1, LuaTools.relativizeIndex(errfunc, -1));
    lua.setField(-2, "Bank");
    
    return error;
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }
  
  private static int isAvailable(Lua lua) {
    return 1;
  }
  
  private static int getCapacity(Lua lua) {
    return 1;
  }
  
  private static int getCount(Lua lua) {
    return 1;
  }
  
  private static int getItem(Lua lua) {
    return 1;
  }
  
  private static int getChestCount(Lua lua) {
    return 1;
  }
  
  private static int getChestName(Lua lua) {
    return 1;
  }
}
