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
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.utils.LuaTools;

/**
 * LuaScrollableControl library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaScrollableControl {

  public static LuaTable add(LuaState state,
                             LuaTable uiEnv,
                             LuaValue luaControlClass) throws LuaError, UnwindThrowable {

    LuaTable luaScrollableControlClass = OperationHelper.call(state, Turbine._luaClass, luaControlClass).checkTable();
    RegisteredFunction.bind(luaScrollableControlClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaScrollableControl::constructor),
        RegisteredFunction.of("GetHorizontalScrollBar", LuaScrollableControl::getHorizontalScrollBar),
        RegisteredFunction.of("SetHorizontalScrollBar", LuaScrollableControl::setHorizontalScrollBar),
        RegisteredFunction.of("GetVerticalScrollBar", LuaScrollableControl::getVerticalScrollBar),
        RegisteredFunction.of("SetVerticalScrollBar", LuaScrollableControl::setVerticalScrollBar)
    });
    
    uiEnv.rawset("ScrollableControl", luaScrollableControlClass);
    
    return luaScrollableControlClass;
  }

  public static LuaValue constructor(LuaState state, LuaValue self) throws LuaError {
    JScrollPane jScrollPane=new JScrollPane();
    LuaControl.controlInheritedConstructor(state, self, jScrollPane);
    return Constants.NIL;
  }
  
  public static LuaValue scrollableControlInheritedConstructor(LuaState state, LuaValue self, Object objectSelf) throws LuaError {
    LuaControl.controlInheritedConstructor(state, self, objectSelf);
    return Constants.NIL;
  }
  
  public static LuaValue getHorizontalScrollBar(LuaState state, LuaValue self) throws LuaError {
    return LuaTools.findLuaObjectFromObject(LuaTools.objectSelf(state, self, JScrollPane.class).getHorizontalScrollBar());
  }

  public static LuaValue setHorizontalScrollBar(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    if (value!=Constants.NIL) {
      LuaTools.objectSelf(state, self, JScrollPane.class).setHorizontalScrollBar(
          LuaTools.objectSelf(state, value, JScrollBar.class)
      );
    }
    return Constants.NIL;
  }
  
  public static LuaValue getVerticalScrollBar(LuaState state, LuaValue self) throws LuaError {
    return LuaTools.findLuaObjectFromObject(LuaTools.objectSelf(state, self, JScrollPane.class).getVerticalScrollBar());
  }
  
  public static LuaValue setVerticalScrollBar(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    if (value!=Constants.NIL) {
      LuaTools.objectSelf(state, self, JScrollPane.class).setVerticalScrollBar(
          LuaTools.objectSelf(state, value, JScrollBar.class)
      );
    }
    return Constants.NIL;
  }
}
