package delta.games.lotro.lua.turbine.ui;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * LuaScrollableControl library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaScrollableControl {
  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = LuaObject.callInherit(lua, -3, "Turbine", "UI", "Control");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", LuaScrollableControl::constructor);
  	LuaTools.setFunction(lua, -1, -3, "GetHorizontalScrollBar", LuaScrollableControl::getHorizontalScrollBar);
  	LuaTools.setFunction(lua, -1, -3, "SetHorizontalScrollBar", LuaScrollableControl::setHorizontalScrollBar);
    LuaTools.setFunction(lua, -1, -3, "GetVerticalScrollBar", LuaScrollableControl::getVerticalScrollBar);
    LuaTools.setFunction(lua, -1, -3, "SetVerticalScrollBar", LuaScrollableControl::setVerticalScrollBar);
    
    lua.setField(-2, "ScrollableControl");
    return error;
  }

  private static int constructor(Lua lua) {
    JScrollPane jScrollPane=new JScrollPane();
    LuaControl.controlInheritedConstructor(lua, 1, jScrollPane);
    return 1;
  }
  
  public static void scrollableControlInheritedConstructor(Lua lua, int indexSelf, Object objectSelf) {
    LuaControl.controlInheritedConstructor(lua, indexSelf, objectSelf);
  }
  
  private static int getHorizontalScrollBar(Lua lua) {
  	LuaObject.findLuaObjectFromObject(LuaObject.objectSelf(lua, 1, JScrollPane.class).getHorizontalScrollBar()).push();
    return 1;
  }

  private static int setHorizontalScrollBar(Lua lua) {
    if (lua.type(2) != Lua.LuaType.NIL) {
      LuaObject.objectSelf(lua, 1, JScrollPane.class).setHorizontalScrollBar(
          LuaObject.objectSelf(lua, 2, JScrollBar.class)
      );
    }
    return 1;
  }
  
  private static int getVerticalScrollBar(Lua lua) {
  	LuaObject.findLuaObjectFromObject(LuaObject.objectSelf(lua, 1, JScrollPane.class).getVerticalScrollBar()).push();
    return 1;
  }
  
  private static int setVerticalScrollBar(Lua lua) {
    if (lua.type(2) != Lua.LuaType.NIL) {
      LuaObject.objectSelf(lua, 1, JScrollPane.class).setVerticalScrollBar(
          LuaObject.objectSelf(lua, 2, JScrollBar.class)
      );
    }
    return 1;
  }
}
