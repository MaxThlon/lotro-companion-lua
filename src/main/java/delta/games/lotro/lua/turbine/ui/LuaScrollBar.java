package delta.games.lotro.lua.turbine.ui;

import javax.swing.JScrollBar;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * LuaScrollBar library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaScrollBar {

  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = LuaObject.callInherit(lua, -3, "Turbine", "UI", "Control");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", LuaScrollBar::constructor);
  	LuaTools.setFunction(lua, -1, -3, "GetOrientation", LuaScrollBar::getOrientation);
    LuaTools.setFunction(lua, -1, -3, "SetOrientation", LuaScrollBar::setOrientation);
    
    LuaTools.setFunction(lua, -1, -3, "GetMinimum", LuaScrollBar::getMinimum);
    LuaTools.setFunction(lua, -1, -3, "SetMinimum", LuaScrollBar::setMinimum);
    LuaTools.setFunction(lua, -1, -3, "GetMaximum", LuaScrollBar::getMaximum);
    LuaTools.setFunction(lua, -1, -3, "SetMaximum", LuaScrollBar::setMaximum);

    LuaTools.setFunction(lua, -1, -3, "GetSmallChange", LuaScrollBar::getSmallChange);
    LuaTools.setFunction(lua, -1, -3, "SetSmallChange", LuaScrollBar::setSmallChange);
    LuaTools.setFunction(lua, -1, -3, "GetLargeChange", LuaScrollBar::getLargeChange);
    LuaTools.setFunction(lua, -1, -3, "SetLargeChange", LuaScrollBar::setLargeChange);

    LuaTools.setFunction(lua, -1, -3, "GetValue", LuaScrollBar::getValue);
    LuaTools.setFunction(lua, -1, -3, "SetValue", LuaScrollBar::setValue);

    LuaTools.setFunction(lua, -1, -3, "GetDecrementButton", LuaScrollBar::getDecrementButton);
    LuaTools.setFunction(lua, -1, -3, "SetDecrementButton", LuaScrollBar::setDecrementButton);
    LuaTools.setFunction(lua, -1, -3, "GetIncrementButton", LuaScrollBar::getIncrementButton);
    LuaTools.setFunction(lua, -1, -3, "SetIncrementButton", LuaScrollBar::setIncrementButton);
    LuaTools.setFunction(lua, -1, -3, "GetThumbButton", LuaScrollBar::getThumbButton);
    LuaTools.setFunction(lua, -1, -3, "SetThumbButton", LuaScrollBar::setThumbButton);

    lua.setField(-2, "ScrollBar");
    return error;
  }

  private static int constructor(Lua lua) {
    JScrollBar jScrollBar = new JScrollBar();
    LuaControl.controlInheritedConstructor(lua, 1, jScrollBar);
    return 1;
  }
  
  private static int getOrientation(Lua lua) {
    lua.push(LuaObject.objectSelf(lua, 1, JScrollBar.class).getOrientation());
    return 1;
  }

  private static int setOrientation(Lua lua) {
    LuaObject.objectSelf(lua, 1, JScrollBar.class).setOrientation((int)lua.toNumber(2));
    return 1;
  }

  private static int getMinimum(Lua lua) {
    lua.push(LuaObject.objectSelf(lua, 1, JScrollBar.class).getMinimum());
    return 1;
  }
  
  private static int setMinimum(Lua lua) {
    LuaObject.objectSelf(lua, 1, JScrollBar.class).setMinimum((int)lua.toNumber(2));
    return 1;
  }
  
  private static int getMaximum(Lua lua) {
    lua.push(LuaObject.objectSelf(lua, 1, JScrollBar.class).getMaximum());
    return 1;
  }
  
  private static int setMaximum(Lua lua) {
    LuaObject.objectSelf(lua, 1, JScrollBar.class).setMaximum((int)lua.toNumber(2));
    return 1;
  }
  
  private static int getSmallChange(Lua lua) {
    return 1;
  }
  
  private static int setSmallChange(Lua lua) {
    return 1;
  }
  
  private static int getLargeChange(Lua lua) {
    return 1;
  }
  
  private static int setLargeChange(Lua lua) {
    return 1;
  }
  
  private static int getValue(Lua lua) {
    lua.push(LuaObject.objectSelf(lua, 1, JScrollBar.class).getValue());
    return 1;
  }
  
  private static int setValue(Lua lua) {
    LuaObject.objectSelf(lua, 1, JScrollBar.class).setValue((int)lua.toNumber(2));
    return 1;
  }
  
  private static int getDecrementButton(Lua lua) {
    return 1;
  }
  
  private static int setDecrementButton(Lua lua) {
    return 1;
  }
  
  private static int getIncrementButton(Lua lua) {
    return 1;
  }
  
  private static int setIncrementButton(Lua lua) {
    return 1;
  }
  
  private static int getThumbButton(Lua lua) {
    return 1;
  }
  
  private static int setThumbButton(Lua lua) {
    return 1;
  }
}
