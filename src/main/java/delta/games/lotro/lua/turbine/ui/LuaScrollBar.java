package delta.games.lotro.lua.turbine.ui;

import static org.squiddev.cobalt.ValueFactory.valueOf;

import javax.swing.JScrollBar;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaNumber;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.OperationHelper;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.utils.LuaTools;

/**
 * UI library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaScrollBar {

  public static void add(LuaState state,
                         LuaTable uiEnv,
                         LuaValue luaControlClass) throws LuaError, UnwindThrowable {

    LuaTable luaScrollBarClass = OperationHelper.call(state, Turbine._luaClass, luaControlClass).checkTable();
    RegisteredFunction.bind(luaScrollBarClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaScrollBar::constructor),
        RegisteredFunction.of("GetOrientation", LuaScrollBar::getOrientation),
        RegisteredFunction.of("SetOrientation", LuaScrollBar::setOrientation),
        
        RegisteredFunction.of("GetMinimum", LuaScrollBar::getMinimum),
        RegisteredFunction.of("SetMinimum", LuaScrollBar::setMinimum),      
        RegisteredFunction.of("GetMaximum", LuaScrollBar::getMaximum),
        RegisteredFunction.of("SetMaximum", LuaScrollBar::setMaximum),
        
        RegisteredFunction.of("GetSmallChange", LuaScrollBar::getSmallChange),
        RegisteredFunction.of("SetSmallChange", LuaScrollBar::setSmallChange),
        RegisteredFunction.of("GetLargeChange", LuaScrollBar::getLargeChange),
        RegisteredFunction.of("SetLargeChange", LuaScrollBar::setLargeChange),
        
        RegisteredFunction.of("GetValue", LuaScrollBar::getValue),
        RegisteredFunction.of("SetValue", LuaScrollBar::setValue),
        
        RegisteredFunction.of("GetDecrementButton", LuaScrollBar::getDecrementButton),
        RegisteredFunction.of("SetDecrementButton", LuaScrollBar::setDecrementButton),
        RegisteredFunction.of("GetIncrementButton", LuaScrollBar::getIncrementButton),
        RegisteredFunction.of("SetIncrementButton", LuaScrollBar::setIncrementButton),
        RegisteredFunction.of("GetThumbButton", LuaScrollBar::getThumbButton),
        RegisteredFunction.of("SetThumbButton", LuaScrollBar::setThumbButton)
    });
    
    uiEnv.rawset("ScrollBar", luaScrollBarClass);
  }

  public static LuaValue constructor(LuaState state, LuaValue self) throws LuaError {
    JScrollBar jScrollBar = new JScrollBar();
    LuaControl.controlInheritedConstructor(state, self, jScrollBar);
    return Constants.NIL;
  }
  
  public static LuaNumber getOrientation(LuaState state, LuaValue self) throws LuaError {
    return valueOf(LuaTools.objectSelf(state, self, JScrollBar.class).getOrientation());
  }

  public static LuaValue setOrientation(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    LuaTools.objectSelf(state, self, JScrollBar.class).setOrientation(value.checkInteger());

    return Constants.NIL;
  }
  
  
  public static LuaNumber getMinimum(LuaState state, LuaValue self) throws LuaError {
    return valueOf(LuaTools.objectSelf(state, self, JScrollBar.class).getMinimum());
  }
  
  public static LuaValue setMinimum(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    LuaTools.objectSelf(state, self, JScrollBar.class).setMinimum(value.checkInteger());
    return Constants.NIL;
  }
  
  public static LuaNumber getMaximum(LuaState state, LuaValue self) throws LuaError {
    return valueOf(LuaTools.objectSelf(state, self, JScrollBar.class).getMaximum());
  }
  
  public static LuaValue setMaximum(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    LuaTools.objectSelf(state, self, JScrollBar.class).setMaximum(value.checkInteger());
    return Constants.NIL;
  }
  
  public static LuaNumber getSmallChange(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaValue setSmallChange(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaNumber getLargeChange(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaValue setLargeChange(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaNumber getValue(LuaState state, LuaValue self) throws LuaError {
    return valueOf(LuaTools.objectSelf(state, self, JScrollBar.class).getValue());
  }
  
  public static LuaValue setValue(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    LuaTools.objectSelf(state, self, JScrollBar.class).setValue(value.checkInteger());
    return Constants.NIL;
  }
  
  public static LuaValue getDecrementButton(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue setDecrementButton(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue getIncrementButton(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue setIncrementButton(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue getThumbButton(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue setThumbButton(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
}
