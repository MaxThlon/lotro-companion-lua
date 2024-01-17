package delta.games.lotro.lua.turbine.shell;

import delta.games.lotro.lua.LotroLuaModule;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;

/**
 * Shell library for lua scripts.
 * @author MaxThlon
 */
public abstract class Shell {
  /**
   * Initialize lua Shell package
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
  	ShellCommand.add(lua, envIndex);

  	lua.createTable(0, 0);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsA", LuaObject::isA);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "WriteLine", LuaTools::print);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetCommands", Shell::getCommands);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "AddCommand", Shell::addCommand);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "RemoveCommand", Shell::removeCommand);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsCommand", Shell::isCommand);
    lua.setField(-2, "Shell");
  }
  
  private static int getCommands(Lua lua) {
  	LotroLuaModule luaLotro = (LotroLuaModule)LuaTools.getJavaLuaModule(lua);

  	lua.push(luaLotro.getCommands(), Conversion.FULL);
    return 1;
  }
  
  private static int addCommand(Lua lua) {
  	LotroLuaModule luaLotro = (LotroLuaModule)LuaTools.getJavaLuaModule(lua);

  	lua.pushValue(2);
  	luaLotro.addCommand(lua.toString(1), lua.get());
    return 1;
  }
  
  private static int removeCommand(Lua lua) {
  	LotroLuaModule luaLotro = (LotroLuaModule)LuaTools.getJavaLuaModule(lua);

  	luaLotro.removeCommand(lua.toString(1));
    return 1;
  }
  
  private static int isCommand(Lua lua) {
  	LotroLuaModule luaLotro = (LotroLuaModule)LuaTools.getJavaLuaModule(lua);

  	lua.push(luaLotro.findCommand(lua.toString(1)) != null);
    return 1;
  }
}