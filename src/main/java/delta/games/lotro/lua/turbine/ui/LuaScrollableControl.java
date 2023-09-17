package delta.games.lotro.lua.turbine.ui;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.OperationHelper;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.games.lotro.lua.turbine.Turbine;

/**
 * LuaScrollableControl library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaScrollableControl {

  public static LuaTable add(LuaState state,
                             LuaTable uiMetatable,
                             LuaFunction luaClass,
                             LuaValue luaControlClass) throws LuaError, UnwindThrowable {

    LuaTable luaScrollableControlClass = OperationHelper.call(state, luaClass, luaControlClass).checkTable();
    RegisteredFunction.bind(luaScrollableControlClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaScrollableControl::Constructor),
        RegisteredFunction.of("GetHorizontalScrollBar", LuaScrollableControl::GetHorizontalScrollBar),
        RegisteredFunction.of("SetHorizontalScrollBar", LuaScrollableControl::SetHorizontalScrollBar),
        RegisteredFunction.of("GetVerticalScrollBar", LuaScrollableControl::GetVerticalScrollBar),
        RegisteredFunction.of("SetVerticalScrollBar", LuaScrollableControl::SetVerticalScrollBar)
    });
    
    uiMetatable.rawset("ScrollableControl", luaScrollableControlClass);
    
    return luaScrollableControlClass;
  }

  public static LuaValue Constructor(LuaState state, LuaValue self) throws LuaError {
    JScrollPane jScrollPane=new JScrollPane();
    LuaControl.ControlInheritedConstructor(state, self, jScrollPane);
    return Constants.NIL;
  }
  
  public static LuaValue scrollableControlInheritedConstructor(LuaState state, LuaValue self, Object objectSelf) throws LuaError {
    LuaControl.ControlInheritedConstructor(state, self, objectSelf);
    return Constants.NIL;
  }
  
  public static LuaValue GetHorizontalScrollBar(LuaState state, LuaValue self) throws LuaError {
    return LuaControl.luaObjectFromJComponent(Turbine.objectSelf(state, self, JScrollPane.class).getHorizontalScrollBar());
  }

  public static LuaValue SetHorizontalScrollBar(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Turbine.objectSelf(state, self, JScrollPane.class).setHorizontalScrollBar(
        Turbine.objectSelf(state, value, JScrollBar.class)
    );
    
    return Constants.NIL;
  }
  
  public static LuaValue GetVerticalScrollBar(LuaState state, LuaValue self) throws LuaError {
    return LuaControl.luaObjectFromJComponent(Turbine.objectSelf(state, self, JScrollPane.class).getVerticalScrollBar());
  }
  
  public static LuaValue SetVerticalScrollBar(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Turbine.objectSelf(state, self, JScrollPane.class).setVerticalScrollBar(
        Turbine.objectSelf(state, value, JScrollBar.class)
    );
    return Constants.NIL;
  }
}
