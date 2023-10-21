package delta.games.lotro.lua.turbine.gameplay.party;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaBoolean;
import org.squiddev.cobalt.LuaNumber;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.function.RegisteredFunction;

/**
 * Party library for lua scripts.
 * @author MaxThlon
 */
public class Party
{
  public static void add(LuaState state, LuaTable gameplayEnv) {
    gameplayEnv.rawset("Party", RegisteredFunction.bind(new RegisteredFunction[]{
        RegisteredFunction.of("GetLeader", Party::getLeader),
        
        RegisteredFunction.of("GetMemberCount", Party::getMemberCount),
        RegisteredFunction.of("GetMember", Party::getMember),
        RegisteredFunction.of("IsMember", Party::isMember),
        
        RegisteredFunction.of("GetAssistTargetCount", Party::getAssistTargetCount),
        RegisteredFunction.of("GetAssistTarget", Party::getAssistTarget),
        RegisteredFunction.of("IsAssistTarget", Party::isAssistTarget)
    }));
  }
  
  public static LuaValue getLeader(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaNumber getMemberCount(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaValue getMember(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaBoolean isMember(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
  
  public static LuaNumber getAssistTargetCount(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaValue getAssistTarget(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaBoolean isAssistTarget(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
}
