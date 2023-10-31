package delta.games.lotro.lua.turbine.plugin;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class PluginData
{
  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;

  	if ((error = LuaObject.callInherit(lua, -3, "Turbine", "Object")) != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", PluginData::constructor);
    LuaTools.setFunction(lua, -1, -3, "Load", PluginData::load);
    LuaTools.setFunction(lua, -1, -3, "Save", PluginData::save);

    lua.setField(-2, "PluginData");
    return error;
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }

  private static int load(Lua lua) {

    return 1;
  }
  
  private static int save(Lua lua) {

    return 1;
  }
}
