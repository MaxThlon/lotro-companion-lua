package delta.games.lotro.lua.turbine.ui;

import javax.swing.JButton;
import javax.xml.ws.Holder;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * LuaButton library for lua scripts.
 * @author MaxThlon
 */
final class LuaButton {
  /**
   * Initialize lua LuaButton package
   * @param lua .
   * @param envIndex .
   * @param errfunc .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError add(Lua lua, int envIndex, int errfunc) {
  	Lua.LuaError error;
  	error = LuaTools.pushClass(lua, errfunc, "Turbine", "UI", "Label");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", LuaButton::constructor);
    lua.setField(-2, "Button");
    return error;
  }
  
  public static JButton jButtonSelf(Lua lua, int index) {
    return LuaObject.objectSelf(lua, index, JButton.class);
  }

  private static int constructor(Lua lua) {
    Holder<JButton> jButton = new Holder<JButton>();

    LuaTools.invokeAndWait(lua, () -> jButton.value = GuiFactory.buildButton("meu"));
    LuaControl.controlInheritedConstructor(lua, 1, jButton.value);
    return 1;
  }
}
