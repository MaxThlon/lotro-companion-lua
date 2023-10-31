package delta.games.lotro.lua.turbine.ui.lotro;

import javax.swing.JButton;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.turbine.ui.LuaControl;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * Quickslot library for lua scripts.
 * @author MaxThlon
 */
public abstract class Quickslot {

  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = LuaObject.callInherit(lua, -3, "Turbine", "UI", "Control");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", Quickslot::constructor);
    LuaTools.setFunction(lua, -1, -3, "GetShortcut", Quickslot::getShortcut);
    LuaTools.setFunction(lua, -1, -3, "SetShortcut", Quickslot::setShortcut);
    LuaTools.setFunction(lua, -1, -3, "IsUseOnRightClick", Quickslot::isUseOnRightClick);
    LuaTools.setFunction(lua, -1, -3, "SetUseOnRightClick", Quickslot::setUseOnRightClick);

    lua.setField(-2, "Quickslot");
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
  
  private static int getShortcut(Lua lua) {
    return 1;
  }
  
  private static int setShortcut(Lua lua) {
    return 1;
  }
  
  private static int isUseOnRightClick(Lua lua) {
    return 1;
  }
  
  private static int setUseOnRightClick(Lua lua) {
    return 1;
  }
}
