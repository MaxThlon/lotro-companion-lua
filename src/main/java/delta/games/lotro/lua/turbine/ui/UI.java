package delta.games.lotro.lua.turbine.ui;

import java.awt.Color;
import java.util.HashMap;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeNode;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeNodeList;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * UI library for lua scripts.
 * @author MaxThlon
 */
public abstract class UI {
  @SuppressWarnings("boxing")
  public static Lua.LuaError openPackage(Lua lua, int globalsIndex) {
  	Lua.LuaError error;

  	LuaTools.pushfenv(
    		lua,
    		globalsIndex,
    		"Turbine.UI",
    		"Turbine", "UI"
    );
  	LuaTools.pushModule(
  			lua,
  			LuaTools.relativizeIndex(globalsIndex, -1),
  			"Turbine", "UI"
  	);
    lua.push(new HashMap<String, String>() {{
        put("Color", "Color");
        put("Normal", "Normal");
        put("Multiply", "Multiply");
        put("AlphaBlend", "AlphaBlend");
        put("Overlay", "Overlay");
        put("Grayscale", "Grayscale");
        put("Screen", "Screen");
        put("None", "None");
        put("Undefined", "Undefined");
    }}, Lua.Conversion.FULL);
    lua.setField(-2, "BlendMode");

    lua.push(new HashMap<String, Integer>() {{
        put("TopLeft", 1);
        put("TopCenter", 2);
        put("TopRight", 3);
        put("MiddleLeft", 4);
        put("MiddleCenter", 5);
        put("MiddleRight", 6);
        put("BottomLeft", 7);
        put("BottomCenter", 8);
        put("BottomRight", 9);
        put("Undefined", 0);
    }}, Lua.Conversion.FULL);
    lua.setField(-2, "ContentAlignment");

    lua.push(new HashMap<String, String>() {{
        put("None", "None");
        put("Outline", "Outline");
    }}, Lua.Conversion.FULL);
    lua.setField(-2, "FontStyle");

    lua.push(new HashMap<String, Integer>() {{
        put("Left", 1);
        put("Middle", 4);
        put("Right", 2);
    }}, Lua.Conversion.FULL);
    lua.setField(-2, "MouseButton");

    lua.push(new HashMap<String, Integer>() {{
        put("Horizontal", 0);
        put("Vertical", 1);
    }}, Lua.Conversion.FULL);
    lua.setField(-2, "Orientation");

    lua.push(new HashMap<String, Integer>() {{
        put("LeftToRight", 0);
        put("RightToLeft", 1);
    }}, Lua.Conversion.FULL);
    lua.setField(-2, "HorizontalLayout");

    lua.push(new HashMap<String, Integer>() {{
        put("BottomToTop", 1);
        put("TopToBottom", 0);
    }}, Lua.Conversion.FULL);
    lua.setField(-2, "VerticalLayout");

    if ((error = LuaObject.callInherit(lua, -3, "Turbine", "Object")) != Lua.LuaError.OK) return error;
    LuaTools.setFunction(lua, -1, -3, "Constructor", UI::ColorConstructor);
    lua.setField(-2, "Color");

    if ((error  = LuaControl.add(lua)) != Lua.LuaError.OK) return error;
    if ((error  = LuaDisplay.add(lua)) != Lua.LuaError.OK) return error;
    if ((error = LuaScrollableControl.add(lua)) != Lua.LuaError.OK) return error;
    if ((error = LuaLabel.add(lua)) != Lua.LuaError.OK) return error;
    if ((error = LuaTextBox.add(lua)) != Lua.LuaError.OK) return error;
    if ((error = LuaButton.add(lua)) != Lua.LuaError.OK) return error;
    if ((error = LuaWindow.add(lua)) != Lua.LuaError.OK) return error;
    if ((error = LuaListBox.add(lua)) != Lua.LuaError.OK) return error;
    if ((error = LuaScrollBar.add(lua)) != Lua.LuaError.OK) return error;
    if ((error = LuaTreeNode.add(lua)) != Lua.LuaError.OK) return error;
    if ((error = LuaTreeNodeList.add(lua)) != Lua.LuaError.OK) return error;
    if ((error = LuaTreeView.add(lua)) != Lua.LuaError.OK) return error;

    lua.pop(2); /* pop env, module */
    return error;
  }
  
  public static Color luaColorToColor(Lua lua, int index) {
  	lua.getField(index, "A");
  	lua.getField(index, "R");
  	lua.getField(index, "G");
  	lua.getField(index, "B");
  	
  	Color color = new Color(
        (float)lua.toNumber(-3),
        (float)lua.toNumber(-2),
        (float)lua.toNumber(-1),
        (float)lua.toNumber(-4)
    );
  	lua.pop(4);
  	return color;
  }
  
  public static HashMap<String, Double> colorToLuaColor(Color color) {
    float[] compArray = color.getRGBComponents(null);

    return new HashMap<String, Double>() {{
        put("A", Double.valueOf(compArray[3]));
        put("R", Double.valueOf(compArray[0]));
        put("G", Double.valueOf(compArray[1]));
        put("B", Double.valueOf(compArray[2]));
    }};
  }

  private static int ColorConstructor(Lua lua) {
    LuaObject.ObjectInheritedConstructor(lua, 1, null, null, null);
    
    switch (lua.getTop()) {
      case 1: {
      	Double defaultValue = Double.valueOf(1.0);
      	lua.push(defaultValue); /* Value affected to "B" */
      	lua.pushValue(-1);
      	lua.setField(1, "A");
      	lua.pushValue(-1);
        lua.setField(1, "R");
        lua.pushValue(-1);
        lua.setField(1, "G");
        lua.setField(1, "B");
        break;
      }
      case 4:
      	lua.push(Double.valueOf(1.0));
      	lua.setField(1, "A");
      	lua.pushValue(2);
        lua.setField(1, "R");
        lua.pushValue(3);
        lua.setField(1, "G");
        lua.pushValue(4);
        lua.setField(1, "B");
        break;
      case 5:
        lua.pushValue(2);
        lua.setField(1, "A");
        lua.pushValue(3);
        lua.setField(1, "R");
        lua.pushValue(4);
        lua.setField(1, "G");
        lua.pushValue(5);
        lua.setField(1, "B");
        break;
    }

    return 1;
  }
}
