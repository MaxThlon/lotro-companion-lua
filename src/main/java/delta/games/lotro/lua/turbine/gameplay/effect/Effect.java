package delta.games.lotro.lua.turbine.gameplay.effect;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class Effect
{
  /**
   * Initialize lua Effect package
   * @param lua .
   * @param envIndex .
   * @param errfunc .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError add(Lua lua, int envIndex, int errfunc) {
  	Lua.LuaError error;
  	error = LuaTools.pushClass(lua, errfunc, "Turbine", "Object");
  	if (error != Lua.LuaError.OK) return error;
    lua.push((JFunction)Effect::constructor);
    lua.setField(-2, "Constructor");
    lua.push((JFunction)Effect::getID);
    lua.setField(-2, "GetID");
    lua.push((JFunction)Effect::getName);
    lua.setField(-2, "GetName");
    lua.push((JFunction)Effect::getDescription);
    lua.setField(-2, "GetDescription");
    lua.push((JFunction)Effect::getCategory);
    lua.setField(-2, "GetCategory");
    lua.push((JFunction)Effect::isDebuff);
    lua.setField(-2, "IsDebuff");
    lua.push((JFunction)Effect::isCurable);
    lua.setField(-2, "IsCurable");
    lua.push((JFunction)Effect::isDispellable);
    lua.setField(-2, "IsDispellable");
    lua.push((JFunction)Effect::getDuration);
    lua.setField(-2, "GetDuration");
    lua.push((JFunction)Effect::getStartTime);
    lua.setField(-2, "GetStartTime");
    lua.push((JFunction)Effect::getIcon);
    lua.setField(-2, "GetIcon");
    lua.setField(-2, "Effect");
    
    error = EffectList.add(lua, envIndex, errfunc);
    
    return error;
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }
  
  private static int getID(Lua lua) {
    return 1;
  }
  
  private static int getName(Lua lua) {
    return 1;
  }
  
  private static int getDescription(Lua lua) {
    return 1;
  }
  
  private static int getCategory(Lua lua) {
    return 1;
  }
  
  private static int isDebuff(Lua lua) {
    return 1;
  }
  
  private static int isCurable(Lua lua) {
    return 1;
  }
  
  private static int isDispellable(Lua lua) {
    return 1;
  }
  
  private static int getDuration(Lua lua) {
    return 1;
  }
  
  private static int getStartTime(Lua lua) {
    return 1;
  }
  
  private static int getIcon(Lua lua) {
    return 1;
  }
}
