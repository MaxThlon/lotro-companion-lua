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
public abstract class LuaDisplay {

  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = LuaObject.callInherit(lua, -3, "Turbine", "Object");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", LuaDisplay::constructor);
    LuaTools.setFunction(lua, -1, -3, "GetWidth", LuaDisplay::getWidth);
    LuaTools.setFunction(lua, -1, -3, "GetHeight", LuaDisplay::GetHeight);
    LuaTools.setFunction(lua, -1, -3, "GetSize", LuaDisplay::GetSize);
    LuaTools.setFunction(lua, -1, -3, "GetMouseX", LuaDisplay::GetMouseX);
    LuaTools.setFunction(lua, -1, -3, "GetMouseY", LuaDisplay::GetMouseY);
    LuaTools.setFunction(lua, -1, -3, "GetMousePosition", LuaDisplay::GetMousePosition);

    if ((error =lua.pCall(0, 1)) != Lua.LuaError.OK) return error;
    lua.setField(-2, "Display");
    return error;
  }

  private static int constructor(Lua lua) {
    return 1;
  }

  private static int getWidth(Lua lua) {
    lua.push(Toolkit.getDefaultToolkit().getScreenSize().width);
    return 1;
  }
  
  private static int GetHeight(Lua lua) {
    lua.push(Toolkit.getDefaultToolkit().getScreenSize().height);
    return 1;
  }
  
  private static int GetSize(Lua lua) {
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    lua.push(dimension.width);
    lua.push(dimension.height);
    return 1;
  }
  
  private static int GetMouseX(Lua lua) {
    lua.push(MouseInfo.getPointerInfo().getLocation().x);
    return 1;
  }

  private static int GetMouseY(Lua lua) {
    lua.push(MouseInfo.getPointerInfo().getLocation().y);
    return 1;
  }
  
  private static int GetMousePosition(Lua lua) {
    Point location = MouseInfo.getPointerInfo().getLocation();
    lua.push(location.x);
    lua.push(location.y);
    return 1;
  }
}
