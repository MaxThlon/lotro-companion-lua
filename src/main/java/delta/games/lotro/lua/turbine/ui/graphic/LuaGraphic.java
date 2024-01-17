package delta.games.lotro.lua.turbine.ui.graphic;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * Graphic library for lua scripts.
 * 
 * @author MaxThlon
 */
public final class LuaGraphic {
  /**
   * Initialize lua Graphic package
   * 
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
    LuaTools.pushClass(lua, "Turbine", "Object");
    LuaTools.setFunction(lua, -1, "IsA", LuaObject::isA);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", LuaGraphic::constructor);
    lua.setField(LuaTools.relativizeIndex(envIndex, -1), "Graphic");
  }

  private static int constructor(Lua lua) {
    return 0;
  }
}
