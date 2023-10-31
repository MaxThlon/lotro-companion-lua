package delta.games.lotro.lua.turbine.ui;

import javax.swing.JButton;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * LuaButton library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaButton {

  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = LuaObject.callInherit(lua, -3, "Turbine", "UI", "Label");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", LuaButton::constructor);
    lua.setField(-2, "Button");
    return error;
  }
  
  public static JButton jButtonSelf(Lua lua, int index) {
    return LuaObject.objectSelf(lua, index, JButton.class);
  }

  private static int constructor(Lua lua) {
    JButton jButton = GuiFactory.buildButton("meu");
    
    LuaControl.controlInheritedConstructor(lua, 1, jButton);
    return 1;
  }
}
