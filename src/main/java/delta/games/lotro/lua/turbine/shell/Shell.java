package delta.games.lotro.lua.turbine.shell;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.games.lotro.lua.turbine.ui.UI;

/**
 * Shell library for lua scripts.
 * @author MaxThlon
 */
public abstract class Shell {

  public static void add(LuaState state, LuaTable turbine) {
    
    LuaTable shell = RegisteredFunction.bind(new RegisteredFunction[]{
        RegisteredFunction.of("WriteLine", Shell::WriteLine),
        RegisteredFunction.of("GetCommands", Shell::GetCommands),
        RegisteredFunction.of("AddCommand", Shell::AddCommand),
        RegisteredFunction.of("RemoveCommand", Shell::RemoveCommand),
        RegisteredFunction.of("IsCommand", Shell::IsCommand)
    });

    turbine.rawset("Shell", shell);
  }
  
  public static LuaValue WriteLine(LuaState state, LuaValue self) throws LuaError {
    UI.dragonConsole.append(self.checkString());
    return Constants.NIL;
  }
  
  public static LuaValue GetCommands(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue AddCommand(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue RemoveCommand(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue IsCommand(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
}