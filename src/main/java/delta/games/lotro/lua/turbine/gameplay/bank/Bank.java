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
   */
  public static void add(Lua lua, int envIndex) {
  	BankItem.add(lua, envIndex);
    LuaTools.newClassInstance(lua, envIndex, (relativeEnvIndex) -> {
    	LuaTools.pushClass(lua, "Turbine", "Object");
      LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(relativeEnvIndex.intValue(), -1), "Constructor", Bank::constructor);
      LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(relativeEnvIndex.intValue(), -1), "IsAvailable", Bank::isAvailable);
      LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(relativeEnvIndex.intValue(), -1), "GetCapacity", Bank::getCapacity);
      LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(relativeEnvIndex.intValue(), -1), "GetCount", Bank::getCount);
      LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(relativeEnvIndex.intValue(), -1), "GetItem", Bank::getItem);
      LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(relativeEnvIndex.intValue(), -1), "GetChestCount", Bank::getChestCount);
      LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(relativeEnvIndex.intValue(), -1), "GetChestName", Bank::getChestName);
    });
    lua.setField(LuaTools.relativizeIndex(envIndex, -1), "Bank");
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
