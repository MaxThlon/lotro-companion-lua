package delta.games.lotro.lua.turbine.ui;

import javax.swing.JTextArea;

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
 * LuaTextBox library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaTextBox {

  public static void add(LuaState state,
                         LuaTable uiEnv,
                         LuaValue luaLabelClass) throws LuaError, UnwindThrowable {

    LuaTable luaTextBoxClass = OperationHelper.call(state, Turbine._luaClass, luaLabelClass).checkTable();
    RegisteredFunction.bind(luaTextBoxClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaTextBox::Constructor),
    });
    
    uiEnv.rawset("TextBox", luaTextBoxClass);
  }

  public static LuaValue Constructor(LuaState state, LuaValue self) throws LuaError {
    JTextArea jTextArea = new JTextArea();
    LuaControl.controlInheritedConstructor(state, self, jTextArea);

    return Constants.NIL;
  }
}
