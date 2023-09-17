package delta.games.lotro.lua.turbine.ui;

import static org.squiddev.cobalt.ValueFactory.tableOf;
import static org.squiddev.cobalt.ValueFactory.valueOf;
import static org.squiddev.cobalt.ValueFactory.varargsOf;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

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

import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.turbine.ui.mouse.LuaMouseListener;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeSelectionListener;

/**
 * LuaControl library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaControl {

  public static LuaTable add(LuaState state,
                             LuaTable uiMetatable,
                             LuaFunction luaClass,
                             LuaValue luaObjectClass) throws LuaError, UnwindThrowable {

    LuaTable luaControlClass = luaClass.call(state, luaObjectClass).checkTable();
    RegisteredFunction.bind(luaControlClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaControl::ControlConstructor),
        RegisteredFunction.of("GetParent", LuaControl::GetParent),
        RegisteredFunction.of("SetParent", LuaControl::SetParent),
        RegisteredFunction.of("GetControls", LuaControl::GetControls),
        RegisteredFunction.of("Focus", LuaControl::Focus),
        RegisteredFunction.of("SetVisible", LuaControl::SetVisible),
        RegisteredFunction.of("IsVisible", LuaControl::IsVisible),
        RegisteredFunction.of("GetLeft", LuaControl::GetLeft),
        RegisteredFunction.of("SetLeft", LuaControl::SetLeft),
        RegisteredFunction.of("GetTop", LuaControl::GetTop),
        RegisteredFunction.of("SetTop", LuaControl::SetTop),
        RegisteredFunction.ofV("GetPosition", LuaControl::GetPosition),
        RegisteredFunction.of("SetPosition", LuaControl::SetPosition),
        RegisteredFunction.of("GetWidth", LuaControl::GetWidth),
        RegisteredFunction.of("SetWidth", LuaControl::SetWidth),
        RegisteredFunction.of("GetHeight", LuaControl::GetHeight),
        RegisteredFunction.of("SetHeight", LuaControl::SetHeight),
        RegisteredFunction.of("SetSize", LuaControl::SetSize),
        RegisteredFunction.ofV("GetSize", LuaControl::GetSize),
        
        RegisteredFunction.of("GetBlendMode", LuaControl::GetBlendMode),
        RegisteredFunction.of("SetBlendMode", LuaControl::SetBlendMode),

        RegisteredFunction.of("SetBackColor", LuaControl::SetBackColor),
        RegisteredFunction.of("GetBackColor", LuaControl::GetBackColor),        
        RegisteredFunction.of("GetBackColorBlendMode", LuaControl::GetBackColorBlendMode),

        
        RegisteredFunction.of("GetBackground", LuaControl::GetBackground),
        RegisteredFunction.of("SetBackground", LuaControl::SetBackground),
        RegisteredFunction.of("GetOpacity", LuaControl::GetOpacity),
        RegisteredFunction.of("GetStretchMode", LuaControl::GetStretchMode),
        RegisteredFunction.of("SetMouseVisible", LuaControl::SetMouseVisible),
        RegisteredFunction.of("IsMouseVisible", LuaControl::IsMouseVisible),
        RegisteredFunction.ofV("GetMousePosition", LuaControl::GetMousePosition),
        RegisteredFunction.of("GetAllowDrop", LuaControl::GetAllowDrop),
        RegisteredFunction.of("GetWantsUpdates", LuaControl::GetWantsUpdates),
        RegisteredFunction.of("SetWantsUpdates", LuaControl::SetWantsUpdates)
    });
    
    uiMetatable.rawset("Control", luaControlClass);
    
    return luaControlClass;
  }
  
  public static Object jComponentKey_luaObjectSelf = new Object();
  public static Object jComponentKey_luaEventsFunc = new Object();
  
  public static LuaValue luaObjectFromJComponent(JComponent jComponent) {
    return (LuaValue)jComponent.getClientProperty(jComponentKey_luaObjectSelf);
  }

  public static LuaValue findluaObjectFromObject(Object object) {
    return luaObjectFromJComponent(findJComponentFromObject(object));
  }
  
  public static Component findComponentFromObject(Object object) {
    Component component = null;

    if (Component.class.isAssignableFrom(object.getClass())) {
      component = (Component)object;
    }/* else if (DefaultMutableTreeNode.class.isAssignableFrom(object.getClass())) {
      component = (Component)((DefaultMutableTreeNode)object).getUserObject();
    }*/

    return component;
  }
  
  public static Component findComponentFromLuaObject(LuaState state, LuaValue value) throws LuaError { 
    return findComponentFromObject(Turbine.objectSelf(state, value));
  }

  public static JComponent findJComponentFromObject(Object object) {
    JComponent jComponent = null;
    
    if (JComponent.class.isAssignableFrom(object.getClass())) {
      jComponent = (JComponent)object;
    } else if (DefaultMutableTreeNode.class.isAssignableFrom(object.getClass())) {
      jComponent = (JComponent)((DefaultMutableTreeNode)object).getUserObject();
    }/* else if (JInternalFrame.class.isAssignableFrom(object.getClass())) {
      jComponent = (JComponent)((JInternalFrame)object).getContentPane();
    }*/
    
    return jComponent;
  }
  
  public static JComponent findJComponentFromLuaObject(LuaState state, LuaValue value) throws LuaError { 
    return findJComponentFromObject(Turbine.objectSelf(state, value));
  }

  public static LuaValue luaNewIndexMetaFunc(LuaState state, LuaValue self, LuaValue key, LuaValue value) throws LuaError {
    
    switch(key.checkString()) {
      case "Update": {
        //JComponent jComponent = LuaControl.findJComponentFromLuaObject(state, self);
        break;
      }
      case "PositionChanged": {
        LuaControl.findJComponentFromLuaObject(state, self).addComponentListener(new ComponentAdapter() {
          public void componentMoved(ComponentEvent event) {
            try {
              OperationHelper.call(state, value, self, tableOf());
            } catch (LuaError | UnwindThrowable e) {
              e.printStackTrace();
            }
          }
        });
        break;
      }
      case "SizeChanged": {
        LuaControl.findJComponentFromLuaObject(state, self).addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent event) {
              try {
                OperationHelper.call(state, value, self, tableOf());
              } catch (LuaError | UnwindThrowable e) {
                e.printStackTrace();
              }
            }
        });
        break;
      }
      case "VisibleChanged": {
        LuaControl.findJComponentFromLuaObject(state, self).addComponentListener(new ComponentAdapter() {
          public void componentShown(ComponentEvent event) {
            try {
              OperationHelper.call(state, value, self, tableOf());
            } catch (LuaError | UnwindThrowable e) {
              e.printStackTrace();
            }
          }
        });
        break;
      }
      case "FocusGained":
      case "FocusLost":
      case "EnabledChanged":
  
      case "KeyDown":
      case "KeyUp":

      case "MouseDown": {
        LuaMouseListener mouseListener = LuaMouseListener.luaIndexMetaFunc(state, self);
        mouseListener.luaMouseDown = value;
        break;
      }
      case "MouseClick": {
        LuaMouseListener mouseListener = LuaMouseListener.luaIndexMetaFunc(state, self);
        mouseListener.luaMouseClick = value;
        break;
      }
      case "MouseDoubleClick":
        break;

      case "MouseUp": {
        LuaMouseListener mouseListener = LuaMouseListener.luaIndexMetaFunc(state, self);
        mouseListener.luaMouseUp = value;
        break;
      }
        
      case "MouseEnter": {
        LuaMouseListener mouseListener = LuaMouseListener.luaIndexMetaFunc(state, self);
        mouseListener.luaMouseEnter = value;
        break;
      }
      case "MouseLeave": {
        LuaMouseListener mouseListener = LuaMouseListener.luaIndexMetaFunc(state, self);
        mouseListener.luaMouseLeave = value;
        break;
      }
      case "MouseHover":{
        JComponent jComponent = LuaControl.findJComponentFromLuaObject(state, self);
        ((JButton)jComponent).addMouseMotionListener(new MouseMotionListener() {
          @Override
          public void mouseDragged(MouseEvent e) {}
          
          @Override
          public void mouseMoved(MouseEvent event) {
              try {
                OperationHelper.call(state, value, self, tableOf());
              } catch (LuaError | UnwindThrowable e) {
                e.printStackTrace();
              }
          }
        });
        break;
      }
      case "MouseMove":
        break;
      case "MouseWheel": {
        JComponent jComponent = LuaControl.findJComponentFromLuaObject(state, self);
        ((JButton)jComponent).addMouseWheelListener(new MouseWheelListener() {
          @Override
          public void mouseWheelMoved(MouseWheelEvent event) {
              try {
                OperationHelper.call(state, value, self, tableOf());
              } catch (LuaError | UnwindThrowable e) {
                e.printStackTrace();
              }
          }
        });
        break;
      }
      case "DragEnter":
      case "DragLeave":
      case "DragDrop":
        break;
      case "Click": {
        JComponent jComponent = LuaControl.findJComponentFromLuaObject(state, self);
        ((JButton)jComponent).addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent event) {
              try {
                OperationHelper.call(state, value, self, tableOf());
              } catch (LuaError | UnwindThrowable e) {
                e.printStackTrace();
              }
          }
        });
        break;
      }
      
      // TreeView
      case "SelectedNodeChanged": {
        JTree jTree = LuaTreeView.jTreeSelf(state, self);
        jTree.addTreeSelectionListener(new LuaTreeSelectionListener(jTree, state, self.checkTable(), value));
        break;
      }
    }
    self.checkTable().rawset(key, value);

    return Constants.NIL;
  }

  public static LuaValue luaIndexMetaFunc(LuaState state, LuaValue self, LuaValue key) {
    /*JComponent jComponent = LuaControl.findJComponentFromObject(objectSelf);
    ((LibFunction.TwoArg)jComponent.getClientProperty(LuaControl.jComponentKey_luaEventsFunc)).call(state1, self1, key);

    return valueOf(self1.typeName() + "[" + key.toString() + "]=xyz");*/
    //self.checkTable().rawset(key, value);
    return Constants.NIL;
  }
  


  public static LuaValue ControlConstructor(LuaState state, LuaValue self) throws LuaError {
    JPanel jPanel = new JPanel();
    jPanel.setLayout(null);
    Turbine.ObjectInheritedConstructor(
        state, self, jPanel,
        LibFunction.create(LuaControl::luaNewIndexMetaFunc),
        LibFunction.create(LuaControl::luaIndexMetaFunc)
    );
    return Constants.NIL;
  }

  public static LuaValue ControlInheritedConstructor(LuaState state,
                                                     LuaValue self,
                                                     Object objectSelf) throws LuaError {
    JComponent jComponent = findJComponentFromObject(objectSelf);
    jComponent.putClientProperty(jComponentKey_luaObjectSelf, self);
    
    Turbine.ObjectInheritedConstructor(
        state, self, objectSelf,
        LibFunction.create(LuaControl::luaNewIndexMetaFunc),
        LibFunction.create(LuaControl::luaIndexMetaFunc)
    );

    return Constants.NIL;
  }

  public static LuaValue GetParent(LuaState state, LuaValue self) {
    //Container container = Turbine.objectSelf(state, self, JComponent.class).getParent();
    //container.getClientProperty();
    return Constants.NIL; //Turbine.luaValueFromObject(container);
  }
  
  public static LuaValue SetParent(LuaState state, LuaValue self, LuaValue parent) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);

    if ((component != null)
        && (!(component instanceof JScrollBar))) {
      JComponent container = LuaControl.findJComponentFromLuaObject(state, parent);

      if (container != null) {
        if (JScrollPane.class.isAssignableFrom(container.getClass())) {
          if (((JScrollPane)container).getViewport().getView() == null)
            ((JScrollPane)container).setViewportView(component);
          else {
            Component containerFromView = ((JScrollPane)container).getViewport().getView();
            if (JComponent.class.isAssignableFrom(containerFromView.getClass()))
              ((JComponent)containerFromView).add(component);
          }
        } else container.add(component);
      }
    }
    return Constants.NIL;
  }

  public static LuaTable GetControls(LuaState state, LuaValue self) {
    return tableOf();
  }

  public static LuaValue Focus(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue SetVisible(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);

    component.setVisible(value.checkBoolean());
    return Constants.NIL;
  }
  
  public static LuaBoolean IsVisible(LuaState state, LuaValue self) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);

    return valueOf(component.isVisible());
  }
  
  public static LuaNumber GetLeft(LuaState state, LuaValue self) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);

    return valueOf(component.getX());
  }
  
  public static LuaValue SetLeft(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);
    
    component.setLocation(value.checkInteger(), component.getY());
    return Constants.NIL;
  }
  
  public static LuaNumber GetTop(LuaState state, LuaValue self) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);

    return valueOf(component.getY());
  }
  
  public static LuaValue SetTop(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);
    
    component.setLocation(component.getX(), value.checkInteger());
    return Constants.NIL;
  }
  
  public static Varargs GetPosition(LuaState state, Varargs varargs) throws LuaError {
    Component component = findComponentFromLuaObject(state, varargs.first());
    
    return varargsOf(valueOf(component.getX()), valueOf(component.getY()));
  }
  
  public static LuaValue SetPosition(LuaState state, LuaValue self, LuaValue left, LuaValue top) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);
    
    component.setLocation(left.checkInteger(), top.checkInteger());
    
    return Constants.NIL;
  }

  public static LuaNumber GetWidth(LuaState state, LuaValue self) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);
    
    if (JInternalFrame.class.isAssignableFrom(component.getClass())) {
      return valueOf(component.getPreferredSize().getWidth());
    }
    return valueOf(component.getWidth());
  }

  public static LuaValue SetWidth(LuaState state, LuaValue self, LuaValue width) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);
    
    if (JInternalFrame.class.isAssignableFrom(component.getClass())) {
      component.setPreferredSize(new Dimension(width.checkInteger(), component.getPreferredSize().width));
      ((JInternalFrame)component).pack();
    } else {
      component.setSize(new Dimension(width.checkInteger(), component.getHeight()));
    }

    return Constants.NIL;
  }

  public static LuaNumber GetHeight(LuaState state, LuaValue self) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);

    if (JInternalFrame.class.isAssignableFrom(component.getClass())) {
      return valueOf(component.getPreferredSize().getHeight());
    }
    return valueOf(component.getHeight());
  }
  
  public static LuaValue SetHeight(LuaState state, LuaValue self, LuaValue height) throws LuaError {
    Component component = findComponentFromLuaObject(state, self);

    if (JInternalFrame.class.isAssignableFrom(component.getClass())) {
      component.setPreferredSize(new Dimension(component.getPreferredSize().width, height.checkInteger()));
      ((JInternalFrame)component).pack();
    } else {
      component.setSize(new Dimension(component.getWidth(), height.checkInteger()));
    }
    
    return Constants.NIL;
  }

  public static Varargs GetSize(LuaState state, Varargs varargs) throws LuaError {
    Dimension dimension;
    Component component = findComponentFromLuaObject(state, varargs.first());
    if (component != null) {
      if (JInternalFrame.class.isAssignableFrom(component.getClass())) {
        dimension = component.getPreferredSize();
      } else {
        dimension = component.getSize();
      }
    } else dimension = new Dimension(100,100);
    return varargsOf(valueOf(dimension.width), valueOf(dimension.height));
  }

  public static LuaValue SetSize(LuaState state, LuaValue self, LuaValue width, LuaValue height) throws LuaError {    
    Component component = findComponentFromLuaObject(state, self);
    if (component != null) {
      if (JInternalFrame.class.isAssignableFrom(component.getClass())) {
        component.setPreferredSize(new Dimension(width.checkInteger(), height.checkInteger()));
        ((JInternalFrame)component).pack();
      } else {
        component.setSize(new Dimension(width.checkInteger(), height.checkInteger()));
      }
    }

    return Constants.NIL;
  }
  
  public static LuaValue GetBlendMode(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue SetBlendMode(LuaState state, LuaValue self) {
    return Constants.NIL;
  }

  public static LuaValue SetBackColor(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    JComponent jComponent = findJComponentFromLuaObject(state, self);
    jComponent.setBackground(UI.luaColorToColor(value));
    return Constants.NIL;
  }
  
  public static LuaValue GetBackColor(LuaState state, LuaValue self) throws LuaError {
    JComponent jComponent = findJComponentFromLuaObject(state, self);

    return UI.colorToLuaColor(jComponent.getBackground());
  }
  
  public static LuaNumber GetBackColorBlendMode(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaValue GetBackground(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue SetBackground(LuaState state, LuaValue self, LuaValue backgroundImage) {
    return Constants.NIL;
  }
  
  public static LuaNumber GetOpacity(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }

  public static LuaValue GetStretchMode(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue SetMouseVisible(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaBoolean IsMouseVisible(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
  
  public static Varargs GetMousePosition(LuaState state, Varargs varargs) throws LuaError {
    Component component = findComponentFromLuaObject(state, varargs.first());
    
    Point point = component.getMousePosition();
    return varargsOf(valueOf(point.x), valueOf(point.y));
  }

  public static LuaBoolean GetAllowDrop(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
  
  public static LuaBoolean GetWantsUpdates(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
  
  public static LuaBoolean SetWantsUpdates(LuaState state, LuaValue self, LuaValue value) {
    return Constants.FALSE;
  }

}
