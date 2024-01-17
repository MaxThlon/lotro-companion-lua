package delta.games.lotro.lua.turbine.ui.menu;

import javax.swing.JMenuItem;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;

/**
 * MenuItemList library for lua scripts.
 * 
 * @author MaxThlon
 */
public final class LuaMenuItemList {
  /**
   * Initialize lua MenuItemList package.
   * 
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
    LuaTools.pushClass(lua, "Turbine", "Object");
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", LuaMenuItemList::constructor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetCount", LuaMenuItemList::getCount);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Get", LuaMenuItemList::get);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Set", LuaMenuItemList::set);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Contains", LuaMenuItemList::contains);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IndexOf", LuaMenuItemList::indexOf);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Add", LuaMenuItemList::add);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Insert", LuaMenuItemList::insert);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Remove", LuaMenuItemList::remove);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "RemoveAt", LuaMenuItemList::removeAt);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Clear", LuaMenuItemList::clear);

    lua.setField(LuaTools.relativizeIndex(envIndex, -1), "MenuItemList");
  }

  /**
   * @param lua .
   * @param index .
   * @return a JMenuItem.
   */
  private static JMenuItem jMenuItemSelf(Lua lua, int index) {
    return LuaObject.objectSelf(lua, index, JMenuItem.class);
  }

  /**
   * @param lua .
   * @param envIndex .
   * @param jMenuItem .
   * @return 1
   */
  public static int newLuaMenuItemList(Lua lua, int envIndex, JMenuItem jMenuItem) {
    LuaTools.newClassInstance(lua, envIndex, (relativeEnvIndex) -> {
      LuaTools.pushValue(lua, relativeEnvIndex.intValue(), "Turbine", "UI", "MenuItemList");
      // lua.push(jMenuItem, Conversion.NONE);
    });
    return 1;
  }

  private static int constructor(Lua lua) {
    LuaObject.ObjectInheritedConstructor(lua, 1, lua.toJavaObject(2), null, null);
    return 0;
  }

  private static int getCount(Lua lua) {
    JMenuItem jMenuItem = jMenuItemSelf(lua, 1);
    lua.push(jMenuItem.getComponentCount());
    return 1;
  }

  private static int get(Lua lua) {
    JMenuItem jMenuItemSelf = jMenuItemSelf(lua, 1);
    lua.push(LuaObject.findLuaObjectFromObject(jMenuItemSelf.getComponent((int)lua.toNumber(2))), Conversion.FULL);
    return 1;
  }

  private static int set(Lua lua) {
    return 0;
  }

  private static int contains(Lua lua) {
    lua.push(false);
    return 1;
  }

  private static int indexOf(Lua lua) {
    lua.push(1);
    return 1;
  }

  private static int add(Lua lua) {
    JMenuItem jMenuItemSelf = jMenuItemSelf(lua, 1);
    JMenuItem jMenuItem = jMenuItemSelf(lua, 2);

    jMenuItemSelf.add(jMenuItem);
    return 0;
  }

  private static int insert(Lua lua) {
    return 0;
  }

  private static int remove(Lua lua) {
    return 0;
  }

  private static int removeAt(Lua lua) {
    return 0;
  }

  private static int clear(Lua lua) {
    return 0;
  }
}
