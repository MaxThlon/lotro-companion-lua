package delta.games.lotro.lua.turbine.ui;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * DragDropInfo library for lua scripts.
 * @author MaxThlon
 */
final class LuaDragDropInfo {
  /**
   * Initialize lua DragDropInfo package
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
  	LuaTools.pushClass(lua, "Turbine", "Object");
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsSuccessful", LuaDragDropInfo::isSuccessful);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetSuccessful", LuaDragDropInfo::setSuccessful);
    lua.setField(-2, "DragDropInfo");
  }

  private static int isSuccessful(Lua lua) {
    lua.push(false);
    return 1;
  }
  
  private static int setSuccessful(Lua lua) {
    return 0;
  }
}
