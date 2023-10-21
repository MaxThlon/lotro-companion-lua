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

import delta.common.ui.swing.JFrame;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.utils.LuaTools;

/**
 * LuaWindow library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaWindow {

  public static void add(LuaState state,
                         LuaTable uiEnv,
                         LuaValue luaControlClass) throws LuaError, UnwindThrowable {

    LuaTable luaWindowClass = OperationHelper.call(state, Turbine._luaClass, luaControlClass).checkTable();
    RegisteredFunction.bind(luaWindowClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaWindow::constructor),
        RegisteredFunction.of("Activate", LuaWindow::activate),
        RegisteredFunction.of("Close", LuaWindow::close),
        
        RegisteredFunction.of("GetMinimumWidth", LuaWindow::getMinimumWidth),
        RegisteredFunction.of("SetMinimumWidth", LuaWindow::setMinimumWidth),
        RegisteredFunction.of("GetMinimumHeight", LuaWindow::getMinimumHeight),
        RegisteredFunction.of("SetMinimumHeight", LuaWindow::setMinimumHeight),
        RegisteredFunction.ofV("GetMinimumSize", LuaWindow::getMinimumSize),
        RegisteredFunction.of("SetMinimumSize", LuaWindow::setMinimumSize),
        RegisteredFunction.of("GetMaximumWidth", LuaWindow::getMaximumWidth),
        RegisteredFunction.of("SetMaximumWidth", LuaWindow::setMaximumWidth),
        RegisteredFunction.of("GetMaximumHeight", LuaWindow::getMaximumHeight),
        RegisteredFunction.of("SetMaximumHeight", LuaWindow::setMaximumHeight),
        RegisteredFunction.ofV("GetMaximumSize", LuaWindow::getMaximumSize),
        RegisteredFunction.of("SetMaximumSize", LuaWindow::setMaximumSize),
        
        RegisteredFunction.of("GetRotation", LuaWindow::getRotation),
        RegisteredFunction.of("SetRotation", LuaWindow::setRotation),

        RegisteredFunction.of("GetText", LuaWindow::getText),
        RegisteredFunction.of("SetText", LuaWindow::setText)
    });
    
    uiEnv.rawset("Window", luaWindowClass);
  }

  public static DefaultWindowController objectSelf(LuaState state, LuaValue self) throws LuaError {
    return LuaTools.objectSelf(state, self, DefaultWindowController.class);
  }
  
  public static LuaValue constructor(LuaState state, LuaValue self) throws LuaError {
    DefaultWindowController defaultWindowController=new DefaultWindowController() {
      @Override
      protected JFrame build() {
        JFrame frame=super.build();
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
  
  public static LuaValue activate(LuaState state, LuaValue self) throws LuaError {
    DefaultWindowController windowController=objectSelf(state, self);
    windowController.bringToFront();
    return Constants.NIL;
  }

  public static LuaValue close(LuaState state, LuaValue self) throws LuaError {
    /*try {
      objectSelf(state, self).getFrame().setClosed(true);
    } catch (PropertyVetoException e) {
      e.printStackTrace();
    }*/
    return Constants.NIL;
  }

  public static LuaNumber getMinimumWidth(LuaState state, LuaValue self) throws LuaError {
    return valueOf(objectSelf(state, self).getFrame().getMinimumSize().width);
  }
  
  public static LuaValue setMinimumWidth(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    JFrame frame = objectSelf(state, self).getFrame();
    Dimension dimension = frame.getMinimumSize();
    dimension.width = value.checkInteger();
    frame.setMinimumSize(dimension);
    return Constants.NIL;
  }
  
  public static LuaNumber getMinimumHeight(LuaState state, LuaValue self) throws LuaError {
    return valueOf(objectSelf(state, self).getFrame().getMinimumSize().height);
  }
  
  public static LuaValue setMinimumHeight(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    JFrame frame = objectSelf(state, self).getFrame();
    Dimension dimension = frame.getMinimumSize();
    dimension.height = value.checkInteger();
    frame.setMinimumSize(dimension);
    return Constants.NIL;
  }
  
  public static Varargs getMinimumSize(LuaState state, Varargs varargs) throws LuaError {
    JFrame frame = objectSelf(state, varargs.first()).getFrame();
    Dimension dimension = frame.getMinimumSize();
    
    return varargsOf(valueOf(dimension.width), valueOf(dimension.height));
  }
  
  public static LuaValue setMinimumSize(LuaState state, LuaValue self, LuaValue width, LuaValue height) throws LuaError {
    JFrame frame = objectSelf(state, self).getFrame();
    Dimension dimension = frame.getMinimumSize();
    dimension.width = width.checkInteger();
    dimension.height = height.checkInteger();
    frame.setMinimumSize(dimension);
    return Constants.NIL;
  }

  public static LuaNumber getMaximumWidth(LuaState state, LuaValue self) throws LuaError {
    return valueOf(objectSelf(state, self).getFrame().getMaximumSize().width);
  }
  
  public static LuaValue setMaximumWidth(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    JFrame frame = objectSelf(state, self).getFrame();
    Dimension dimension = frame.getMaximumSize();
    dimension.width = value.checkInteger();
    frame.setMaximumSize(dimension);
    return Constants.NIL;
  }
  
  public static LuaNumber getMaximumHeight(LuaState state, LuaValue self) throws LuaError {
    return valueOf(objectSelf(state, self).getFrame().getMaximumSize().height);
  }
  
  public static LuaValue setMaximumHeight(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    JFrame frame = objectSelf(state, self).getFrame();
    Dimension dimension = frame.getMaximumSize();
    dimension.height = value.checkInteger();
    frame.setMaximumSize(dimension);
    return Constants.NIL;
  }
  
  public static Varargs getMaximumSize(LuaState state, Varargs varargs) throws LuaError {
    JFrame frame = objectSelf(state, varargs.first()).getFrame();
    Dimension dimension = frame.getMaximumSize();
    
    return varargsOf(valueOf(dimension.width), valueOf(dimension.height));
  }
  
  public static LuaValue setMaximumSize(LuaState state, LuaValue self, LuaValue width, LuaValue height) throws LuaError {
    JFrame frame = objectSelf(state, self).getFrame();
    Dimension dimension = frame.getMaximumSize();
    dimension.width = width.checkInteger();
    dimension.height = height.checkInteger();
    frame.setMaximumSize(dimension);
    return Constants.NIL;
  }
  
  public static LuaNumber getRotation(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaValue setRotation(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getText(LuaState state, LuaValue self) throws LuaError {
    return valueOf(objectSelf(state, self).getFrame().getTitle());
  }
  
  public static LuaValue setText(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    objectSelf(state, self).setTitle(value.checkString());
    return Constants.NIL;
  }
}
