package delta.games.lotro.lua.turbine.ui;

import java.awt.Component;
import java.awt.Container;
import java.util.List;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;

/**
 * ControlList library for lua scripts.
 * 
 * @author MaxThlon
 */
public final class LuaControlList {
  /**
   * Initialize lua ControlList package
   * 
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
    LuaTools.pushClass(lua, "Turbine", "Object");
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", LuaControlList::constructor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetCount", LuaControlList::getCount);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Get", LuaControlList::get);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Set", LuaControlList::set);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Contains", LuaControlList::contains);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IndexOf", LuaControlList::indexOf);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Add", LuaControlList::add);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Insert", LuaControlList::insert);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Remove", LuaControlList::remove);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "RemoveAt", LuaControlList::removeAt);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Clear", LuaControlList::clear);

    lua.setField(-2, "ControlList");
  }

  /**
   * @param lua .
   * @param envIndex .
   * @param container .
   * @return int.
   */
  public static int newLuaControlList(Lua lua, int envIndex, Container container) {
    LuaTools.pushValue(lua, envIndex, "Turbine", "UI", "ControlList");
    lua.push(container, Conversion.NONE);
    lua.pCall(1, 1);
    return 1;
  }

  /**
   * @param lua .
   * @param index .
   * @return a list of component.
   */
  @SuppressWarnings("unchecked")
  public static List<Component> controlListSelf(Lua lua, int index) {
    return LuaObject.objectSelf(lua, index, List.class);
  }

  private static int constructor(Lua lua) {
    LuaObject.ObjectInheritedConstructor(lua, 1, lua.toJavaObject(2), null, null);
    return 0;
  }

  private static int getCount(Lua lua) {
    List<Component> controlList = controlListSelf(lua, 1);
    lua.push(controlList.size());
    return 1;
  }

  private static int get(Lua lua) {
    List<Component> controlList = controlListSelf(lua, 1);

    LuaObject.findLuaObjectFromObject(controlList.get((int)lua.toNumber(2)));
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
    // List<Component> controlList = controlListSelf(lua, 1);
    return 0;
  }

  private static int insert(Lua lua) {
    // List<Component> controlList = controlListSelf(lua, 1);
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
