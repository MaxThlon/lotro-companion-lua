package delta.games.lotro.lua.turbine.shell;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * ShellCommand for lua scripts.
 * 
 * @author MaxThlon
 */
public abstract class ShellCommand {
  /**
   * Initialize lua ShellCommand package
   * 
   * @param lua      .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
    LuaTools.pushClass(lua);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", ShellCommand::constructor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Execute", ShellCommand::execute);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetHelp", ShellCommand::getHelp);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetShortHelp", ShellCommand::getShortHelp);

    lua.setField(-2, "ShellCommand");
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
