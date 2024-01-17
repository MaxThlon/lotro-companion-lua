package delta.games.lotro.lua.turbine.plugin;

import delta.common.framework.plugin.PluginManager;
import delta.games.lotro.client.plugin.Plugin;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class LuaPluginManager
{
  /**
   * Initialize lua PluginManager package
   * @param lua .
   * @param envIndex .
   * @param namespaceIndex .
   */
  public static void add(Lua lua, int envIndex, int namespaceIndex) {
  	lua.createTable(0, 0);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsA", LuaObject::isA);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetAvailablePlugins", LuaPluginManager::getAvailablePlugins);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetLoadedPlugins", LuaPluginManager::getLoadedPlugins);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "LoadPlugin", LuaPluginManager::loadPlugin);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "RefreshAvailablePlugins", LuaPluginManager::refreshAvailablePlugins);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "ShowOptions", LuaPluginManager::showOptions);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "UnloadScriptState", LuaPluginManager::unloadScriptState);
    lua.setField(LuaTools.relativizeIndex(namespaceIndex, -1), "PluginManager");
  }

  private static int getAvailablePlugins(Lua lua) {
  	int index = 1;
  	lua.createTable(0, 0);
  	
  	for (Plugin plugin:PluginManager.getInstance().getPlugins()) {
  		lua.createTable(0, 0);
  		lua.push(plugin.getInformation()._image);
  		lua.setField(-2, "Image");
  		lua.push(plugin.getInformation()._description);
  		lua.setField(-2, "Description");
  		lua.push(plugin.getInformation()._version);
  		lua.setField(-2, "Version");
  		lua.push(plugin.getPackage());
  		lua.setField(-2, "Package");
  		lua.push(plugin.getInformation()._author);
  		lua.setField(-2, "Author");
  		lua.push(plugin.getInformation()._name);
  		lua.setField(-2, "Name");
  		lua.push(plugin.getConfiguration().get("Apartment"));
  		lua.setField(-2, "ScriptState");
  		lua.rawSetI(-2, index++);
  	}
    return 1;
  }
  
  private static int getLoadedPlugins(Lua lua) {

    return 1;
  }
  
  private static int loadPlugin(Lua lua) {
  	String pluginName = lua.toString(1);
  	Plugin plugin = PluginManager.getInstance().getPlugins().stream()
  															 .filter(itePlugin -> pluginName.equals(itePlugin.getInformation()._name))
  															 .findFirst()
  															 .orElse(null);
  	if (plugin != null) {
  		PluginManager.getInstance().loadPlugin(plugin);
  	}
    return 0;
  }
  
  private static int refreshAvailablePlugins(Lua lua) {
  	PluginManager.getInstance().refreshPluginList();
    return 0;
  }
  
  private static int showOptions(Lua lua) {
    return 0;
  }
  
  private static int unloadScriptState(Lua lua) {

    return 1;
  }
}
