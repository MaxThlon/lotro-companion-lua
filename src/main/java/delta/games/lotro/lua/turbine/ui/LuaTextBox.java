package delta.games.lotro.lua.turbine.ui;

import javax.swing.JTextArea;
import javax.xml.ws.Holder;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * LuaTextBox library for lua scripts.
 * @author MaxThlon
 */
final class LuaTextBox {

  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	if ((error = LuaObject.callInherit(lua, -3, "Turbine", "UI", "Label")) != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", LuaTextBox::constructor);

    lua.setField(-2, "TextBox");
    return error;
  }

  public static int constructor(Lua lua) {
  	Holder<JTextArea> jTextArea = new Holder<JTextArea>();
    
    LuaTools.invokeAndWait(lua, () -> jTextArea.value = GuiFactory.buildTextArea("meu", false));
    LuaControl.controlInheritedConstructor(lua, 1, jTextArea.value);

    return 1;
  }
}
