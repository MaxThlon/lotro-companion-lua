package delta.games.lotro.lua.turbine.gameplay.entity;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class Actor
{
  /**
   * Initialize lua Actor package
   * @param lua .
   * @param envIndex .
   * @param errfunc .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError add(Lua lua, int envIndex, int errfunc) {
  	Lua.LuaError error;
  	error = LuaTools.pushClass(lua, errfunc, "Turbine", "Gameplay", "Entity");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", Actor::constructor);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetLevel", Actor::getLevel);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetTarget", Actor::getTarget);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetEffects", Actor::getEffects);
    
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetMorale", Actor::getMorale);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetBaseMaxMorale", Actor::getBaseMaxMorale);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetTemporaryMorale", Actor::getTemporaryMorale);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetMaxTemporaryMorale", Actor::getMaxTemporaryMorale);

    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetPower", Actor::getPower);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetBaseMaxPower", Actor::getBaseMaxPower);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetTemporaryPower", Actor::getTemporaryPower);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetMaxTemporaryPower", Actor::getMaxTemporaryPower);
    
    lua.setField(-2, "Actor");
    
    return error;
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }
  
  private static int getLevel(Lua lua) {
    return 1;
  }
  
  private static int getTarget(Lua lua) {
    return 1;
  }
  
  private static int getEffects(Lua lua) {
    return 1;
  }
  
  private static int getMorale(Lua lua) {
    return 1;
  }
  
  private static int getBaseMaxMorale(Lua lua) {
    return 1;
  }
  
  private static int getTemporaryMorale(Lua lua) {
    return 1;
  }
  
  private static int getMaxTemporaryMorale(Lua lua) {
    return 1;
  }
  
  private static int getPower(Lua lua) {
    return 1;
  }
  
  private static int getBaseMaxPower(Lua lua) {
    return 1;
  }
  
  private static int getTemporaryPower(Lua lua) {
    return 1;
  }
  
  private static int getMaxTemporaryPower(Lua lua) {
    return 1;
  }
}
