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
  /**
   * Initialize lua Quickslot package
   * @param lua .
   * @param envIndex .
   * @param errfunc .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError add(Lua lua, int envIndex, int errfunc) {
  	Lua.LuaError error;
  	error = LuaTools.pushClass(lua, errfunc, "Turbine", "UI", "Control");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", Quickslot::constructor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetShortcut", Quickslot::getShortcut);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetShortcut", Quickslot::setShortcut);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsUseOnRightClick", Quickslot::isUseOnRightClick);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetUseOnRightClick", Quickslot::setUseOnRightClick);

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
