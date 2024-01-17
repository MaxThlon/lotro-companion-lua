package delta.games.lotro.lua.turbine.ui;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.xml.ws.Holder;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * LuaScrollableControl library for lua scripts.
 * @author MaxThlon
 */
final class LuaScrollableControl {
  /**
   * Initialize lua ScrollableControl package
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
  	LuaTools.pushClass(lua, "Turbine", "UI", "Control");
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", LuaScrollableControl::constructor);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetHorizontalScrollBar", LuaScrollableControl::getHorizontalScrollBar);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetHorizontalScrollBar", LuaScrollableControl::setHorizontalScrollBar);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetVerticalScrollBar", LuaScrollableControl::getVerticalScrollBar);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetVerticalScrollBar", LuaScrollableControl::setVerticalScrollBar);
    
    lua.setField(-2, "ScrollableControl");
  }

  private static int constructor(Lua lua) {
  	Holder<JScrollPane> jScrollPane = new Holder<JScrollPane>();
    
    LuaTools.invokeAndWait(lua, () -> jScrollPane.value = GuiFactory.buildScrollPane(null));
    LuaControl.controlInheritedConstructor(lua, 1, jScrollPane.value);
    return 1;
  }
  
  public static void scrollableControlInheritedConstructor(Lua lua, int indexSelf, Object objectSelf) {
    LuaControl.controlInheritedConstructor(lua, indexSelf, objectSelf);
  }
  
  private static int getHorizontalScrollBar(Lua lua) {
  	JScrollPane jScrollPane = LuaObject.objectSelf(lua, 1, JScrollPane.class);

  	LuaObject.findLuaObjectFromObject(jScrollPane.getHorizontalScrollBar()).push();
    return 1;
  }

  private static int setHorizontalScrollBar(Lua lua) {
    if (lua.type(2) != Lua.LuaType.NIL) {
    	JScrollPane jScrollPane = LuaObject.objectSelf(lua, 1, JScrollPane.class);
    	JScrollBar jScrollBar = LuaObject.objectSelf(lua, 2, JScrollBar.class);
    	
    	SwingUtilities.invokeLater(() -> jScrollPane.setHorizontalScrollBar(jScrollBar));
    }
    return 1;
  }
  
  private static int getVerticalScrollBar(Lua lua) {
  	JScrollPane jScrollPane = LuaObject.objectSelf(lua, 1, JScrollPane.class);
  	
  	LuaObject.findLuaObjectFromObject(jScrollPane.getVerticalScrollBar()).push();
    return 1;
  }
  
  private static int setVerticalScrollBar(Lua lua) {
    if (lua.type(2) != Lua.LuaType.NIL) {
    	JScrollPane jScrollPane = LuaObject.objectSelf(lua, 1, JScrollPane.class);
    	JScrollBar jScrollBar = LuaObject.objectSelf(lua, 2, JScrollBar.class);
    	
      SwingUtilities.invokeLater(() -> jScrollPane.setVerticalScrollBar(jScrollBar));
    }
    return 1;
  }
}
