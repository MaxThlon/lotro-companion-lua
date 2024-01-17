package delta.games.lotro.lua.turbine.gameplay.backpack;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class Backpack
{
  /**
   * Initialize lua Backpack package
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
  	LuaTools.pushClass(lua, "Turbine", "Object");
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", Backpack::constructor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetSize", Backpack::getSize);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetItem", Backpack::getItem);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "PerformItemDrop", Backpack::performItemDrop);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "PerformShortcutDrop", Backpack::performShortcutDrop);

    lua.setField(-2, "Backpack");
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
