package delta.games.lotro.lua.turbine.plugin;

import delta.games.lotro.client.plugin.Plugin;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;

/**
 * @author MaxThlon
 */
public class LuaPlugin {
  /**
   * Initialize lua Plugin package
   * @param lua .
   * @param envIndex .
   * @param errfunc .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError openPackage(Lua lua, int envIndex, int errfunc) {
  	Lua.LuaError error;

  	if ((error = LuaTools.pushEnum(lua)) != Lua.LuaError.OK) return error;
  	for (DataScope dataScope:DataScope.values()) {
  		lua.push(dataScope._value);
  		lua.setField(-2, dataScope._label);
  	}
  	lua.setField(-2, "DataScope");

    if ((error = LuaPluginData.add(lua, envIndex, errfunc)) != Lua.LuaError.OK) return error;

    if ((error = LuaTools.pushClass(lua, errfunc, "Turbine", "Object")) != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", LuaPlugin::constructor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetName", LuaPlugin::getName);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetVersion", LuaPlugin::getVersion);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetAuthor", LuaPlugin::getAuthor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetConfiguration", LuaPlugin::getConfiguration);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Load", LuaPlugin::load);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Unload", LuaPlugin::unload);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetOptionsPanel", LuaPlugin::getOptionsPanel);
    lua.setField(-2, "Plugin");
    
    error = LuaPluginManager.add(lua, envIndex, errfunc);
    return error;
  }
  
  public static Plugin pluginSelf(Lua lua, int index) {
    return LuaObject.objectSelf(lua, index, Plugin.class);
  }

  public static int newLuaPlugin(Lua lua, int envIndex, int errfunc, Plugin plugin) {
  	LuaTools.pushValue(lua, envIndex, "Turbine", "Object");
    lua.push(plugin, Conversion.NONE);
    LuaTools.pCall(lua, 1, 1, LuaTools.relativizeIndex(errfunc, -2));
    
    return 1;
  }
  
  private static int constructor(Lua lua) {
  	LuaObject.ObjectInheritedConstructor(
        lua,
        1,
        lua.toJavaObject(2),
        null,
        null
    );
    return 1;
  }
  
  private static int getName(Lua lua) {
  	Plugin plugin = LuaObject.objectSelf(lua, 1, Plugin.class);
  	lua.push(plugin.getInformation()._name);
    return 1;
  }
  
  private static int getVersion(Lua lua) {
  	Plugin plugin = LuaObject.objectSelf(lua, 1, Plugin.class);
  	lua.push(plugin.getInformation()._version);
    return 1;
  }

  private static int getAuthor(Lua lua) {
  	Plugin plugin = LuaObject.objectSelf(lua, 1, Plugin.class);
  	lua.push(plugin.getInformation()._author);
    return 1;
  }
  
  private static int getConfiguration(Lua lua) {
  	Plugin plugin = LuaObject.objectSelf(lua, 1, Plugin.class);
  	lua.push(plugin.getConfiguration(), Conversion.FULL);
    return 1;
  }
  
  private static int load(Lua lua) {
    return 1;
  }
  
  private static int unload(Lua lua) {
    return 1;
  }
  
  private static int getOptionsPanel(Lua lua) {
    return 1;
  }
}
