package delta.games.lotro.lua.turbine.shell;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaString;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.OperationHelper;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

/**
 * ShellCommand for lua scripts.
 * @author DAM
 */
public abstract class ShellCommand {

  public static void add(LuaState state,
                         LuaTable luaTurbine,
                         LuaFunction luaClass,
                         LuaTable luaObjectClass) throws LuaError, UnwindThrowable {

    LuaTable luaShellCommandClass = OperationHelper.call(state, luaClass, luaObjectClass).checkTable();
    RegisteredFunction.bind(luaShellCommandClass, new RegisteredFunction[]{
        RegisteredFunction.of("Execute", ShellCommand::Execute),
        RegisteredFunction.of("GetHelp", ShellCommand::GetHelp),
        RegisteredFunction.of("GetShortHelp", ShellCommand::GetShortHelp)
    });
    
    luaTurbine.rawset("ShellCommand", luaShellCommandClass);
  }
  
  public static LuaValue Execute(LuaState state, LuaValue self) throws LuaError, UnwindThrowable {

    return Constants.NIL;
  }
  
  public static LuaString GetHelp(LuaState state, LuaValue self) throws LuaError, UnwindThrowable {

    return Constants.EMPTYSTRING;
  }

  public static LuaString GetShortHelp(LuaState state, LuaValue self) throws LuaError, UnwindThrowable {

    return Constants.EMPTYSTRING;
  }
}
