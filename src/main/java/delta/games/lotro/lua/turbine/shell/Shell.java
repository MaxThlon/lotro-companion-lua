package delta.games.lotro.lua.turbine.shell;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * Shell library for lua scripts.
 * @author MaxThlon
 */
public abstract class Shell {

  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	
  	if ((error = ShellCommand.add(lua)) != Lua.LuaError.OK) return error;

  	if ((error  = LuaObject.callInherit(lua, -3, "Turbine", "Object")) != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", Shell::constructor);
    LuaTools.setFunction(lua, -1, -3, "WriteLine", Shell::WriteLine);
    LuaTools.setFunction(lua, -1, -3, "GetCommands", Shell::GetCommands);
    LuaTools.setFunction(lua, -1, -3, "AddCommand", Shell::AddCommand);
    LuaTools.setFunction(lua, -1, -3, "RemoveCommand", Shell::RemoveCommand);
    LuaTools.setFunction(lua, -1, -3, "IsCommand", Shell::IsCommand);

    if ((error = lua.pCall(0, 1)) != Lua.LuaError.OK) return error;
    lua.setField(-2, "Shell");
    return error;
  }
  
  public static int constructor(Lua lua) {
    return 1;
  }

  public static int WriteLine(Lua lua) {
    //UI.dragonConsole.append(self.checkString());
    return 1;
  }
  
  public static int GetCommands(Lua lua) {
    return 1;
  }
  
  public static int AddCommand(Lua lua) {
    return 1;
  }
  
  public static int RemoveCommand(Lua lua) {
    return 1;
  }
  
  public static int IsCommand(Lua lua) {
    return 1;
  }
}