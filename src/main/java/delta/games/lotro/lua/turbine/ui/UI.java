package delta.games.lotro.lua.turbine.ui;

import static org.squiddev.cobalt.ValueFactory.listOf;
import static org.squiddev.cobalt.ValueFactory.tableOf;
import static org.squiddev.cobalt.ValueFactory.valueOf;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.Varargs;
import org.squiddev.cobalt.function.RegisteredFunction;

import com.eleet.dragonconsole.DragonConsole;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeNode;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeNodeList;

/**
 * UI library for lua scripts.
 * @author MaxThlon
 */
public abstract class UI {
  public static JFrame frame = null;
  public static JDesktopPane jDesktopPane = null;
  public static DragonConsole dragonConsole = null;


  public static void buildUI() {
    /*try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Throwable e) {
        e.printStackTrace();
    }*/
    //FlatLotroLaf.setup();
    GuiFactory.init();
    frame=new JFrame("Lotro companion lua");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(1024, 768));
    
    
    jDesktopPane = new JDesktopPane();
    frame.setContentPane(jDesktopPane);
    frame.pack();

    JInternalFrame jInternalFrame = new JInternalFrame("General", true);
    jInternalFrame.setLocation(0, jDesktopPane.getHeight() - 200);
    jInternalFrame.setPreferredSize(new Dimension(600, 200));
    jInternalFrame.setOpaque(false);
    jInternalFrame.pack();
    jInternalFrame.setVisible(true);
    dragonConsole = new DragonConsole(false, false);
    jInternalFrame.add(dragonConsole);
    jDesktopPane.add(jInternalFrame);

    
    frame.setVisible(true);
  }

  public static void add(LuaState state, LuaTable uiEnv) throws LuaError, UnwindThrowable {
    LuaTable globals = state.getMainThread().getfenv();

    LuaTable luaColorClass = Turbine._luaClass.call(state, Turbine._luaObjectClass).checkTable();
    RegisteredFunction.bind(luaColorClass, new RegisteredFunction[]{
        RegisteredFunction.ofV("Constructor", UI::ColorConstructor)
    });
    uiEnv.rawset("Color", luaColorClass);

    uiEnv.rawset("BlendMode", tableOf(
        valueOf("Color"), valueOf("Color"),
        valueOf("Normal"), valueOf("Normal"),
        valueOf("Multiply"), valueOf("Multiply"),
        valueOf("AlphaBlend"), valueOf("AlphaBlend"),
        valueOf("Overlay"), valueOf("Overlay"),
        valueOf("Grayscale"), valueOf("Grayscale"),
        valueOf("Screen"), valueOf("Screen"),
        valueOf("None"), valueOf("None"),
        valueOf("Undefined"), valueOf("Undefined")
    ));
    
    uiEnv.rawset("ContentAlignment", tableOf(
        valueOf("TopLeft"), valueOf(1),
        valueOf("TopCenter"), valueOf(2),
        valueOf("TopRight"), valueOf(3),
        valueOf("MiddleLeft"), valueOf(4),
        valueOf("MiddleCenter"), valueOf(5),
        valueOf("MiddleRight"), valueOf(6),
        valueOf("BottomLeft"), valueOf(7),
        valueOf("BottomCenter"), valueOf(8),
        valueOf("BottomRight"), valueOf(9),
        valueOf("Undefined"), valueOf(0)
    ));
    
    
    uiEnv.rawset("FontStyle", tableOf(
        valueOf("None"), valueOf("None"),
        valueOf("Outline"), valueOf("Outline")
    ));
    
    uiEnv.rawset("MouseButton", tableOf(
        valueOf("Left"), valueOf(1),
        valueOf("Middle"), valueOf(4),
        valueOf("Right"), valueOf(2)
    ));
    
    uiEnv.rawset("Orientation" , tableOf(
        valueOf("Horizontal"), valueOf(0),
        valueOf("Vertical"), valueOf(1)
    ));
    
    uiEnv.rawset("HorizontalLayout", tableOf(
        valueOf("LeftToRight"), valueOf(0),
        valueOf("RightToLeft"), valueOf(1)
    ));

    uiEnv.rawset("VerticalLayout", tableOf(
        valueOf("BottomToTop"), valueOf(1),
        valueOf("TopToBottom"), valueOf(0)
    ));

    LuaTable luaControlClass = LuaControl.add(state, uiEnv, Turbine._luaClass, Turbine._luaObjectClass);

    LuaDisplay.add(state, uiEnv);
    LuaTable luaScrollableControlClass = LuaScrollableControl.add(state, uiEnv, luaControlClass);
    LuaTable luaLabelClass = LuaLabel.add(state, uiEnv, Turbine._luaClass, luaScrollableControlClass);
    LuaTextBox.add(state, uiEnv, luaLabelClass);
    LuaButton.add(state, uiEnv, luaLabelClass);
    LuaWindow.add(state, uiEnv, luaControlClass);
    LuaListBox.add(state, uiEnv, luaScrollableControlClass);
    LuaScrollBar.add(state, uiEnv, luaControlClass);
    LuaTreeNode.add(state, uiEnv, luaControlClass);
    LuaTreeNodeList.add(state, uiEnv);
    LuaTreeView.add(state, uiEnv, luaScrollableControlClass);
  }
  
  public static Color luaColorToColor(LuaValue self) throws LuaError {
    LuaTable _self = self.checkTable();
    return new Color(
        (float)_self.rawget("R").checkDouble(),
        (float)_self.rawget("G").checkDouble(),
        (float)_self.rawget("B").checkDouble(),
        (float)_self.rawget("A").checkDouble()
    );
  }
  
  public static LuaValue colorToLuaColor(Color color) throws LuaError {
    float[] compArray = color.getRGBComponents(null);
    return tableOf(
        valueOf("A"), valueOf(compArray[3]),
        valueOf("R"), valueOf(compArray[0]),
        valueOf("G"), valueOf(compArray[1]),
        valueOf("B"), valueOf(compArray[2])
    );
  }

  public static LuaValue ColorConstructor(LuaState state, Varargs varargs) throws LuaError {
    LuaTable self = varargs.first().checkTable();
    Turbine.ObjectConstructor(state, self);

    switch (varargs.count()) {
      case 1:
        self.rawset("A", valueOf(1.0));
        self.rawset("R", valueOf(1.0));
        self.rawset("G", valueOf(1.0));
        self.rawset("B", valueOf(1.0));
        break;
      case 4:
        self.rawset("A", valueOf(1.0));
        self.rawset("R", varargs.checkValue(2).checkNumber());
        self.rawset("G", varargs.checkValue(3).checkNumber());
        self.rawset("B", varargs.checkValue(4).checkNumber());
        break;
      case 5:
        self.rawset("A", varargs.checkValue(2).checkNumber());
        self.rawset("R", varargs.checkValue(3).checkNumber());
        self.rawset("G", varargs.checkValue(4).checkNumber());
        self.rawset("B", varargs.checkValue(5).checkNumber());
        break;
    }

    return Constants.NIL;
  }
}
