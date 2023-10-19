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
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.games.lotro.lua.turbine.Turbine;

/**
 * UI library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaScrollBar {

  public static void add(LuaState state,
                         LuaTable uiMetatable,
                         LuaValue luaControlClass) throws LuaError, UnwindThrowable {

    LuaTable luaScrollBarClass = OperationHelper.call(state, Turbine._luaClass, luaControlClass).checkTable();
    RegisteredFunction.bind(luaScrollBarClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaScrollBar::Constructor),
        RegisteredFunction.of("GetOrientation", LuaScrollBar::GetOrientation),
        RegisteredFunction.of("SetOrientation", LuaScrollBar::SetOrientation),
        
        RegisteredFunction.of("GetMinimum", LuaScrollBar::GetMinimum),
        RegisteredFunction.of("SetMinimum", LuaScrollBar::SetMinimum),      
        RegisteredFunction.of("GetMaximum", LuaScrollBar::GetMaximum),
        RegisteredFunction.of("SetMaximum", LuaScrollBar::SetMaximum),
        
        RegisteredFunction.of("GetSmallChange", LuaScrollBar::GetSmallChange),
        RegisteredFunction.of("SetSmallChange", LuaScrollBar::GetSmallChange),
        RegisteredFunction.of("GetLargeChange", LuaScrollBar::GetLargeChange),
        RegisteredFunction.of("SetLargeChange", LuaScrollBar::SetLargeChange),
        
        RegisteredFunction.of("GetValue", LuaScrollBar::GetValue),
        RegisteredFunction.of("SetValue", LuaScrollBar::SetValue),
        
        RegisteredFunction.of("GetDecrementButton", LuaScrollBar::GetDecrementButton),
        RegisteredFunction.of("SetDecrementButton", LuaScrollBar::SetDecrementButton),
        RegisteredFunction.of("GetIncrementButton", LuaScrollBar::GetIncrementButton),
        RegisteredFunction.of("SetIncrementButton", LuaScrollBar::SetIncrementButton),
        RegisteredFunction.of("GetThumbButton", LuaScrollBar::GetThumbButton),
        RegisteredFunction.of("SetThumbButton", LuaScrollBar::SetThumbButton)
    });
    
    uiMetatable.rawset("ScrollBar", luaScrollBarClass);
  }

  public static LuaValue Constructor(LuaState state, LuaValue self) throws LuaError {
    JScrollBar jScrollBar = new JScrollBar();
    LuaControl.controlInheritedConstructor(state, self, jScrollBar);
    return Constants.NIL;
  }
  
  public static LuaNumber GetOrientation(LuaState state, LuaValue self) throws LuaError {
    return valueOf(Turbine.objectSelf(state, self, JScrollBar.class).getOrientation());
  }

  public static LuaValue SetOrientation(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Turbine.objectSelf(state, self, JScrollBar.class).setOrientation(value.checkInteger());

    return Constants.NIL;
  }
  
  
  public static LuaNumber GetMinimum(LuaState state, LuaValue self) throws LuaError {
    return valueOf(Turbine.objectSelf(state, self, JScrollBar.class).getMinimum());
  }
  
  public static LuaValue SetMinimum(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Turbine.objectSelf(state, self, JScrollBar.class).setMinimum(value.checkInteger());
    return Constants.NIL;
  }
  
  public static LuaNumber GetMaximum(LuaState state, LuaValue self) throws LuaError {
    return valueOf(Turbine.objectSelf(state, self, JScrollBar.class).getMaximum());
  }
  
  public static LuaValue SetMaximum(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Turbine.objectSelf(state, self, JScrollBar.class).setMaximum(value.checkInteger());
    return Constants.NIL;
  }
  
  public static LuaNumber GetSmallChange(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaValue SetSmallChange(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaNumber GetLargeChange(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaValue SetLargeChange(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaNumber GetValue(LuaState state, LuaValue self) throws LuaError {
    return valueOf(Turbine.objectSelf(state, self, JScrollBar.class).getValue());
  }
  
  public static LuaValue SetValue(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Turbine.objectSelf(state, self, JScrollBar.class).setValue(value.checkInteger());
    return Constants.NIL;
  }
  
  public static LuaValue GetDecrementButton(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue SetDecrementButton(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue GetIncrementButton(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue SetIncrementButton(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue GetThumbButton(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue SetThumbButton(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
}
