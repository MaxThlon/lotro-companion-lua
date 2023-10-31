package delta.games.lotro.lua.turbine.gameplay.player;

import delta.games.lotro.lua.turbine.object.LuaObject;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class LocalPlayer
{
  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = LuaObject.callInherit(lua, -3, "Turbine", "Gameplay", "Player");
  	if (error != Lua.LuaError.OK) return error;
    lua.push((JFunction)LocalPlayer::constructor);
    lua.setField(-2, "Constructor");
    lua.push((JFunction)LocalPlayer::getInstance);
    lua.setField(-2, "GetInstance");
    lua.push((JFunction)LocalPlayer::getRaceAttributes);
    lua.setField(-2, "GetRaceAttributes");
    lua.push((JFunction)LocalPlayer::getClassAttributes);
    lua.setField(-2, "GetClassAttributes");
    lua.push((JFunction)LocalPlayer::getAttributes);
    lua.setField(-2, "GetAttributes");
    lua.push((JFunction)LocalPlayer::getTrainedSkills);
    lua.setField(-2, "GetTrainedSkills");
    lua.push((JFunction)LocalPlayer::getUntrainedSkills);
    lua.setField(-2, "GetUntrainedSkills");

    lua.push((JFunction)LocalPlayer::getWallet);
    lua.setField(-2, "GetWallet");
    lua.push((JFunction)LocalPlayer::getEquipment);
    lua.setField(-2, "GetEquipment");
    lua.push((JFunction)LocalPlayer::getBackpack);
    lua.setField(-2, "GetBackpack");
    lua.push((JFunction)LocalPlayer::getVault);
    lua.setField(-2, "GetVault");
    lua.push((JFunction)LocalPlayer::getSharedStorage);
    lua.setField(-2, "GetSharedStorage");
    lua.push((JFunction)LocalPlayer::getMount);
    lua.setField(-2, "GetMount");

    lua.push((JFunction)LocalPlayer::getParty);
    lua.setField(-2, "GetParty");
    lua.push((JFunction)LocalPlayer::isInCombat);
    lua.setField(-2, "IsInCombat");

    lua.setField(-2, "LocalPlayer");
    return error;
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }
  
  private static int getInstance(Lua lua) {
    return 1;
  }
  
  private static int getRaceAttributes(Lua lua) {
    return 1;
  }
  
  private static int getClassAttributes(Lua lua) {
    return 1;
  }
  
  private static int getAttributes(Lua lua) {
    return 1;
  }
  
  private static int getTrainedSkills(Lua lua) {
    return 1;
  }
  
  private static int getUntrainedSkills(Lua lua) {
    return 1;
  }

  private static int getWallet(Lua lua) {
    return 1;
  }

  private static int getEquipment(Lua lua) {
    return 1;
  }
  
  private static int getBackpack(Lua lua) {
    return 1;
  }
  
  private static int getVault(Lua lua) {
    return 1;
  }
  
  private static int getSharedStorage(Lua lua) {
    return 1;
  }
  
  private static int getMount(Lua lua) {
    return 1;
  }
  
  private static int getParty(Lua lua) {
    return 1;
  }
  
  private static int isInCombat(Lua lua) {
    return 1;
  }
}
