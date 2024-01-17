package delta.games.lotro.lua.turbine.ui.menu;

import javax.swing.JMenuItem;
import javax.xml.ws.Holder;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.turbine.ui.LuaControl;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * MenuItem library for lua scripts.
 * 
 * @author MaxThlon
 */
public final class LuaMenuItem {
  /**
   * Initialize lua MenuItem package
   * 
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
    LuaTools.pushClass(lua, "Turbine", "Object");
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", LuaMenuItem::constructor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetText", LuaMenuItem::getText);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetText", LuaMenuItem::setText);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsEnabled", LuaMenuItem::isEnabled);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetEnabled", LuaMenuItem::setEnabled);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsChecked", LuaMenuItem::isChecked);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetChecked", LuaMenuItem::setChecked);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetItems", LuaMenuItem::getItems);

    lua.setField(LuaTools.relativizeIndex(envIndex, -1), "MenuItem");
  }

  /**
   * @param lua .
   * @param index .
   * @return a menu item.
   */
  public static JMenuItem jMenuItemSelf(Lua lua, int index) {
    return LuaObject.objectSelf(lua, index, JMenuItem.class);
  }

  private static int constructor(Lua lua) {
    Holder<JMenuItem> jMenuItem = new Holder<JMenuItem>();

    LuaTools.invokeAndWait(lua, () -> jMenuItem.value = GuiFactory.buildMenuItem("meu"));
    LuaControl.controlInheritedConstructor(lua, 1, jMenuItem.value);
    return 0;
  }

  private static int getText(Lua lua) {
    lua.pushNil();
    return 1;
  }

  private static int setText(Lua lua) {
    return 0;
  }

  private static int isEnabled(Lua lua) {
    lua.pushNil();
    return 1;
  }

  private static int setEnabled(Lua lua) {
    return 0;
  }

  private static int isChecked(Lua lua) {
    lua.pushNil();
    return 1;
  }

  private static int setChecked(Lua lua) {
    return 0;
  }

  private static int getItems(Lua lua) {
    lua.pushNil();
    return 1;
  }
}
