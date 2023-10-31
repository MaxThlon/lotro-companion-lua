package delta.games.lotro.lua.turbine.gameplay.entity;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class Actor
{
  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = LuaObject.callInherit(lua, -3, "Turbine", "Gameplay", "Entity");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", Actor::constructor);
  	LuaTools.setFunction(lua, -1, -3, "GetLevel", Actor::getLevel);
    LuaTools.setFunction(lua, -1, -3, "GetTarget", Actor::getTarget);
    LuaTools.setFunction(lua, -1, -3, "GetEffects", Actor::getEffects);
    
    LuaTools.setFunction(lua, -1, -3, "GetMorale", Actor::getMorale);
    LuaTools.setFunction(lua, -1, -3, "GetBaseMaxMorale", Actor::getBaseMaxMorale);
    LuaTools.setFunction(lua, -1, -3, "GetTemporaryMorale", Actor::getTemporaryMorale);
    LuaTools.setFunction(lua, -1, -3, "GetMaxTemporaryMorale", Actor::getMaxTemporaryMorale);

    LuaTools.setFunction(lua, -1, -3, "GetPower", Actor::getPower);
    LuaTools.setFunction(lua, -1, -3, "GetBaseMaxPower", Actor::getBaseMaxPower);
    LuaTools.setFunction(lua, -1, -3, "GetTemporaryPower", Actor::getTemporaryPower);
    LuaTools.setFunction(lua, -1, -3, "GetMaxTemporaryPower", Actor::getMaxTemporaryPower);
    
    lua.setField(-2, "Actor");
    
    return error;
  }
  
  public static int constructor(Lua lua) {
    return 1;
  }
  
  public static int getLevel(Lua lua) {
    return 1;
  }
  
  public static int getTarget(Lua lua) {
    return 1;
  }
  
  public static int getEffects(Lua lua) {
    return 1;
  }
  
  public static int getMorale(Lua lua) {
    return 1;
  }
  
  public static int getBaseMaxMorale(Lua lua) {
    return 1;
  }
  
  public static int getTemporaryMorale(Lua lua) {
    return 1;
  }
  
  public static int getMaxTemporaryMorale(Lua lua) {
    return 1;
  }
  
  public static int getPower(Lua lua) {
    return 1;
  }
  
  public static int getBaseMaxPower(Lua lua) {
    return 1;
  }
  
  public static int getTemporaryPower(Lua lua) {
    return 1;
  }
  
  public static int getMaxTemporaryPower(Lua lua) {
    return 1;
  }
}
