package delta.games.lotro.lua.turbine.gameplay.player;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaBoolean;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

/**
 * @author MaxThlon
 */
public class LocalPlayer
{
  public static LuaTable add(LuaState state, LuaTable gameplayEnv,
                             LuaFunction luaClass, LuaValue luaPlayerClass) throws LuaError, UnwindThrowable {
    LuaTable luaLocalPlayerClass = luaClass.call(state, luaPlayerClass).checkTable();
    RegisteredFunction.bind(luaLocalPlayerClass, new RegisteredFunction[]{
      RegisteredFunction.of("Constructor", LocalPlayer::constructor),
      RegisteredFunction.of("GetInstance", LocalPlayer::getInstance),
      RegisteredFunction.of("GetRaceAttributes", LocalPlayer::getRaceAttributes),
      RegisteredFunction.of("GetClassAttributes", LocalPlayer::getClassAttributes),
      RegisteredFunction.of("GetAttributes", LocalPlayer::getAttributes),
      RegisteredFunction.of("GetTrainedSkills", LocalPlayer::getTrainedSkills),
      RegisteredFunction.of("GetUntrainedSkills", LocalPlayer::getUntrainedSkills),

      RegisteredFunction.of("GetWallet", LocalPlayer::getWallet),
      RegisteredFunction.of("GetEquipment", LocalPlayer::getEquipment),
      RegisteredFunction.of("GetBackpack", LocalPlayer::getBackpack),
      RegisteredFunction.of("GetVault", LocalPlayer::getVault),
      RegisteredFunction.of("GetSharedStorage", LocalPlayer::getSharedStorage),
      RegisteredFunction.of("GetMount", LocalPlayer::getMount),
      
      RegisteredFunction.of("GetParty", LocalPlayer::getParty),
      RegisteredFunction.of("IsInCombat", LocalPlayer::isInCombat)
    });
    gameplayEnv.rawset("LocalPlayer", luaLocalPlayerClass);

    return luaLocalPlayerClass;
  }
  
  private static LuaValue constructor(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  private static LuaValue getInstance(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  private static LuaValue getRaceAttributes(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  private static LuaValue getClassAttributes(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  private static LuaValue getAttributes(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  private static LuaValue getTrainedSkills(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  private static LuaValue getUntrainedSkills(LuaState state, LuaValue self) {
    return Constants.NIL;
  }

  private static LuaValue getWallet(LuaState state, LuaValue self) {
    return Constants.NIL;
  }

  private static LuaValue getEquipment(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  private static LuaValue getBackpack(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  private static LuaValue getVault(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  private static LuaValue getSharedStorage(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  private static LuaValue getMount(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  private static LuaValue getParty(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  private static LuaBoolean isInCombat(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
}
