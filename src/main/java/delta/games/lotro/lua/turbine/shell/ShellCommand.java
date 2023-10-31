package delta.games.lotro.lua.turbine.shell;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * ShellCommand for lua scripts.
 * @author MaxThlon
 */
public abstract class ShellCommand {

  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;

  	if ((error  = LuaObject.callInherit(lua, -3, "Turbine", "Object")) != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", ShellCommand::constructor);
    LuaTools.setFunction(lua, -1, -3, "Execute", ShellCommand::execute);
    LuaTools.setFunction(lua, -1, -3, "GetHelp", ShellCommand::getHelp);
    LuaTools.setFunction(lua, -1, -3, "GetShortHelp", ShellCommand::getShortHelp);

    lua.setField(-2, "ShellCommand");
    return error;
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }

  private static int execute(Lua lua) {

    return 1;
  }
  
  private static int getHelp(Lua lua) {

    return 1;
  }

  private static int getShortHelp(Lua lua) {

    return 1;
  }
}
