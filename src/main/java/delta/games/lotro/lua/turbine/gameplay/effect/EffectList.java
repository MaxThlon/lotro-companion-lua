package delta.games.lotro.lua.turbine.gameplay.effect;

import delta.games.lotro.lua.turbine.object.LuaObject;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class EffectList
{
  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = LuaObject.callInherit(lua, -3, "Turbine", "Object");
  	if (error != Lua.LuaError.OK) return error;
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
    
    return error;
  }
  
  public static int constructor(Lua lua) {
    return 1;
  }
  
  public static int getCount(Lua lua) {
    return 1;
  }
  
  public static int get(Lua lua) {
    return 1;
  }
  
  public static int contains(Lua lua) {
    return 1;
  }
  
  public static int indexOf(Lua lua) {
    return 1;
  }
}
