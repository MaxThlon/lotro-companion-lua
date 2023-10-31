package delta.games.lotro.lua.turbine.gameplay.bank;

import delta.games.lotro.lua.turbine.engine.Engine;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class Bank
{
  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = BankItem.add(lua);
  	if (error != Lua.LuaError.OK) return error;

    LuaObject.callInherit(lua, -1, "Turbine", "Object");
    if (error != Lua.LuaError.OK) return error;
    LuaTools.setFunction(lua, -1, -3, "Constructor", Bank::constructor);
    LuaTools.setFunction(lua, -1, -3, "IsAvailable", Bank::isAvailable);
    LuaTools.setFunction(lua, -1, -3, "GetCapacity", Bank::getCapacity);
    LuaTools.setFunction(lua, -1, -3, "GetCount", Bank::getCount);
    LuaTools.setFunction(lua, -1, -3, "GetItem", Bank::getItem);
    LuaTools.setFunction(lua, -1, -3, "GetChestCount", Bank::getChestCount);
    LuaTools.setFunction(lua, -1, -3, "GetChestName", Bank::getChestName);

    lua.pCall(0, 1);
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
