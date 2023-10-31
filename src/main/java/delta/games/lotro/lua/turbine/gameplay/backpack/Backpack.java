package delta.games.lotro.lua.turbine.gameplay.backpack;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class Backpack
{
  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = LuaObject.callInherit(lua, -3, "Turbine", "Object");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", Backpack::constructor);
    LuaTools.setFunction(lua, -1, -3, "GetSize", Backpack::getSize);
    LuaTools.setFunction(lua, -1, -3, "GetItem", Backpack::getItem);
    LuaTools.setFunction(lua, -1, -3, "PerformItemDrop", Backpack::performItemDrop);
    LuaTools.setFunction(lua, -1, -3, "PerformShortcutDrop", Backpack::performShortcutDrop);

    lua.setField(-2, "Backpack");
    
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
  
  private static int performItemDrop(Lua lua) {
    return 1;
  }
  
  private static int performShortcutDrop(Lua lua) {
    return 1;
  }
}
