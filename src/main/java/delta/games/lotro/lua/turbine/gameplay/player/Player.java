package delta.games.lotro.lua.turbine.gameplay.player;

import delta.games.lotro.lua.turbine.object.LuaObject;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class Player
{
  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = LuaObject.callInherit(lua, -3, "Turbine", "Gameplay", "Actor");
  	if (error != Lua.LuaError.OK) return error;
    lua.push((JFunction)Player::constructor);
    lua.setField(-2, "Constructor");
    lua.push((JFunction)Player::getLotroClass);
    lua.setField(-2, "GetClass");
    lua.push((JFunction)Player::getRace);
    lua.setField(-2, "GetRace");
    lua.push((JFunction)Player::getAlignment);
    lua.setField(-2, "GetAlignment");
    
    lua.push((JFunction)Player::isLinkDead);
    lua.setField(-2, "IsLinkDead");
    lua.push((JFunction)Player::isVoiceActive);
    lua.setField(-2, "IsVoiceActive");
    lua.push((JFunction)Player::isVoiceEnabled);
    lua.setField(-2, "IsVoiceEnabled");
    
    lua.push((JFunction)Player::getParty);
    lua.setField(-2, "GetParty");
    lua.push((JFunction)Player::getReadyState);
    lua.setField(-2, "GetReadyState");

    lua.push((JFunction)Player::getPet);
    lua.setField(-2, "GetPet");

    lua.setField(-2, "Player");
    return error;
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }
  
  private static int getLotroClass(Lua lua) {
    return 1;
  }
  
  private static int getRace(Lua lua) {
    return 1;
  }
  
  private static int getAlignment(Lua lua) {
    return 1;
  }
  
  private static int isLinkDead(Lua lua) {
    return 1;
  }
  
  private static int isVoiceActive(Lua lua) {
    return 1;
  }
  
  private static int isVoiceEnabled(Lua lua) {
    return 1;
  }
  
  private static int getParty(Lua lua) {
    return 1;
  }
  
  private static int getReadyState(Lua lua) {
    return 1;
  }
  
  private static int getPet(Lua lua) {
    return 1;
  }
}
