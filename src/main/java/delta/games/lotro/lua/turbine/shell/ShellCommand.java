package delta.games.lotro.lua.turbine.shell;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaString;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.games.lotro.lua.turbine.Turbine;

/**
 * ShellCommand for lua scripts.
 * @author MaxThlon
 */
public abstract class ShellCommand {

  public static void add(LuaState state,
                         LuaTable turbine) throws LuaError, UnwindThrowable {

    LuaTable luaShellCommandClass = Turbine._luaClass.call(state, Turbine._luaObjectClass).checkTable();
    RegisteredFunction.bind(luaShellCommandClass, new RegisteredFunction[]{
        RegisteredFunction.of("Execute", ShellCommand::Execute),
        RegisteredFunction.of("GetHelp", ShellCommand::GetHelp),
        RegisteredFunction.of("GetShortHelp", ShellCommand::GetShortHelp)
    });
    
    turbine.rawset("ShellCommand", luaShellCommandClass);
  }
  
  public static LuaValue Execute(LuaState state, LuaValue self) {

    return Constants.NIL;
  }
  
  public static LuaString GetHelp(LuaState state, LuaValue self) {

    return Constants.EMPTYSTRING;
  }

  public static LuaString GetShortHelp(LuaState state, LuaValue self) {

    return Constants.EMPTYSTRING;
  }
}
