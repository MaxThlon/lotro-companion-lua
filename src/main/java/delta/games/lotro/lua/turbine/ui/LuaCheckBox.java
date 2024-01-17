package delta.games.lotro.lua.turbine.ui;

import javax.swing.JCheckBox;
import javax.xml.ws.Holder;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * CheckBox library for lua scripts.
 * @author MaxThlon
 */
final class LuaCheckBox {
  /**
   * Initialize lua LuaCheckBox package
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
  	LuaTools.pushClass(lua, "Turbine", "UI", "Label");
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", LuaCheckBox::constructor);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsChecked", LuaCheckBox::isChecked);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetChecked", LuaCheckBox::setChecked);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetCheckAlignment", LuaCheckBox::getCheckAlignment);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetCheckAlignment", LuaCheckBox::setCheckAlignment);

    lua.setField(LuaTools.relativizeIndex(envIndex, -1), "CheckBox");
  }
  
  public static JCheckBox jCheckBoxSelf(Lua lua, int index) {
    return LuaObject.objectSelf(lua, index, JCheckBox.class);
  }

  private static int constructor(Lua lua) {
    Holder<JCheckBox> jCheckBox = new Holder<JCheckBox>();

    LuaTools.invokeAndWait(lua, () -> jCheckBox.value = GuiFactory.buildCheckbox("meu"));
    LuaControl.controlInheritedConstructor(lua, 1, jCheckBox.value);
    return 1;
  }
  
  private static int isChecked(Lua lua) {
    lua.push(false);
    return 1;
  }
  
  private static int setChecked(Lua lua) {
    return 0;
  }
  
  private static int getCheckAlignment(Lua lua) {
    lua.push(0);
    return 1;
  }
  
  private static int setCheckAlignment(Lua lua) {
    return 0;
  }
}
