package delta.games.lotro.lua.turbine.ui;

import javax.swing.JScrollBar;
import javax.xml.ws.Holder;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * LuaScrollBar library for lua scripts.
 * @author MaxThlon
 */
final class LuaScrollBar {
  /**
   * Initialize lua ScrollBar package
   * @param lua .
   * @param envIndex .
   * @param errfunc .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError add(Lua lua, int envIndex, int errfunc) {
  	Lua.LuaError error;
  	error = LuaTools.pushClass(lua, errfunc, "Turbine", "UI", "Control");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", LuaScrollBar::constructor);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetOrientation", LuaScrollBar::getOrientation);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetOrientation", LuaScrollBar::setOrientation);
    
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetMinimum", LuaScrollBar::getMinimum);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetMinimum", LuaScrollBar::setMinimum);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetMaximum", LuaScrollBar::getMaximum);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetMaximum", LuaScrollBar::setMaximum);

    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetSmallChange", LuaScrollBar::getSmallChange);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetSmallChange", LuaScrollBar::setSmallChange);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetLargeChange", LuaScrollBar::getLargeChange);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetLargeChange", LuaScrollBar::setLargeChange);

    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetValue", LuaScrollBar::getValue);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetValue", LuaScrollBar::setValue);

    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetDecrementButton", LuaScrollBar::getDecrementButton);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetDecrementButton", LuaScrollBar::setDecrementButton);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetIncrementButton", LuaScrollBar::getIncrementButton);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetIncrementButton", LuaScrollBar::setIncrementButton);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetThumbButton", LuaScrollBar::getThumbButton);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetThumbButton", LuaScrollBar::setThumbButton);

    lua.setField(-2, "ScrollBar");
    return error;
  }

  private static int constructor(Lua lua) {
  	Holder<JScrollBar> jScrollBar = new Holder<JScrollBar>();

    LuaTools.invokeAndWait(lua, () -> jScrollBar.value = new JScrollBar());
    LuaControl.controlInheritedConstructor(lua, 1, jScrollBar.value);
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
