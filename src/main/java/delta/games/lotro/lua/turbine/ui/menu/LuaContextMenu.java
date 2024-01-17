package delta.games.lotro.lua.turbine.ui.menu;

import javax.swing.JPopupMenu;
import javax.xml.ws.Holder;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.turbine.ui.LuaControl;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * ContextMenu library for lua scripts.
 * 
 * @author MaxThlon
 */
public final class LuaContextMenu {
  /**
   * Initialize lua ContextMenu package
   * 
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
    LuaTools.pushClass(lua, "Turbine", "Object");
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", LuaContextMenu::constructor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetItems", LuaContextMenu::getItems);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "ShowMenu", LuaContextMenu::showMenu);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "ShowMenuAt", LuaContextMenu::showMenuAt);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Close", LuaContextMenu::close);

    lua.setField(LuaTools.relativizeIndex(envIndex, -1), "ContextMenu");
  }

  private static JPopupMenu jPopupMenuSelf(Lua lua, int index) {
    return LuaObject.objectSelf(lua, index, JPopupMenu.class);
  }

  private static int constructor(Lua lua) {
    Holder<JPopupMenu> jPopupMenu = new Holder<JPopupMenu>();

    LuaTools.invokeAndWait(lua, () -> jPopupMenu.value = GuiFactory.buildPopupMenu());
    LuaControl.controlInheritedConstructor(lua, 1, jPopupMenu.value);
    return 0;
  }

  private static int getItems(Lua lua) {
    lua.pushNil();
    return 1;
  }

  private static int showMenu(Lua lua) {
    JPopupMenu jPopupMenu = jPopupMenuSelf(lua, 1);
    jPopupMenu.setVisible(true);
    return 0;
  }

  private static int showMenuAt(Lua lua) {
    return 0;
  }

  private static int close(Lua lua) {
    return 0;
  }
}
