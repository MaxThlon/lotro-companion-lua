package delta.games.lotro.lua.turbine.ui;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * LuaDisplay library for lua scripts.
 * @author MaxThlon
 */
final class LuaDisplay {
  /**
   * Initialize lua Display package
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
  	lua.createTable(0, 0);
  	LuaTools.setFunction(lua, -1, "IsA", LuaObject::isA);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetWidth", LuaDisplay::getWidth);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetHeight", LuaDisplay::getHeight);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetSize", LuaDisplay::getSize);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetMouseX", LuaDisplay::getMouseX);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetMouseY", LuaDisplay::getMouseY);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetMousePosition", LuaDisplay::getMousePosition);
    lua.setField(LuaTools.relativizeIndex(envIndex, -1), "Display");
  }

  private static int getWidth(Lua lua) {
    lua.push(Toolkit.getDefaultToolkit().getScreenSize().width);
    return 1;
  }
  
  private static int getHeight(Lua lua) {
    lua.push(Toolkit.getDefaultToolkit().getScreenSize().height);
    return 1;
  }
  
  private static int getSize(Lua lua) {
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    lua.push(dimension.width);
    lua.push(dimension.height);
    return 2;
  }
  
  private static int getMouseX(Lua lua) {
    lua.push(MouseInfo.getPointerInfo().getLocation().x);
    return 1;
  }

  private static int getMouseY(Lua lua) {
    lua.push(MouseInfo.getPointerInfo().getLocation().y);
    return 1;
  }
  
  private static int getMousePosition(Lua lua) {
    Point location = MouseInfo.getPointerInfo().getLocation();
    lua.push(location.x);
    lua.push(location.y);
    return 2;
  }
}
