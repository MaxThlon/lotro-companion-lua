package delta.games.lotro.lua.turbine.ui;

import static org.squiddev.cobalt.ValueFactory.valueOf;
import static org.squiddev.cobalt.ValueFactory.varargsOf;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;

import org.squiddev.cobalt.LuaNumber;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.Varargs;
import org.squiddev.cobalt.function.RegisteredFunction;

/**
 * LuaDisplay library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaDisplay {

  public static void add(LuaState state,
                         LuaTable uiMetatable) {

    LuaTable luaDisplay = RegisteredFunction.bind(new RegisteredFunction[]{
        RegisteredFunction.of("GetWidth", LuaDisplay::GetWidth),
        RegisteredFunction.of("GetHeight", LuaDisplay::GetHeight),
        RegisteredFunction.ofV("GetSize", LuaDisplay::GetSize),
        RegisteredFunction.of("GetMouseX", LuaDisplay::GetMouseX),
        RegisteredFunction.of("GetMouseY", LuaDisplay::GetMouseY),
        RegisteredFunction.ofV("GetMousePosition", LuaDisplay::GetMousePosition)
    });
    
    uiMetatable.rawset("Display", luaDisplay);
  }

  public static LuaNumber GetWidth(LuaState state) {
    return valueOf(Toolkit.getDefaultToolkit().getScreenSize().width);
  }
  
  public static LuaNumber GetHeight(LuaState state) {
    return valueOf(Toolkit.getDefaultToolkit().getScreenSize().height);
  }
  
  public static Varargs GetSize(LuaState state, Varargs varargs) {
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    return varargsOf(valueOf(dimension.width), valueOf(dimension.height));
  }
  
  public static LuaNumber GetMouseX(LuaState state) {
    return valueOf(MouseInfo.getPointerInfo().getLocation().x);
  }

  public static LuaNumber GetMouseY(LuaState state) {
    return valueOf(MouseInfo.getPointerInfo().getLocation().y);
  }
  
  public static Varargs GetMousePosition(LuaState state, Varargs varargs) {
    Point location = MouseInfo.getPointerInfo().getLocation();
    
    return varargsOf(valueOf(location.x), valueOf(location.y));
  }
}
