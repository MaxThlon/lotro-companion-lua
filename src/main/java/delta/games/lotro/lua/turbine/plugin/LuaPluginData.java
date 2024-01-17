package delta.games.lotro.lua.turbine.plugin;

import delta.games.lotro.lua.LotroLuaModule;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public final class LuaPluginData
{
  /**
   * Initialize lua PluginData package
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
  	lua.createTable(0, 0);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Load", LuaPluginData::load);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Save", LuaPluginData::save);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsA", LuaObject::isA);
    lua.setField(-2, "PluginData");
  }

  private static int load(Lua lua) {
  	Lua.LuaError error;
  	DataScope dataScope = DataScope.valueOfNumber(Integer.valueOf((int)lua.toNumber(1)));
  	String key  = lua.toString(2);
  	LotroLuaModule luaLotro = (LotroLuaModule)LuaTools.getJavaLuaModule(lua);
  	
  	error = luaLotro.loadPluginData(lua, dataScope, key);
  	return (error == Lua.LuaError.OK)?1:-1;
  }

  /*
   * @param dataScope Type: DataScope, The scope of the data. This specifies the level of availability of this data when loading. Data scope can be specified to an Account, a Server, or to a specific Character.
   * @param key Type: string, The key to store the data under.
   * @param data Type: Object, The data to save.
   * @param saveCompleteEventHandler Type: Object, Event handler called when the data is saved. It has two arguments, the first specifies if the save succeeded and the second is a message if it failed.
   * Remarks
   * This method serializes the object data passed to the specified key. Any existing data is replaced with the new data.s
   */
  private static int save(Lua lua) {
  	DataScope dataScope = DataScope.valueOfNumber(Integer.valueOf((int)lua.toNumber(1)));
  	String key  = lua.toString(2);
  	LotroLuaModule luaLotro = (LotroLuaModule)LuaTools.getJavaLuaModule(lua);
  	
  	luaLotro.savePluginData(lua, dataScope, key, 3);
  	return 1;
  }
}
