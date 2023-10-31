package delta.games.lotro.lua.turbine.ui;

import java.awt.Color;
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
import javax.swing.SwingUtilities;

import delta.common.ui.swing.Window;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lua.turbine.Apartment;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.turbine.ui.mouse.LuaMouseListener;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeSelectionListener;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.value.LuaValue;

/**
 * LuaControl library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaControl {
  //private static Logger LOGGER = Logger.getLogger(LuaControl.class);
	public static LuaValue _newIndexMetaFunc;
	public static LuaValue _indexMetaFunc;

  public static String jComponentKey_luaObjectSelf = "jComponentKey_luaObjectSelf";
  public static Object jComponentKey_luaEventsFunc = new Object();

  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
		
  	/* Register luaNewIndexMetaFunc*/
  	lua.push((JFunction)LuaControl::newIndexMetaFunc);
  	_newIndexMetaFunc = lua.get();
  	
  	lua.push((JFunction)LuaControl::indexMetaFunc);
  	_indexMetaFunc = lua.get();
  	
  	error = LuaObject.callInherit(lua, -3, "Turbine", "Object");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", LuaControl::constructor);
  	LuaTools.setFunction(lua, -1, -3, "GetParent", LuaControl::getParent);
  	LuaTools.setFunction(lua, -1, -3, "SetParent", LuaControl::setParent);
  	LuaTools.setFunction(lua, -1, -3, "GetControls", LuaControl::getControls);
  	LuaTools.setFunction(lua, -1, -3, "Focus", LuaControl::focus);
  	LuaTools.setFunction(lua, -1, -3, "SetVisible", LuaControl::setVisible);
    LuaTools.setFunction(lua, -1, -3, "IsVisible", LuaControl::isVisible);
    LuaTools.setFunction(lua, -1, -3, "GetLeft", LuaControl::getLeft);
    LuaTools.setFunction(lua, -1, -3, "SetLeft", LuaControl::setLeft);
    LuaTools.setFunction(lua, -1, -3, "GetTop", LuaControl::getTop);
    LuaTools.setFunction(lua, -1, -3, "SetTop", LuaControl::setTop);
    LuaTools.setFunction(lua, -1, -3, "GetPosition", LuaControl::getPosition);
    LuaTools.setFunction(lua, -1, -3, "SetPosition", LuaControl::setPosition);
    LuaTools.setFunction(lua, -1, -3, "GetWidth", LuaControl::getWidth);
    LuaTools.setFunction(lua, -1, -3, "SetWidth", LuaControl::setWidth);
    LuaTools.setFunction(lua, -1, -3, "GetHeight", LuaControl::getHeight);
    LuaTools.setFunction(lua, -1, -3, "SetHeight", LuaControl::setHeight);
    LuaTools.setFunction(lua, -1, -3, "GetSize", LuaControl::getSize);
    LuaTools.setFunction(lua, -1, -3, "SetSize", LuaControl::setSize);
    LuaTools.setFunction(lua, -1, -3, "GetZOrder", LuaControl::getZOrder);
    LuaTools.setFunction(lua, -1, -3, "SetZOrder", LuaControl::setZOrder);

    LuaTools.setFunction(lua, -1, -3, "GetBlendMode", LuaControl::getBlendMode);
    LuaTools.setFunction(lua, -1, -3, "SetBlendMode", LuaControl::setBlendMode);
    LuaTools.setFunction(lua, -1, -3, "GetBackColor", LuaControl::getBackColor);
    LuaTools.setFunction(lua, -1, -3, "SetBackColor", LuaControl::setBackColor);
    LuaTools.setFunction(lua, -1, -3, "GetBackColorBlendMode", LuaControl::getBackColorBlendMode);

    LuaTools.setFunction(lua, -1, -3, "GetBackground", LuaControl::getBackground);
    LuaTools.setFunction(lua, -1, -3, "SetBackground", LuaControl::setBackground);
    LuaTools.setFunction(lua, -1, -3, "GetOpacity", LuaControl::getOpacity);
    LuaTools.setFunction(lua, -1, -3, "SetOpacity", LuaControl::setOpacity);
    LuaTools.setFunction(lua, -1, -3, "GetStretchMode", LuaControl::getStretchMode);
    LuaTools.setFunction(lua, -1, -3, "SetMouseVisible", LuaControl::setMouseVisible);
    LuaTools.setFunction(lua, -1, -3, "IsMouseVisible", LuaControl::isMouseVisible);
    LuaTools.setFunction(lua, -1, -3, "GetMousePosition", LuaControl::getMousePosition);
    LuaTools.setFunction(lua, -1, -3, "GetAllowDrop", LuaControl::getAllowDrop);
    LuaTools.setFunction(lua, -1, -3, "GetWantsUpdates", LuaControl::getWantsUpdates);
    LuaTools.setFunction(lua, -1, -3, "SetWantsUpdates", LuaControl::setWantsUpdates);
    LuaTools.setFunction(lua, -1, -3, "GetWantsKeyEvents", LuaControl::getWantsKeyEvents);
    LuaTools.setFunction(lua, -1, -3, "SetWantsKeyEvents", LuaControl::setWantsKeyEvents);

    lua.setField(-2, "Control");
    return error;
  }
  
  public static Component findComponentFromLuaObject(Lua lua, int index) { 
    return findComponentFromObject(LuaObject.objectSelf(lua, index));
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

  public static Container findContainerFromLuaObject(Lua lua, int index) { 
    Component component=findComponentFromObject(LuaObject.objectSelf(lua, index));
    if (component instanceof Container) {
      return (Container)component;
    }
    return null;
  }
  
  public static Component findContentComponentFromLuaObject(Lua lua, int index) { 
    Component viewComponent=findComponentFromObject(LuaObject.objectSelf(lua, index));
    if (viewComponent instanceof JScrollPane) {
      Component component = ((JScrollPane)viewComponent).getViewport().getView();
      if (component != null) viewComponent = component;
    } if (viewComponent instanceof RootPaneContainer) {
      Component component = ((RootPaneContainer)viewComponent).getContentPane();
      if (component != null) viewComponent = component;
    }
    return viewComponent;
  }

  public static JComponent findJComponentFromLuaObject(Lua lua, int index) { 
    return findJComponentFromObject(LuaObject.objectSelf(lua, index));
  }

  private static int newIndexMetaFunc(Lua lua) {
    String key = lua.toString(2);
    lua.pushValue(3);
    LuaValue method = lua.get();
    Component component = LuaControl.findComponentFromLuaObject(lua, 1);
    if (component != null) {
      switch(key) {
        case "Update": {
          break;
        }
        case "PositionChanged": {
          component.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent event) {
            	LuaTools.invokeEvent(
            			lua,
              		"PositionChanged", 
              		new Object[]{
              				Apartment.findApartment(lua),
              				method,
              				LuaObject.findLuaObjectFromObject(component)
              		}
              );
            }
          });
          break;
        }
        case "SizeChanged": {
          component.addComponentListener(new ComponentAdapter() {
              public void componentResized(ComponentEvent event) {
                LuaTools.invokeEvent(lua, "SizeChanged", new Object[]{Apartment.findApartment(lua), method, LuaObject.findLuaObjectFromObject(component)});
              }
          });
          break;
        }
        case "VisibleChanged": {
          component.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent event) {
              LuaTools.invokeEvent(lua, "VisibleChanged", new Object[]{Apartment.findApartment(lua), method, LuaObject.findLuaObjectFromObject(component)});
            }
          });
          break;
        }
        case "FocusGained": {
          component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent event) {
              LuaTools.invokeEvent(lua, "FocusGained", new Object[]{Apartment.findApartment(lua), method, LuaObject.findLuaObjectFromObject(component)});
            }
          });
          break;
        }
        case "FocusLost": {
          component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent event) {
              LuaTools.invokeEvent(lua, "FocusLost", new Object[]{Apartment.findApartment(lua), method, LuaObject.findLuaObjectFromObject(component)});
            }
          });
          break;
        }
  
        case "EnabledChanged":
        case "KeyDown":
        case "KeyUp":
          break;
        case "MouseDown": {
          LuaMouseListener mouseListener = LuaMouseListener.luaIndexMetaFunc(lua, component, LuaObject.findLuaObjectFromObject(component));
          mouseListener._luaMouseDown = method;
          break;
        }
        case "MouseClick": {
          LuaMouseListener mouseListener = LuaMouseListener.luaIndexMetaFunc(lua, component, LuaObject.findLuaObjectFromObject(component));
          mouseListener._luaMouseClick = method;
          break;
        }
        case "MouseDoubleClick":
          break;
  
        case "MouseUp": {
          LuaMouseListener mouseListener = LuaMouseListener.luaIndexMetaFunc(lua, component, LuaObject.findLuaObjectFromObject(component));
          mouseListener._luaMouseUp = method;
          break;
        }
          
        case "MouseEnter": {
          LuaMouseListener mouseListener = LuaMouseListener.luaIndexMetaFunc(lua, component, LuaObject.findLuaObjectFromObject(component));
          mouseListener._luaMouseEnter = method;
          break;
        }
        case "MouseLeave": {
          LuaMouseListener mouseListener = LuaMouseListener.luaIndexMetaFunc(lua, component, LuaObject.findLuaObjectFromObject(component));
          mouseListener._luaMouseLeave = method;
          break;
        }
        case "MouseHover": {
          component.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent event) {
              LuaTools.invokeEvent(lua, "MouseHover", new Object[]{Apartment.findApartment(lua), method, LuaObject.findLuaObjectFromObject(component)});
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
              LuaTools.invokeEvent(lua, "MouseWheel", new Object[]{Apartment.findApartment(lua), method, LuaObject.findLuaObjectFromObject(component)});
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
          LuaButton.jButtonSelf(lua, 1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
              LuaTools.invokeEvent(lua, "Click", new Object[]{Apartment.findApartment(lua), method, LuaObject.findLuaObjectFromObject(component)});
            }
          });
          break;
        }
        
        // TreeView
        case "SelectedNodeChanged": {
          JTree jTree = LuaTreeView.jTreeSelf(lua, 1);
          jTree.addTreeSelectionListener(new LuaTreeSelectionListener(jTree, LuaObject.findLuaObjectFromObject(component), method));
          break;
        }
      }
    }

    lua.rawSet(1); /* Self[key]=value */
    return 1;
  }

  public static int indexMetaFunc(Lua lua) {
    /*LuaValue self = lua.get();
    LuaValue key = lua.get();
    JComponent jComponent = LuaControl.findJComponentFromObject(objectSelf);
    ((LibFunction.TwoArg)jComponent.getClientProperty(LuaControl.jComponentKey_luaEventsFunc)).call(lua1, self1, key);

    lua.push(self1.typeName() + "[" + key.toString() + "]=xyz");*/
    //self.checkTable().set(key, value);
    return 0;
  }

  private static int constructor(Lua lua) {
    JPanel jPanel = new JPanel();
    jPanel.setLayout(null);
    LuaObject.ObjectInheritedConstructor(
        lua, 1, jPanel, _newIndexMetaFunc, null
    );
    return 1;
  }

  public static int controlInheritedConstructor(Lua lua,
                                                int indexSelf,
                                                Object objectSelf) {
    JComponent jComponent = findJComponentFromObject(objectSelf);
    if (jComponent != null) {
    	lua.pushValue(indexSelf);
      jComponent.putClientProperty(jComponentKey_luaObjectSelf, lua.get());
    } else if (objectSelf instanceof WindowController) {
      WindowController windowController=(WindowController)objectSelf;
      lua.pushValue(indexSelf);
      windowController.getContext().setValue(jComponentKey_luaObjectSelf, lua.get());
    }
    
    return LuaObject.ObjectInheritedConstructor(
        lua, indexSelf, objectSelf, _newIndexMetaFunc, null
    );
  }

  private static int getParent(Lua lua) {
    //Container container = LuaObject.objectSelf(lua, self, JComponent.class).getParent();
    //container.getClientProperty();
    return 1; //Turbine.luaValueFromObject(container);
  }
  
  private static int setParent(Lua lua) {
    Component component = findComponentFromLuaObject(lua, 1);

    if ((component != null)
        && (!(component instanceof JScrollBar))) {
      Container container = LuaControl.findContainerFromLuaObject(lua, 2);

      if (container != null) {
        if (container instanceof JScrollPane) {
          JScrollPane scrollPane=(JScrollPane)container;
          if (scrollPane.getViewport().getView() == null)
          	SwingUtilities.invokeLater(() -> scrollPane.setViewportView(component));
          else {
            Component containerFromView = scrollPane.getViewport().getView();
            if (containerFromView instanceof JComponent)
            	SwingUtilities.invokeLater(() -> ((JComponent)containerFromView).add(component));
          }
        } else SwingUtilities.invokeLater(() -> container.add(component));
      }
    }
    return 1;
  }

  private static int getControls(Lua lua) {
    return 1;
  }

  private static int focus(Lua lua) {
    return 1;
  }
  
  private static int setVisible(Lua lua) {
    Component component = findComponentFromLuaObject(lua, 1);
    boolean visible = lua.toBoolean(2);

    SwingUtilities.invokeLater(() -> component.setVisible(visible));
    return 1;
  }
  
  private static int isVisible(Lua lua) {
    Component component = findComponentFromLuaObject(lua, 1);

    LuaTools.invokeAndWait(lua, () -> lua.push(component.isVisible()));
    return 1;
  }
  
  private static int getLeft(Lua lua) {
    Component component = findComponentFromLuaObject(lua, 1);

    LuaTools.invokeAndWait(lua, () -> lua.push(component.getX()));
    return 1;
  }
  
  private static int setLeft(Lua lua) {
    Component component = findComponentFromLuaObject(lua, 1);
    int x = (int)lua.toNumber(2);

    SwingUtilities.invokeLater(() -> component.setLocation(x, component.getY()));
    return 1;
  }
  
  private static int getTop(Lua lua) {
    Component component = findComponentFromLuaObject(lua, 1);

    LuaTools.invokeAndWait(lua, () -> lua.push(component.getY()));
    return 1;
  }
  
  private static int setTop(Lua lua) {
    Component component = findComponentFromLuaObject(lua, 1);
		int y = (int)lua.toNumber(2);

    SwingUtilities.invokeLater(() -> component.setLocation(component.getX(), y));
    return 1;
  }
  
  private static int getPosition(Lua lua) {
    Component component = findComponentFromLuaObject(lua, 1);
    
    LuaTools.invokeAndWait(lua, () -> {
    	lua.push(component.getX());
    	lua.push(component.getY());
    });
    return 1;
  }
  
  private static int setPosition(Lua lua) {
    Component component = findComponentFromLuaObject(lua, 1);
    int x = (int)lua.toNumber(2);
    int y = (int)lua.toNumber(3);
    
    SwingUtilities.invokeLater(() -> component.setLocation(x, y));
    return 1;
  }

  private static int getWidth(Lua lua) {
    Component component = findComponentFromLuaObject(lua, 1);
    
    if (component instanceof Window) {
    	LuaTools.invokeAndWait(lua, () ->lua.push(Double.valueOf(component.getPreferredSize().getWidth())));
    } else {
    	LuaTools.invokeAndWait(lua, () -> lua.push(component.getWidth()));
    }
    return 1;
  }

  private static int setWidth(Lua lua) {
    Component component = findComponentFromLuaObject(lua, 1);
    int width = (int)lua.toNumber(2);

    if (component instanceof Window) {
    	SwingUtilities.invokeLater(() -> {
    		component.setPreferredSize(new Dimension(width, component.getPreferredSize().width));
    		((Window)component).pack();
    	});
    } else {
    	SwingUtilities.invokeLater(() -> component.setSize(new Dimension(width, component.getHeight())));
    }
    return 1;
  }

  private static int getHeight(Lua lua) {
    Component component = findComponentFromLuaObject(lua, 1);

    if (component instanceof Window) {
    	LuaTools.invokeAndWait(lua, () -> lua.push(Double.valueOf(component.getPreferredSize().getHeight())));
    } else {
    	LuaTools.invokeAndWait(lua, () -> lua.push(component.getHeight()));
    }
    return 1;
  }
  
  private static int setHeight(Lua lua) {
    Component component = findComponentFromLuaObject(lua, 1);
    int height = (int)lua.toNumber(2);

    if (component instanceof Window) {
    	SwingUtilities.invokeLater(() -> {
    		component.setPreferredSize(new Dimension(component.getPreferredSize().width, height));
    		((Window)component).pack();
    	});
    } else {
    	SwingUtilities.invokeLater(() -> component.setSize(new Dimension(component.getWidth(), height)));
    }
    return 1;
  }

  private static int getSize(Lua lua) {
    Dimension dimension;
    Component component = findComponentFromLuaObject(lua, 1);
    if (component != null) {
      if (component instanceof Window) {
        dimension = component.getPreferredSize();
      } else {
        dimension = component.getSize();
      }
    } else dimension = new Dimension(100,100);
    
    LuaTools.invokeAndWait(lua, () -> {
    	lua.push(dimension.width);
    	lua.push(dimension.height);
    });
    return 1;
  }

  private static int setSize(Lua lua) {
    Component component = findComponentFromLuaObject(lua, 1);
    if (component != null) {
    	Dimension dimension = new Dimension((int)lua.toNumber(2), (int)lua.toNumber(3));

      if (component instanceof Window) {
      	SwingUtilities.invokeLater(() -> {
          component.setPreferredSize(dimension);
          ((Window)component).pack();
      	});
      } else {
      	SwingUtilities.invokeLater(() -> component.setSize(dimension));
      }
    }
    return 1;
  }

  private static int getZOrder(Lua lua) {
    return 1;
  }
  
  private static int setZOrder(Lua lua) {
    return 1;
  }

  private static int getBlendMode(Lua lua) {
    return 1;
  }
  
  private static int setBlendMode(Lua lua) {
    return 1;
  }

  private static int getBackColor(Lua lua) {
    Component component = findContentComponentFromLuaObject(lua, 1);
    if (component != null) {
    	LuaTools.invokeAndWait(lua, () -> lua.push(UI.colorToLuaColor(component.getBackground()), Lua.Conversion.FULL));
    }
    return 1;
  }

  private static int setBackColor(Lua lua) {
    Component component = findContentComponentFromLuaObject(lua, 1);
    if (component != null) {
    	Color color = UI.luaColorToColor(lua, 1);
    	SwingUtilities.invokeLater(() -> component.setBackground(color));
    }
    return 1;
  }
  
  private static int getBackColorBlendMode(Lua lua) {
    return 1;
  }
  
  private static int getBackground(Lua lua) {
    return 1;
  }
  
  private static int setBackground(Lua lua) {
    return 1;
  }
  
  private static int getOpacity(Lua lua) {
    Component component = findContentComponentFromLuaObject(lua, 1);
    LuaTools.invokeAndWait(lua, () -> lua.push(component.isOpaque()));
    return 1;
  }

  private static int setOpacity(Lua lua) {
    /*Component component = findContentComponentFromLuaObject(lua, 1);
    if (component instanceof JComponent) {
    	double opacity = lua.toNumber(2);
    	SwingUtilities.invokeLater(() -> ((JComponent)component).setOpaque(opacity >.0));
    }*/
    return 1;
  }
  
  private static int getStretchMode(Lua lua) {
    return 1;
  }
  
  private static int setMouseVisible(Lua lua) {
    return 1;
  }
  
  private static int isMouseVisible(Lua lua) {
    return 1;
  }
  
  private static int getMousePosition(Lua lua) {
    Component component = findComponentFromLuaObject(lua, 1);
    Point point = component.getMousePosition();
    
    LuaTools.invokeAndWait(lua, () -> {
      lua.push(point.x);
      lua.push(point.y);
    });
    return 1;
  }

  private static int getAllowDrop(Lua lua) {
    return 1;
  }
  
  private static int getWantsUpdates(Lua lua) {
    return 1;
  }
  
  private static int setWantsUpdates(Lua lua) {
    return 1;
  }

  private static int getWantsKeyEvents(Lua lua) {
    return 1;
  }
  
  private static int setWantsKeyEvents(Lua lua) {
    return 1;
  }
}
