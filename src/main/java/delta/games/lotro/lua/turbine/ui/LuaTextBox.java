package delta.games.lotro.lua.turbine.ui;

import javax.swing.JTextArea;
import javax.xml.ws.Holder;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * LuaTextBox library for lua scripts.
 * 
 * @author MaxThlon
 */
final class LuaTextBox {
  /**
   * Initialize lua TextBox package
   * 
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
    LuaTools.pushClass(lua, "Turbine", "UI", "Label");
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", LuaTextBox::constructor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsReadOnly", LuaTextBox::isReadOnly);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetReadOnly", LuaTextBox::setReadOnly);

    lua.setField(LuaTools.relativizeIndex(envIndex, -1), "TextBox");
  }

  private static int constructor(Lua lua) {
    Holder<JTextArea> jTextArea = new Holder<JTextArea>();

    LuaTools.invokeAndWait(lua, () -> jTextArea.value = GuiFactory.buildTextArea("meu", false));
    LuaControl.controlInheritedConstructor(lua, 1, jTextArea.value);
    return 0;
  }

  private static int isReadOnly(Lua lua) {
    lua.push(false);
    return 1;
  }

  private static int setReadOnly(Lua lua) {
    return 0;
  }
}
