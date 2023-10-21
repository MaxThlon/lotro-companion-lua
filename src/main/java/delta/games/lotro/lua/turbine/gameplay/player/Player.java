package delta.games.lotro.lua.turbine.gameplay.player;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaBoolean;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaString;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

/**
 * @author MaxThlon
 */
public class Player
{
  public static LuaTable add(LuaState state, LuaTable gameplayEnv,
                             LuaFunction luaClass, LuaValue luaActorClass) throws LuaError, UnwindThrowable {
    LuaTable luaPlayerClass = luaClass.call(state, luaActorClass).checkTable();
    RegisteredFunction.bind(luaPlayerClass, new RegisteredFunction[]{
      RegisteredFunction.of("Constructor", Player::constructor),
      RegisteredFunction.of("GetClass", Player::getLotroClass),
      RegisteredFunction.of("GetRace", Player::getRace),
      RegisteredFunction.of("GetAlignment", Player::getAlignment),

      RegisteredFunction.of("IsLinkDead", Player::isLinkDead),
      RegisteredFunction.of("IsVoiceActive", Player::isVoiceActive),
      RegisteredFunction.of("IsVoiceEnabled", Player::isVoiceEnabled),
      
      RegisteredFunction.of("GetParty", Player::getParty),
      RegisteredFunction.of("GetReadyState", Player::getReadyState),
      
      RegisteredFunction.of("GetPet", Player::getPet),
    });
    gameplayEnv.rawset("Player", luaPlayerClass);

    return luaPlayerClass;
  }
  
  public static LuaValue constructor(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaString getLotroClass(LuaState state, LuaValue self) {
    return Constants.EMPTYSTRING;
  }
  
  public static LuaBoolean getRace(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
  
  public static LuaBoolean getAlignment(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
  
  public static LuaBoolean isLinkDead(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
  
  public static LuaBoolean isVoiceActive(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
  
  public static LuaBoolean isVoiceEnabled(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
  
  public static LuaBoolean getParty(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
  
  public static LuaBoolean getReadyState(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
  
  public static LuaBoolean getPet(LuaState state, LuaValue self) {
    return Constants.TRUE;
  }
}
