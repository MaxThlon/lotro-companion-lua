package delta.games.lotro.lua.turbine.shell;

import delta.games.lotro.lua.LuaLotro;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;

/**
 * Shell library for lua scripts.
 * @author MaxThlon
 */
public abstract class Shell {

  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	
  	if ((error = ShellCommand.add(lua)) != Lua.LuaError.OK) return error;

  	if ((error  = LuaObject.callInherit(lua, -3, "Turbine", "Object")) != Lua.LuaError.OK) return error;
    LuaTools.setFunction(lua, -1, -3, "WriteLine", LuaTools::print);
    LuaTools.setFunction(lua, -1, -3, "GetCommands", Shell::GetCommands);
    LuaTools.setFunction(lua, -1, -3, "AddCommand", Shell::AddCommand);
    LuaTools.setFunction(lua, -1, -3, "RemoveCommand", Shell::RemoveCommand);
    LuaTools.setFunction(lua, -1, -3, "IsCommand", Shell::IsCommand);

    lua.setField(-2, "Shell");
    return error;
  }
  
  public static int GetCommands(Lua lua) {
  	LuaLotro luaLotro = (LuaLotro)LuaTools.getJavaLuaModuleImpl(lua);

  	lua.push(luaLotro.getCommands(), Conversion.FULL);
    return 1;
  }
  
  public static int AddCommand(Lua lua) {
  	LuaLotro luaLotro = (LuaLotro)LuaTools.getJavaLuaModuleImpl(lua);

  	lua.pushValue(2);
  	luaLotro.addCommand(lua.toString(1), lua.get());
    return 1;
  }
  
  public static int RemoveCommand(Lua lua) {
  	LuaLotro luaLotro = (LuaLotro)LuaTools.getJavaLuaModuleImpl(lua);

  	luaLotro.removeCommand(lua.toString(1));
    return 1;
  }
  
  public static int IsCommand(Lua lua) {
  	LuaLotro luaLotro = (LuaLotro)LuaTools.getJavaLuaModuleImpl(lua);

  	lua.push(luaLotro.findCommand(lua.toString(1)) != null);
    return 1;
  }
}