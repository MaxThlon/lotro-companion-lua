package delta.games.lotro.lua.turbine.ui;

import static org.squiddev.cobalt.ValueFactory.tableOf;
import static org.squiddev.cobalt.ValueFactory.valueOf;
import static org.squiddev.cobalt.ValueFactory.varargsOf;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.RootPaneContainer;

import org.apache.log4j.Logger;
import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaBoolean;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaNumber;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.OperationHelper;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.Varargs;
import org.squiddev.cobalt.function.LibFunction;
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.common.framework.plugin.PluginManager;
import delta.common.ui.swing.Window;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lua.turbine.Apartment;
import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.turbine.ui.mouse.LuaMouseListener;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeSelectionListener;
import delta.games.lotro.lua.utils.LuaTools;

/**
 * LuaControl library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaControl {
  private static Logger LOGGER = Logger.getLogger(LuaControl.class);

  public static String jComponentKey_luaObjectSelf = "jComponentKey_luaObjectSelf";
  public static Object jComponentKey_luaEventsFunc = new Object();

  public static LuaTable add(LuaState state,
                             LuaTable uiEnv,
                             LuaFunction luaClass,
                             LuaValue luaObjectClass) throws LuaError, UnwindThrowable {

    LuaTable luaControlClass = luaClass.call(state, luaObjectClass).checkTable();
    RegisteredFunction.bind(luaControlClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaControl::controlConstructor),
        RegisteredFunction.of("GetParent", LuaControl::getParent),
        RegisteredFunction.of("SetParent", LuaControl::setParent),
        RegisteredFunction.of("GetControls", LuaControl::getControls),
        RegisteredFunction.of("Focus", LuaControl::focus),
        RegisteredFunction.of("SetVisible", LuaControl::setVisible),
        RegisteredFunction.of("IsVisible", LuaControl::isVisible),
        RegisteredFunction.of("GetLeft", LuaControl::getLeft),
        RegisteredFunction.of("SetLeft", LuaControl::setLeft),
        RegisteredFunction.of("GetTop", LuaControl::getTop),
        RegisteredFunction.of("SetTop", LuaControl::setTop),
        RegisteredFunction.ofV("GetPosition", LuaControl::getPosition),
        RegisteredFunction.of("SetPosition", LuaControl::setPosition),
        RegisteredFunction.of("GetWidth", LuaControl::getWidth),
        RegisteredFunction.of("SetWidth", LuaControl::setWidth),
        RegisteredFunction.of("GetHeight", LuaControl::getHeight),
        RegisteredFunction.of("SetHeight", LuaControl::setHeight),
        RegisteredFunction.ofV("GetSize", LuaControl::getSize),
        RegisteredFunction.of("SetSize", LuaControl::setSize),
        RegisteredFunction.of("GetZOrder", LuaControl::getZOrder),
        RegisteredFunction.of("SetZOrder", LuaControl::setZOrder),
        
        RegisteredFunction.of("GetBlendMode", LuaControl::getBlendMode),
        RegisteredFunction.of("SetBlendMode", LuaControl::setBlendMode),
        RegisteredFunction.of("GetBackColor", LuaControl::getBackColor),
        RegisteredFunction.of("SetBackColor", LuaControl::setBackColor),
        RegisteredFunction.of("GetBackColorBlendMode", LuaControl::getBackColorBlendMode),

        
        RegisteredFunction.of("GetBackground", LuaControl::getBackground),
        RegisteredFunction.of("SetBackground", LuaControl::setBackground),
        RegisteredFunction.of("GetOpacity", LuaControl::getOpacity),
        RegisteredFunction.of("SetOpacity", LuaControl::setOpacity),
        RegisteredFunction.of("GetStretchMode", LuaControl::getStretchMode),
        RegisteredFunction.of("SetMouseVisible", LuaControl::setMouseVisible),
        RegisteredFunction.of("IsMouseVisible", LuaControl::isMouseVisible),
        RegisteredFunction.ofV("GetMousePosition", LuaControl::getMousePosition),
        RegisteredFunction.of("GetAllowDrop", LuaControl::getAllowDrop),
        RegisteredFunction.of("GetWantsUpdates", LuaControl::getWantsUpdates),
        RegisteredFunction.of("SetWantsUpdates", LuaControl::setWantsUpdates),
        RegisteredFunction.of("GetWantsKeyEvents", LuaControl::getWantsKeyEvents),
        RegisteredFunction.of("SetWantsKeyEvents", LuaControl::setWantsKeyEvents),
        
    });
    
    uiEnv.rawset("Control", luaControlClass);
    
    return luaControlClass;
  }
  
  public static Component findComponentFromLuaObject(LuaState state, LuaValue value) throws LuaError { 
    return findComponentFromObject(LuaTools.objectSelf(state, value));
  }

  public static Component findComponentFromObject(Object objectSelf) {
    Component component=null;

    if (objectSelf instanceof Component) {
      component=(Component)objectSelf;
    /*} else if (objectSelf instanceof DefaultMutableTreeNode) {
      component=(Component)((DefaultMutableTreeNode)objectSelf).getUserObject();*/
    } else if (objectSelf instanceof WindowController) {
      component=(Component)((WindowController)objectSelf).getWindow();
    }

    return component;
  }

  public static JComponent findJComponentFromObject(Object objectSelf) {
    Component component=findComponentFromObject(objectSelf);
    JComponent jComponent = null;
    
    if (component instanceof JComponent) {
      jComponent = (JComponent)component;
    }
    
    return jComponent;
  }

  public static Container findContainerFromLuaObject(LuaState state, LuaValue self) throws LuaError { 
    Component component=findComponentFromObject(LuaTools.objectSelf(state, self));
    if (component instanceof Container) {
      return (Container)component;
    }
    return null;
  }
  
  public static Component findContentComponentFromLuaObject(LuaState state, LuaValue self) throws LuaError { 
    Component viewComponent=findComponentFromObject(LuaTools.objectSelf(state, self));
    if (viewComponent instanceof JScrollPane) {
      Component component = ((JScrollPane)viewComponent).getViewport().getView();
      if (component != null) viewComponent = component;
    } if (viewComponent instanceof RootPaneContainer) {
      Component component = ((RootPaneContainer)viewComponent).getContentPane();
      if (component != null) viewComponent = component;
    }
    return viewComponent;
  }

  public static JComponent findJComponentFromLuaObject(LuaState state, LuaValue self) throws LuaError { 
    return findJComponentFromObject(LuaTools.objectSelf(state, self));
  }

  public static LuaValue luaNewIndexMetaFunc(LuaState state, LuaValue self, LuaValue key, LuaValue method) throws LuaError {
    Component component = LuaControl.findComponentFromLuaObject(state, self);
    if (component != null) {
      switch(key.checkString()) {
        case "Update": {
          //JComponent jComponent = LuaControl.findJComponentFromLuaObject(state, self);
          break;
        }
        case "PositionChanged": {
          component.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent event) {
              try {
                OperationHelper.call(state, method, self, tableOf());
              } catch (LuaError | UnwindThrowable error) {
                LOGGER.error(error);
              }
            }
          });
          break;
        }
        case "SizeChanged": {
          component.addComponentListener(new ComponentAdapter() {
              public void componentResized(ComponentEvent event) {
                try {
                  OperationHelper.call(state, method, self, tableOf());
                } catch (LuaError | UnwindThrowable error) {
                  LOGGER.error(error);
                }
              }
          });
          break;
        }
        case "VisibleChanged": {
          component.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent event) {
              try {
                OperationHelper.call(state, method, self, tableOf());
              } catch (LuaError | UnwindThrowable error) {
                LOGGER.error(error);
              }
            }
          });
          break;
        }
        case "FocusGained": {
          component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent event) {
              try {
                OperationHelper.call(state, method, self, tableOf());
              } catch (LuaError | UnwindThrowable error) {
                LOGGER.error(error);
              }
            }
          });
          break;
        }
        case "FocusLost": {
          component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent event) {
              try {
                OperationHelper.call(state, method, self, tableOf());
              } catch (LuaError | UnwindThrowable error) {
                LOGGER.error(error);
              }
            }
          });
          break;
        }
  
        case "EnabledChanged":
    
        case "KeyDown":
        case "KeyUp":
          break;
        case "MouseDown": {
          LuaMouseListener mouseListener = LuaMouseListener.luaIndexMetaFunc(state, component, self);
          mouseListener._luaMouseDown = method;
          break;
        }
        case "MouseClick": {
          LuaMouseListener mouseListener = LuaMouseListener.luaIndexMetaFunc(state, component, self);
          mouseListener._luaMouseClick = method;
          break;
        }
        case "MouseDoubleClick":
          break;
  
        case "MouseUp": {
          LuaMouseListener mouseListener = LuaMouseListener.luaIndexMetaFunc(state, component, self);
          mouseListener._luaMouseUp = method;
          break;
        }
          
        case "MouseEnter": {
          LuaMouseListener mouseListener = LuaMouseListener.luaIndexMetaFunc(state, component, self);
          mouseListener._luaMouseEnter = method;
          break;
        }
        case "MouseLeave": {
          LuaMouseListener mouseListener = LuaMouseListener.luaIndexMetaFunc(state, component, self);
          mouseListener._luaMouseLeave = method;
          break;
        }
        case "MouseHover": {
          component.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent event) {
                try {
                  OperationHelper.call(state, method, self, tableOf());
                } catch (LuaError | UnwindThrowable error) {
                  LOGGER.error(error);
                }
            }
          });
          break;
        }
        case "MouseMove":
          break;
        case "MouseWheel": {
          component.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                try {
                  OperationHelper.call(state, method, self, tableOf());
                } catch (LuaError | UnwindThrowable e) {
                  LOGGER.error(e);
                }
            }
          });
          break;
        }
        case "DragEnter":
        case "DragLeave":
        case "DragDrop":
          break;
        // Button
        case "Click": {
          LuaButton.jButtonSelf(state, self).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                  PluginManager.getInstance().event("Click", new Object[]{Apartment.findApartment(state), method, self, tableOf()});
                } catch (LuaError e) {
                  LOGGER.error(e);
                }
            }
          });
          break;
        }
        
        // TreeView
        case "SelectedNodeChanged": {
          JTree jTree = LuaTreeView.jTreeSelf(state, self);
          jTree.addTreeSelectionListener(new LuaTreeSelectionListener(jTree, state, self.checkTable(), method));
          break;
        }
      }
    }
    self.checkTable().rawset(key, method);

    return Constants.NIL;
  }

  public static LuaValue luaIndexMetaFunc(LuaState state, LuaValue self, LuaValue key) {
    /*JComponent jComponent = LuaControl.findJComponentFromObject(objectSelf);
    ((LibFunction.TwoArg)jComponent.getClientProperty(LuaControl.jComponentKey_luaEventsFunc)).call(state1, self1, key);

    return valueOf(self1.typeName() + "[" + key.toString() + "]=xyz");*/
    //self.checkTable().rawset(key, value);
    return Constants.NIL;
  }

  public static LuaValue controlConstructor(LuaState state, LuaValue self) throws LuaError {
    JPanel jPanel = new JPanel();
    jPanel.setLayout(null);
    Turbine.ObjectInheritedConstructor(
        state, self, jPanel,
        LibFunction.create(LuaControl::luaNewIndexMetaFunc),
        LibFunction.create(LuaControl::luaIndexMetaFunc)
    );
    return Constants.NIL;
  }

  public static LuaValue controlInheritedConstructor(LuaState state,
                                                     LuaValue self,
                                                     Object objectSelf) throws LuaError {
    JComponent jComponent = findJComponentFromObject(objectSelf);
    if (jComponent != null) {
      jComponent.putClientProperty(jComponentKey_luaObjectSelf, self);
    } else if (objectSelf instanceof WindowController) {
      WindowController windowController=(WindowController)objectSelf;
      windowController.getContext().setValue(jComponentKey_luaObjectSelf, self);
    }
    
    Turbine.ObjectInheritedConstructor(
        state, self, objectSelf,
        LibFunction.create(LuaControl::luaNewIndexMetaFunc),
        LibFunction.create(LuaControl::luaIndexMetaFunc)
    );

    return Constants.NIL;
  }

  public static LuaValue getParent(LuaState state, LuaValue self) {
    //Container container = LuaTools.objectSelf(state, self, JComponent.class).getParent();
    //container.getClientProperty();
    return Constants.NIL; //Turbine.luaValueFromObject(container);
  }
  
  public static LuaValue setParent(LuaState state, LuaValue self, LuaValue parent) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);

    if ((component != null)
        && (!(component instanceof JScrollBar))) {
      Container container = LuaControl.findContainerFromLuaObject(state, parent);

      if (container != null) {
        if (container instanceof JScrollPane) {
          JScrollPane scrollPane=(JScrollPane)container;
          if (scrollPane.getViewport().getView() == null)
              scrollPane.setViewportView(component);
          else {
            Component containerFromView = scrollPane.getViewport().getView();
            if (containerFromView instanceof JComponent)
              ((JComponent)containerFromView).add(component);
          }
        } else container.add(component);
      }
    }
    return Constants.NIL;
  }

  public static LuaTable getControls(LuaState state, LuaValue self) {
    return tableOf();
  }

  public static LuaValue focus(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue setVisible(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);

    component.setVisible(value.checkBoolean());
    return Constants.NIL;
  }
  
  public static LuaBoolean isVisible(LuaState state, LuaValue self) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);

    return valueOf(component.isVisible());
  }
  
  public static LuaNumber getLeft(LuaState state, LuaValue self) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);

    return valueOf(component.getX());
  }
  
  public static LuaValue setLeft(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);
    
    component.setLocation(value.checkInteger(), component.getY());
    return Constants.NIL;
  }
  
  public static LuaNumber getTop(LuaState state, LuaValue self) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);

    return valueOf(component.getY());
  }
  
  public static LuaValue setTop(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);
    
    component.setLocation(component.getX(), value.checkInteger());
    return Constants.NIL;
  }
  
  public static Varargs getPosition(LuaState state, Varargs varargs) throws LuaError {
    Component component = findComponentFromLuaObject(state, varargs.first());
    
    return varargsOf(valueOf(component.getX()), valueOf(component.getY()));
  }
  
  public static LuaValue setPosition(LuaState state, LuaValue self, LuaValue left, LuaValue top) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);
    
    component.setLocation(left.checkInteger(), top.checkInteger());
    
    return Constants.NIL;
  }

  public static LuaNumber getWidth(LuaState state, LuaValue self) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);
    
    if (component instanceof Window) {
      return valueOf(component.getPreferredSize().getWidth());
    }
    return valueOf(component.getWidth());
  }

  public static LuaValue setWidth(LuaState state, LuaValue self, LuaValue width) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);
    
    if (component instanceof Window) {
      component.setPreferredSize(new Dimension(width.checkInteger(), component.getPreferredSize().width));
      ((Window)component).pack();
    } else {
      component.setSize(new Dimension(width.checkInteger(), component.getHeight()));
    }

    return Constants.NIL;
  }

  public static LuaNumber getHeight(LuaState state, LuaValue self) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);

    if (component instanceof Window) {
      return valueOf(component.getPreferredSize().getHeight());
    }
    return valueOf(component.getHeight());
  }
  
  public static LuaValue setHeight(LuaState state, LuaValue self, LuaValue height) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);

    if (component instanceof Window) {
      component.setPreferredSize(new Dimension(component.getPreferredSize().width, height.checkInteger()));
      ((Window)component).pack();
    } else {
      component.setSize(new Dimension(component.getWidth(), height.checkInteger()));
    }
    
    return Constants.NIL;
  }

  public static Varargs getSize(LuaState state, Varargs varargs) throws LuaError {
    Dimension dimension;
    Component component = findComponentFromLuaObject(state, varargs.first());
    if (component != null) {
      if (component instanceof Window) {
        dimension = component.getPreferredSize();
      } else {
        dimension = component.getSize();
      }
    } else dimension = new Dimension(100,100);
    return varargsOf(valueOf(dimension.width), valueOf(dimension.height));
  }

  public static LuaValue setSize(LuaState state, LuaValue self, LuaValue width, LuaValue height) throws LuaError {    
    Component component = findComponentFromLuaObject(state, self);
    if (component != null) {
      if (component instanceof Window) {
        component.setPreferredSize(new Dimension(width.checkInteger(), height.checkInteger()));
        ((Window)component).pack();
      } else {
        component.setSize(new Dimension(width.checkInteger(), height.checkInteger()));
      }
    }

    return Constants.NIL;
  }

  public static LuaNumber getZOrder(LuaState state, LuaValue self, LuaValue value) {
    return Constants.ZERO;
  }
  
  public static LuaValue setZOrder(LuaState state, LuaValue self) {
    return Constants.NIL;
  }

  public static LuaValue getBlendMode(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue setBlendMode(LuaState state, LuaValue self) {
    return Constants.NIL;
  }

  public static LuaValue setBackColor(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Component component = findContentComponentFromLuaObject(state, self);
    if (component != null) {
      component.setBackground(UI.luaColorToColor(value));
    }
    return Constants.NIL;
  }
  
  public static LuaValue getBackColor(LuaState state, LuaValue self) throws LuaError {
    Component component = findContentComponentFromLuaObject(state, self);
    LuaValue color=Constants.NIL;
    if (component != null) {
      color=UI.colorToLuaColor(component.getBackground());
    }
    return color;
  }
  
  public static LuaNumber getBackColorBlendMode(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaValue getBackground(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue setBackground(LuaState state, LuaValue self, LuaValue backgroundImage) {
    return Constants.NIL;
  }
  
  public static LuaNumber getOpacity(LuaState state, LuaValue self) throws LuaError {
    Component component = findContentComponentFromLuaObject(state, self);
    return component.isOpaque()?Constants.ONE:Constants.ZERO;
  }

  public static LuaValue setOpacity(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Component component = findContentComponentFromLuaObject(state, self);
    if (component instanceof JComponent) ((JComponent)component).setOpaque(value.checkDouble()>.0);
    return Constants.NIL;
  }
  
  public static LuaValue getStretchMode(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue setMouseVisible(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaBoolean isMouseVisible(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
  
  public static Varargs getMousePosition(LuaState state, Varargs varargs) throws LuaError {
    Component component = findComponentFromLuaObject(state, varargs.first());
    
    Point point = component.getMousePosition();
    return varargsOf(valueOf(point.x), valueOf(point.y));
  }

  public static LuaBoolean getAllowDrop(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
  
  public static LuaBoolean getWantsUpdates(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
  
  public static LuaValue setWantsUpdates(LuaState state, LuaValue self, LuaValue value) {
    return Constants.FALSE;
  }

  public static LuaBoolean getWantsKeyEvents(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
  
  public static LuaValue setWantsKeyEvents(LuaState state, LuaValue self, LuaValue value) {
    return Constants.FALSE;
  }
}
