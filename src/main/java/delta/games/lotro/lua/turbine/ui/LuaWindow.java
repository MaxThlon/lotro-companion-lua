package delta.games.lotro.lua.turbine.ui;

import static org.squiddev.cobalt.ValueFactory.valueOf;
import static org.squiddev.cobalt.ValueFactory.varargsOf;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaNumber;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.OperationHelper;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.Varargs;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.common.ui.swing.Frame;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.lua.turbine.Turbine;

/**
 * LuaWindow library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaWindow {

  public static void add(LuaState state,
                         LuaTable uiMetatable,
                         LuaValue luaControlClass) throws LuaError, UnwindThrowable {

    LuaTable luaWindowClass = OperationHelper.call(state, Turbine._luaClass, luaControlClass).checkTable();
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

  public static DefaultWindowController objectSelf(LuaState state, LuaValue self) throws LuaError {
    return Turbine.objectSelf(state, self, DefaultWindowController.class);
  }
  
  public static LuaValue Constructor(LuaState state, LuaValue self) throws LuaError {
    DefaultWindowController defaultWindowController=new DefaultWindowController() {
      @Override
      protected Frame build() {
        Frame frame=super.build();
        frame.setLayout(null);
        return frame;
      }
      @Override
      protected JComponent buildContents() {
        return null;
      }
    };
    LuaControl.controlInheritedConstructor(state, self, defaultWindowController);

    return Constants.NIL;
  }
  
  public static LuaValue Activate(LuaState state, LuaValue self) throws LuaError {
    DefaultWindowController windowController=objectSelf(state, self);
    windowController.bringToFront();
    return Constants.NIL;
  }

  public static LuaValue Close(LuaState state, LuaValue self) throws LuaError {
    /*try {
      objectSelf(state, self).getFrame().setClosed(true);
    } catch (PropertyVetoException e) {
      e.printStackTrace();
    }*/
    return Constants.NIL;
  }

  public static LuaNumber GetMinimumWidth(LuaState state, LuaValue self) throws LuaError {
    return valueOf(objectSelf(state, self).getFrame().getMinimumSize().width);
  }
  
  public static LuaValue SetMinimumWidth(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Frame frame = objectSelf(state, self).getFrame();
    Dimension dimension = frame.getMinimumSize();
    dimension.width = value.checkInteger();
    frame.setMinimumSize(dimension);
    return Constants.NIL;
  }
  
  public static LuaNumber GetMinimumHeight(LuaState state, LuaValue self) throws LuaError {
    return valueOf(objectSelf(state, self).getFrame().getMinimumSize().height);
  }
  
  public static LuaValue SetMinimumHeight(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Frame frame = objectSelf(state, self).getFrame();
    Dimension dimension = frame.getMinimumSize();
    dimension.height = value.checkInteger();
    frame.setMinimumSize(dimension);
    return Constants.NIL;
  }
  
  public static Varargs GetMinimumSize(LuaState state, Varargs varargs) throws LuaError {
    Frame frame = objectSelf(state, varargs.first()).getFrame();
    Dimension dimension = frame.getMinimumSize();
    
    return varargsOf(valueOf(dimension.width), valueOf(dimension.height));
  }
  
  public static LuaValue SetMinimumSize(LuaState state, LuaValue self, LuaValue width, LuaValue height) throws LuaError {
    Frame frame = objectSelf(state, self).getFrame();
    Dimension dimension = frame.getMinimumSize();
    dimension.width = width.checkInteger();
    dimension.height = height.checkInteger();
    frame.setMinimumSize(dimension);
    return Constants.NIL;
  }

  public static LuaNumber GetMaximumWidth(LuaState state, LuaValue self) throws LuaError {
    return valueOf(objectSelf(state, self).getFrame().getMaximumSize().width);
  }
  
  public static LuaValue SetMaximumWidth(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Frame frame = objectSelf(state, self).getFrame();
    Dimension dimension = frame.getMaximumSize();
    dimension.width = value.checkInteger();
    frame.setMaximumSize(dimension);
    return Constants.NIL;
  }
  
  public static LuaNumber GetMaximumHeight(LuaState state, LuaValue self) throws LuaError {
    return valueOf(objectSelf(state, self).getFrame().getMaximumSize().height);
  }
  
  public static LuaValue SetMaximumHeight(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Frame frame = objectSelf(state, self).getFrame();
    Dimension dimension = frame.getMaximumSize();
    dimension.height = value.checkInteger();
    frame.setMaximumSize(dimension);
    return Constants.NIL;
  }
  
  public static Varargs GetMaximumSize(LuaState state, Varargs varargs) throws LuaError {
    Frame frame = objectSelf(state, varargs.first()).getFrame();
    Dimension dimension = frame.getMaximumSize();
    
    return varargsOf(valueOf(dimension.width), valueOf(dimension.height));
  }
  
  public static LuaValue SetMaximumSize(LuaState state, LuaValue self, LuaValue width, LuaValue height) throws LuaError {
    Frame frame = objectSelf(state, self).getFrame();
    Dimension dimension = frame.getMaximumSize();
    dimension.width = width.checkInteger();
    dimension.height = height.checkInteger();
    frame.setMaximumSize(dimension);
    return Constants.NIL;
  }
  
  public static LuaNumber GetRotation(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaValue SetRotation(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue GetText(LuaState state, LuaValue self) throws LuaError {
    return valueOf(objectSelf(state, self).getFrame().getTitle());
  }
  
  public static LuaValue SetText(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    objectSelf(state, self).setTitle(value.checkString());
    return Constants.NIL;
  }
}
