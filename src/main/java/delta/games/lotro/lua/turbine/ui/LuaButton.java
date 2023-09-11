package delta.games.lotro.lua.turbine.ui;

import javax.swing.JButton;

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

/**
 * UI library for luaJ scripts.
 * @author DAM
 */
public abstract class LuaButton {

  public static void add(LuaState state,
                         LuaTable uiMetatable,
                         LuaFunction luaClass,
                         LuaValue luaLabelClass) throws LuaError, UnwindThrowable {

    LuaTable luaButtonClass = OperationHelper.call(state, luaClass, luaLabelClass).checkTable();
    RegisteredFunction.bind(luaButtonClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaButton::Constructor),
    });
    
    uiMetatable.rawset("Button", luaButtonClass);
  }

  public static LuaValue Constructor(LuaState state, LuaValue self) throws LuaError {
    JButton jButton = GuiFactory.buildButton("meu");
    
    LuaControl.ControlInheritedConstructor(state, self, jButton);
/*
    jButton.addActionListener(e -> {
      LuaValue luaButton = LuaControl.luaObjectFromJComponent(jButton);
      LuaValue luaMouseClick = luaButton.metatag(state, valueOf("Click"));
      if (luaMouseClick != Constants.NIL)
        try {
          OperationHelper.call(
              state,
              luaMouseClick,
              luaButton,
              Constants.NIL
          );
        } catch (LuaError e1) {
          e1.printStackTrace();
        } catch (UnwindThrowable e1) {
          e1.printStackTrace();
        }
    });
    */
    return Constants.NIL;
  }
}
