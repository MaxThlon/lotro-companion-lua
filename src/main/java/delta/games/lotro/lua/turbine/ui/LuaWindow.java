package delta.games.lotro.lua.turbine.ui;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

import delta.common.ui.swing.JFrame;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * LuaWindow library for lua scripts.
 * @author MaxThlon
 */
final class LuaWindow {

  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = LuaObject.callInherit(lua, -3, "Turbine", "UI", "Control");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", LuaWindow::constructor);
  	LuaTools.setFunction(lua, -1, -3, "Activate", LuaWindow::activate);
    LuaTools.setFunction(lua, -1, -3, "Close", LuaWindow::close);

    LuaTools.setFunction(lua, -1, -3, "GetMinimumWidth", LuaWindow::getMinimumWidth);
    LuaTools.setFunction(lua, -1, -3, "SetMinimumWidth", LuaWindow::setMinimumWidth);
    LuaTools.setFunction(lua, -1, -3, "GetMinimumHeight", LuaWindow::getMinimumHeight);
    LuaTools.setFunction(lua, -1, -3, "SetMinimumHeight", LuaWindow::setMinimumHeight);
    LuaTools.setFunction(lua, -1, -3, "GetMinimumSize", LuaWindow::getMinimumSize);
    LuaTools.setFunction(lua, -1, -3, "SetMinimumSize", LuaWindow::setMinimumSize);
    LuaTools.setFunction(lua, -1, -3, "GetMaximumWidth", LuaWindow::getMaximumWidth);
    LuaTools.setFunction(lua, -1, -3, "SetMaximumWidth", LuaWindow::setMaximumWidth);
    LuaTools.setFunction(lua, -1, -3, "GetMaximumHeight", LuaWindow::getMaximumHeight);
    LuaTools.setFunction(lua, -1, -3, "SetMaximumHeight", LuaWindow::setMaximumHeight);
    LuaTools.setFunction(lua, -1, -3, "GetMaximumSize", LuaWindow::getMaximumSize);
    LuaTools.setFunction(lua, -1, -3, "SetMaximumSize", LuaWindow::setMaximumSize);

    LuaTools.setFunction(lua, -1, -3, "GetRotation", LuaWindow::getRotation);
    LuaTools.setFunction(lua, -1, -3, "SetRotation", LuaWindow::setRotation);

    LuaTools.setFunction(lua, -1, -3, "GetText", LuaWindow::getText);
    LuaTools.setFunction(lua, -1, -3, "SetText", LuaWindow::setText);

    lua.setField(-2, "Window");
    return error;
  }

  public static DefaultWindowController windowControllerSelf(Lua lua, int index) {
    return LuaObject.objectSelf(lua, index, DefaultWindowController.class);
  }

  private static int constructor(Lua lua) {
  	DefaultWindowController windowController = new DefaultWindowController() {
      @Override
      protected JFrame build() {
        JFrame frame = super.build();
        frame.getContentPane().setLayout(null);
        return frame;
      }
      /*@Override
      protected JComponent buildContents() {
        return null;
      }*/
    };
    
    LuaTools.invokeAndWait(lua, () -> windowController.getWindow());
    return LuaControl.controlInheritedConstructor(lua, 1, windowController);
  }
  
  private static int activate(Lua lua) {
    DefaultWindowController windowController = windowControllerSelf(lua, 1);
    
    SwingUtilities.invokeLater(() -> windowController.bringToFront());
    return 1;
  }

  private static int close(Lua lua) {
    /*
     LuaValue self = lua.get();
      objectSelf(lua, self).getFrame().setClosed(true);
    } catch (PropertyVetoException e) {
      e.printStackTrace();
    }*/
    return 1;
  }

  private static int getMinimumWidth(Lua lua) {
  	JFrame frame = windowControllerSelf(lua, 1).getFrame();
  	
  	LuaTools.invokeAndWait(lua, () ->  lua.push(frame.getMinimumSize().width));
    return 1;
  }
  
  private static int setMinimumWidth(Lua lua) {
    JFrame frame = windowControllerSelf(lua, 1).getFrame();
    Dimension dimension = frame.getMinimumSize();
    
    dimension.width = (int)lua.toNumber(2);
    SwingUtilities.invokeLater(() -> frame.setMinimumSize(dimension));
    return 1;
  }
  
  private static int getMinimumHeight(Lua lua) {
  	JFrame frame = windowControllerSelf(lua, 1).getFrame();
  	
  	LuaTools.invokeAndWait(lua, () ->  lua.push(frame.getMinimumSize().height));
    return 1;
  }
  
  private static int setMinimumHeight(Lua lua) {
    JFrame frame = windowControllerSelf(lua, 1).getFrame();
    Dimension dimension = frame.getMinimumSize();

    dimension.height = (int)lua.toNumber(2);
    SwingUtilities.invokeLater(() -> frame.setMinimumSize(dimension));
    return 1;
  }
  
  private static int getMinimumSize(Lua lua) {
    JFrame frame = windowControllerSelf(lua, 1).getFrame();
    Dimension dimension = frame.getMinimumSize();
    lua.push(dimension.width);
    lua.push(dimension.height);
    return 1;
  }
  
  private static int setMinimumSize(Lua lua) {
    JFrame frame = windowControllerSelf(lua, 1).getFrame();
    Dimension dimension = frame.getMinimumSize();
    dimension.width = (int)lua.toNumber(2);
    dimension.height = (int)lua.toNumber(3);
    SwingUtilities.invokeLater(() -> frame.setMinimumSize(dimension));
    return 1;
  }

  private static int getMaximumWidth(Lua lua) {
  	JFrame frame = windowControllerSelf(lua, 1).getFrame();
  	
  	lua.push(frame.getMaximumSize().width);
    return 1;
  }
  
  private static int setMaximumWidth(Lua lua) {
    JFrame frame = windowControllerSelf(lua, 1).getFrame();
    Dimension dimension = frame.getMaximumSize();
    dimension.width = (int)lua.toNumber(2);

    SwingUtilities.invokeLater(() -> frame.setMaximumSize(dimension));
    return 1;
  }
  
  private static int getMaximumHeight(Lua lua) {
    lua.push(windowControllerSelf(lua, 1).getFrame().getMaximumSize().height);
    return 1;
  }
  
  private static int setMaximumHeight(Lua lua) {
    JFrame frame = windowControllerSelf(lua, 1).getFrame();
    Dimension dimension = frame.getMaximumSize();
    dimension.height = (int)lua.toNumber(2);
    SwingUtilities.invokeLater(() -> frame.setMaximumSize(dimension));
    return 1;
  }
  
  private static int getMaximumSize(Lua lua) {
    JFrame frame = windowControllerSelf(lua, 1).getFrame();
    Dimension dimension = frame.getMaximumSize();
    
    lua.push(dimension.width);
    lua.push(dimension.height);
    return 1;
  }
  
  private static int setMaximumSize(Lua lua) {
    JFrame frame = windowControllerSelf(lua, 1).getFrame();
    Dimension dimension = frame.getMaximumSize();
    dimension.width = (int)lua.toNumber(2);
    dimension.height = (int)lua.toNumber(3);
    SwingUtilities.invokeLater(() -> frame.setMaximumSize(dimension));
    return 1;
  }
  
  private static int getRotation(Lua lua) {
    return 1;
  }
  
  private static int setRotation(Lua lua) {
    return 1;
  }
  
  private static int getText(Lua lua) {
    lua.push(windowControllerSelf(lua, 1).getFrame().getTitle());
    return 1;
  }
  
  private static int setText(Lua lua) {
  	JFrame frame = windowControllerSelf(lua, 1).getFrame();
  	String title = lua.toString(2);
  	SwingUtilities.invokeLater(() -> frame.setTitle(title));
    return 1;
  }
}
