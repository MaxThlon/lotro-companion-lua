package delta.games.lotro.lua.turbine.ui;

import java.util.HashMap;

import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.turbine.ui.graphic.LuaGraphic;
import delta.games.lotro.lua.turbine.ui.menu.LuaContextMenu;
import delta.games.lotro.lua.turbine.ui.menu.LuaMenuItem;
import delta.games.lotro.lua.turbine.ui.menu.LuaMenuItemList;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeNode;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeNodeList;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * UI library for lua scripts.
 * 
 * @author MaxThlon
 */
public final class UI {
  /**
   * Initialize lua UI package
   * 
   * @param lua      .
   * @param envIndex .
   */
  public static void openPackage(Lua lua, int envIndex) {
    Turbine.pushfenv(lua, envIndex, "Turbine.UI", "Turbine", "UI");

    lua.push(new HashMap<String, Integer>() {
      {
        put("Color", Integer.valueOf(1));
        put("Normal", Integer.valueOf(2));
        put("Multiply", Integer.valueOf(3));
        put("AlphaBlend", Integer.valueOf(4));
        put("Overlay", Integer.valueOf(5));
        put("Grayscale", Integer.valueOf(6));
        put("Screen", Integer.valueOf(7));
        put("None", Integer.valueOf(8));
        put("Undefined", Integer.valueOf(8));
      }
    }, Lua.Conversion.FULL);
    LuaTools.setFunction(lua, -1, "IsA", LuaObject::isA);
    lua.setField(-2, "BlendMode");

    lua.push(new HashMap<String, Integer>() {
      {
        put("Undefined", Integer.valueOf(0));
        put("TopLeft", Integer.valueOf(1));
        put("TopCenter", Integer.valueOf(2));
        put("TopRight", Integer.valueOf(3));
        put("MiddleLeft", Integer.valueOf(4));
        put("MiddleCenter", Integer.valueOf(5));
        put("MiddleRight", Integer.valueOf(6));
        put("BottomLeft", Integer.valueOf(7));
        put("BottomCenter", Integer.valueOf(8));
        put("BottomRight", Integer.valueOf(9));

      }
    }, Lua.Conversion.FULL);
    LuaTools.setFunction(lua, -1, "IsA", LuaObject::isA);
    lua.setField(-2, "ContentAlignment");

    lua.push(new HashMap<String, Integer>() {
      {
        put("None", Integer.valueOf(0));
        put("Outline", Integer.valueOf(8));
      }
    }, Lua.Conversion.FULL);
    LuaTools.setFunction(lua, -1, "IsA", LuaObject::isA);
    lua.setField(-2, "FontStyle");

    lua.push(new HashMap<String, Integer>() {
      {
        put("Left", Integer.valueOf(1));
        put("Middle", Integer.valueOf(4));
        put("Right", Integer.valueOf(2));
      }
    }, Lua.Conversion.FULL);
    LuaTools.setFunction(lua, -1, -2, "IsA", LuaObject::isA);
    lua.setField(-2, "MouseButton");

    lua.push(new HashMap<String, Integer>() {
      {
        put("Horizontal", Integer.valueOf(0));
        put("Vertical", Integer.valueOf(1));
      }
    }, Lua.Conversion.FULL);
    LuaTools.setFunction(lua, -1, "IsA", LuaObject::isA);
    lua.setField(-2, "Orientation");

    lua.push(new HashMap<String, Integer>() {
      {
        put("LeftToRight", Integer.valueOf(0));
        put("RightToLeft", Integer.valueOf(1));
      }
    }, Lua.Conversion.FULL);
    LuaTools.setFunction(lua, -1, "IsA", LuaObject::isA);
    lua.setField(-2, "HorizontalLayout");

    lua.push(new HashMap<String, Integer>() {
      {
        put("BottomToTop", Integer.valueOf(1));
        put("TopToBottom", Integer.valueOf(0));
      }
    }, Lua.Conversion.FULL);
    LuaTools.setFunction(lua, -1, "IsA", LuaObject::isA);
    lua.setField(-2, "VerticalLayout");

    LuaColor.add(lua, -1);
    LuaGraphic.add(lua, -1);
    LuaControlList.add(lua, -1);
    LuaDragDropInfo.add(lua, -1);
    LuaControl.add(lua, -1);
    LuaDisplay.add(lua, -1);
    LuaScrollBar.add(lua, -1);
    LuaScrollableControl.add(lua, -1);
    LuaLabel.add(lua, -1);
    LuaTextBox.add(lua, -1);
    LuaButton.add(lua, -1);
    LuaCheckBox.add(lua, -1);
    LuaListBox.add(lua, -1);
    LuaTreeNode.add(lua, -1);
    LuaTreeNodeList.add(lua, -1);
    LuaTreeView.add(lua, -1);
    LuaMenuItem.add(lua, -1);
    LuaMenuItemList.add(lua, -1);
    LuaContextMenu.add(lua, -1);
    LuaWindow.add(lua, -1);
    lua.pop(1); /* pop env */
  }
}
