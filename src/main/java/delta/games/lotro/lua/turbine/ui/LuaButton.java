package delta.games.lotro.lua.turbine.ui;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.OperationHelper;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lua.turbine.Turbine;

/**
 * UI library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaButton {

  public static void add(LuaState state,
                         LuaTable uiMetatable,
                         LuaValue luaLabelClass) throws LuaError, UnwindThrowable {

    LuaTable luaButtonClass = OperationHelper.call(state, Turbine._luaClass, luaLabelClass).checkTable();
    RegisteredFunction.bind(luaButtonClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaButton::Constructor),
    });
    
    uiMetatable.rawset("Button", luaButtonClass);
  }
  
  public static JButton jButtonSelf(LuaState state, LuaValue self) throws LuaError {
    return Turbine.objectSelf(state, self, JButton.class);
  }

  public static LuaValue Constructor(LuaState state, LuaValue self) throws LuaError {
    JButton jButton = GuiFactory.buildButton("meu");
    
    LuaControl.controlInheritedConstructor(state, self, jButton);
    return Constants.NIL;
  }
}
