package delta.games.lotro.lua.turbine.ui.lotro;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * LotroDragDropInfo library for lua scripts.
 * @author MaxThlon
 */
final class LuaLotroDragDropInfo {
  /**
   * Initialize lua LotroDragDropInfo package
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
  	LuaTools.pushClass(lua, "Turbine", "UI", "DragDropInfo");
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetShortcut", LuaLotroDragDropInfo::getShortcut);
    lua.setField(-2, "DragDropInfo");
  }

  private static int getShortcut(Lua lua) {
    lua.push("meu");
    return 1;
  }
}
