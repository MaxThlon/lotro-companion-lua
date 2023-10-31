package delta.games.lotro.lua.turbine.ui;

import javax.swing.JTextArea;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * LuaTextBox library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaTextBox {

  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	if ((error = LuaObject.callInherit(lua, -3, "Turbine", "UI", "Label")) != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", LuaTextBox::constructor);

    lua.setField(-2, "TextBox");
    return error;
  }

  public static int constructor(Lua lua) {
    JTextArea jTextArea = new JTextArea();
    LuaControl.controlInheritedConstructor(lua, 1, jTextArea);

    return 1;
  }
}
