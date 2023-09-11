package delta.games.lotro.lua.turbine.ui;

import static org.squiddev.cobalt.ValueFactory.valueOf;
import static org.squiddev.cobalt.ValueFactory.varargsOf;

import java.awt.Dimension;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaNumber;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.OperationHelper;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.Varargs;
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.games.lotro.lua.turbine.Turbine;

/**
 * LuaWindow library for lua scripts.
 * @author DAM
 */
public abstract class LuaWindow {

  public static void add(LuaState state,
                         LuaTable uiMetatable,
                         LuaFunction luaClass,
                         LuaValue luaControlClass) throws LuaError, UnwindThrowable {

    LuaTable luaWindowClass = OperationHelper.call(state, luaClass, luaControlClass).checkTable();
    RegisteredFunction.bind(luaWindowClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaWindow::Constructor),
        RegisteredFunction.of("Activate", LuaWindow::Activate),
        RegisteredFunction.of("Close", LuaWindow::Close),
        
        RegisteredFunction.of("GetMinimumWidth", LuaWindow::GetMinimumWidth),
        RegisteredFunction.of("SetMinimumWidth", LuaWindow::SetMinimumWidth),
        RegisteredFunction.of("GetMinimumHeight", LuaWindow::GetMinimumHeight),
        RegisteredFunction.of("SetMinimumHeight", LuaWindow::SetMinimumHeight),
        RegisteredFunction.ofV("GetMinimumSize", LuaWindow::GetMinimumSize),
        RegisteredFunction.of("SetMinimumSize", LuaWindow::SetMinimumSize),
        
        RegisteredFunction.of("GetMaximumWidth", LuaWindow::GetMaximumWidth),
        RegisteredFunction.of("SetMaximumWidth", LuaWindow::SetMaximumWidth),
        RegisteredFunction.of("GetMaximumHeight", LuaWindow::GetMaximumHeight),
        RegisteredFunction.of("SetMaximumHeight", LuaWindow::SetMaximumHeight),
        RegisteredFunction.ofV("GetMaximumSize", LuaWindow::GetMaximumSize),
        RegisteredFunction.of("SetMaximumSize", LuaWindow::SetMaximumSize),
        
        RegisteredFunction.of("GetRotation", LuaWindow::GetRotation),
        RegisteredFunction.of("SetRotation", LuaWindow::SetRotation),

        RegisteredFunction.of("GetText", LuaWindow::GetText),
        RegisteredFunction.of("SetText", LuaWindow::SetText)
    });
    
    uiMetatable.rawset("Window", luaWindowClass);
  }

  public static LuaValue Constructor(LuaState state, LuaValue self) throws LuaError {
    JInternalFrame jInternalFrame = new JInternalFrame();
    jInternalFrame.setLayout(null);
    jInternalFrame.setOpaque(false);
    UI.jDesktopPane.add(jInternalFrame);
    LuaControl.ControlInheritedConstructor(state, self, jInternalFrame);

    return Constants.NIL;
  }
  
  public static LuaValue Activate(LuaState state, LuaValue self) throws LuaError {
    JInternalFrame jInternalFrame = Turbine.objectSelf(state, self, JInternalFrame.class);
    jInternalFrame.setVisible(true);
    jInternalFrame.toFront();
    return Constants.NIL;
  }

  public static LuaValue Close(LuaState state, LuaValue self) throws LuaError {
    try {
      Turbine.objectSelf(state, self, JInternalFrame.class).setClosed(true);
    } catch (PropertyVetoException e) {
      e.printStackTrace();
    }
    return Constants.NIL;
  }

  public static LuaNumber GetMinimumWidth(LuaState state, LuaValue self) throws LuaError {
    return valueOf(Turbine.objectSelf(state, self, JInternalFrame.class).getMinimumSize().width);
  }
  
  public static LuaValue SetMinimumWidth(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    JInternalFrame jInternalFrame = Turbine.objectSelf(state, self, JInternalFrame.class);
    Dimension dimension = jInternalFrame.getMinimumSize();
    dimension.width = value.checkInteger();
    jInternalFrame.setMinimumSize(dimension);
    return Constants.NIL;
  }
  
  public static LuaNumber GetMinimumHeight(LuaState state, LuaValue self) throws LuaError {
    return valueOf(Turbine.objectSelf(state, self, JInternalFrame.class).getMinimumSize().height);
  }
  
  public static LuaValue SetMinimumHeight(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    JInternalFrame jInternalFrame = Turbine.objectSelf(state, self, JInternalFrame.class);
    Dimension dimension = jInternalFrame.getMinimumSize();
    dimension.height = value.checkInteger();
    jInternalFrame.setMinimumSize(dimension);
    return Constants.NIL;
  }
  
  public static Varargs GetMinimumSize(LuaState state, Varargs varargs) throws LuaError {
    JInternalFrame jInternalFrame = Turbine.objectSelf(state, varargs.first(), JInternalFrame.class);
    Dimension dimension = jInternalFrame.getMinimumSize();
    
    return varargsOf(valueOf(dimension.width), valueOf(dimension.height));
  }
  
  public static LuaValue SetMinimumSize(LuaState state, LuaValue self, LuaValue width, LuaValue height) throws LuaError {
    JInternalFrame jInternalFrame = Turbine.objectSelf(state, self, JInternalFrame.class);
    Dimension dimension = jInternalFrame.getMinimumSize();
    dimension.width = width.checkInteger();
    dimension.height = height.checkInteger();
    jInternalFrame.setMinimumSize(dimension);
    return Constants.NIL;
  }

  public static LuaNumber GetMaximumWidth(LuaState state, LuaValue self) throws LuaError {
    return valueOf(Turbine.objectSelf(state, self, JInternalFrame.class).getMaximumSize().width);
  }
  
  public static LuaValue SetMaximumWidth(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    JInternalFrame jInternalFrame = Turbine.objectSelf(state, self, JInternalFrame.class);
    Dimension dimension = jInternalFrame.getMaximumSize();
    dimension.width = value.checkInteger();
    jInternalFrame.setMaximumSize(dimension);
    return Constants.NIL;
  }
  
  public static LuaNumber GetMaximumHeight(LuaState state, LuaValue self) throws LuaError {
    return valueOf(Turbine.objectSelf(state, self, JInternalFrame.class).getMaximumSize().height);
  }
  
  public static LuaValue SetMaximumHeight(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    JInternalFrame jInternalFrame = Turbine.objectSelf(state, self, JInternalFrame.class);
    Dimension dimension = jInternalFrame.getMaximumSize();
    dimension.height = value.checkInteger();
    jInternalFrame.setMaximumSize(dimension);
    return Constants.NIL;
  }
  
  public static Varargs GetMaximumSize(LuaState state, Varargs varargs) throws LuaError {
    JInternalFrame jInternalFrame = Turbine.objectSelf(state, varargs.first(), JInternalFrame.class);
    Dimension dimension = jInternalFrame.getMaximumSize();
    
    return varargsOf(valueOf(dimension.width), valueOf(dimension.height));
  }
  
  public static LuaValue SetMaximumSize(LuaState state, LuaValue self, LuaValue width, LuaValue height) throws LuaError {
    JInternalFrame jInternalFrame = Turbine.objectSelf(state, self, JInternalFrame.class);
    Dimension dimension = jInternalFrame.getMaximumSize();
    dimension.width = width.checkInteger();
    dimension.height = height.checkInteger();
    jInternalFrame.setMaximumSize(dimension);
    return Constants.NIL;
  }
  
  public static LuaNumber GetRotation(LuaState state, LuaValue self) throws LuaError {
    return Constants.ZERO;
  }
  
  public static LuaValue SetRotation(LuaState state, LuaValue self) throws LuaError {
    return Constants.NIL;
  }
  
  public static LuaValue GetText(LuaState state, LuaValue self) throws LuaError {
    return valueOf(Turbine.objectSelf(state, self, JInternalFrame.class).getTitle());
  }
  
  public static LuaValue SetText(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Turbine.objectSelf(state, self, JInternalFrame.class).setTitle(value.checkString());
    return Constants.NIL;
  }
}
