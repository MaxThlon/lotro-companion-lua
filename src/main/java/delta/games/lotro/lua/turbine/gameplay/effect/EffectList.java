package delta.games.lotro.lua.turbine.gameplay.effect;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class EffectList
{
  /**
   * Initialize lua EffectList package
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
  	LuaTools.pushClass(lua, "Turbine", "Object");
  	lua.push((JFunction)EffectList::constructor);
    lua.setField(-2, "Constructor");
    lua.push((JFunction)EffectList::getCount);
    lua.setField(-2, "GetCount");
    lua.push((JFunction)EffectList::get);
    lua.setField(-2, "Get");
    lua.push((JFunction)EffectList::contains);
    lua.setField(-2, "Contains");
    lua.push((JFunction)EffectList::indexOf);
    lua.setField(-2, "IndexOf");
    lua.setField(-2, "EffectList");
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }
  
  private static int getCount(Lua lua) {
    return 1;
  }
  
  private static int get(Lua lua) {
    return 1;
  }
  
  private static int contains(Lua lua) {
    return 1;
  }
  
  private static int indexOf(Lua lua) {
    return 1;
  }
}
