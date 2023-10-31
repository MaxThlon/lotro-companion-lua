package delta.games.lotro.lua.turbine;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import delta.common.framework.module.ModuleEvent;
import delta.games.lotro.client.plugin.Plugin;
import delta.games.lotro.lua.turbine.engine.Engine;
import delta.games.lotro.lua.turbine.gameplay.Gameplay;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.turbine.plugin.LuaPlugin;
import delta.games.lotro.lua.turbine.shell.Shell;
import delta.games.lotro.lua.turbine.ui.UI;
import delta.games.lotro.lua.turbine.ui.lotro.UiLotro;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.lua51.Lua51Consts;
import party.iroiro.luajava.value.LuaValue;

/**
 * Turbine library for lua scripts.
 * @author MaxThlon
 */
public final class Turbine {
  private static Logger LOGGER=Logger.getLogger(Turbine.class);
  
  public static LuaValue _luaClass;
  private static Map<String,Apartment> _apartments=new HashMap<String,Apartment>();

  /*
   * Initialize root environment
   */
  public static Lua.LuaError openRootPackage(Lua lua) {
    lua.register("import", Turbine::turbineImport);
    lua.register("processEvents", Turbine::processEvents);

    Lua localLua = lua.newThread();
    localLua.push("Turbine.Class");
    if (turbineImport(localLua, Lua51Consts.LUA_GLOBALSINDEX) != 1) return Lua.LuaError.RUNTIME;
    _luaClass = localLua.get("class");
    localLua.pop(1); /* pop "Turbine.Class" */
    localLua = null;
    lua.pop(1); /* pop Thread */
    return Lua.LuaError.OK;
  }
  
  /*
   * Initialize environment
   */
  @SuppressWarnings("boxing")
  public static Lua.LuaError openPackage(Lua lua) {
  	Lua.LuaError error;
  	LuaTools.pushModule(lua, -1, "Turbine"); /* push module */

  	LuaTools.setFunction(lua, -2, -2, "import", Turbine::turbineImport);

  	lua.push(new HashMap<String, Integer>() {{
      put("Invalid", 0);
      put("English", 2);
      put("EnglishGB", 268435457);
      put("French", 268435459);
      put("German", 268435460);
      put("Russian", 268435463);
    }}, Lua.Conversion.FULL);
    lua.setField(-2, "Language");

    lua.push(new HashMap<String, Integer>() {{
        put("Account", 1);
        put("Server", 2);
        put("Character", 3);
    }}, Lua.Conversion.FULL);
    lua.setField(-2, "DataScope");
    
    lua.push(new HashMap<String, Integer>() {{
        put("Undef", 0);
        put("Error", 1);
        put("Admin", 3);
        put("Standard", 4);
        put("Say", 5);
        put("Tell", 6);
        put("Emote", 7);
        put("Fellowship", 11);
        put("Kinship", 12);
        put("Officer", 13);
        put("Advancement", 14);
        put("Trade", 15);
        put("LFF", 16);
        put("Advice", 17);
        put("OOC", 18);
        put("Regional", 19);
        put("Death", 20);
        put("Quest", 21);
        put("Raid", 23);
        put("Unfiltered", 25);
        put("Roleplay", 27);
        put("UserChat1", 28);
        put("UserChat2", 29);
        put("UserChat3", 30);
        put("UserChat4", 31);
        put("Tribe", 32);
        put("PlayerCombat", 34);
        put("EnemyCombat", 35);
        put("PlayerLoot", 36);
        put("FellowshipLoot", 37);
        put("World", 38);
        put("UserChat5", 39);
        put("UserChat6", 40);
        put("UserChat7", 41);
        put("UserChat8", 42);
    }}, Lua.Conversion.FULL);
    lua.setField(-2, "ChatType");
    
    if ((error = LuaObject.openPackage(lua)) != Lua.LuaError.OK) return error;
    if ((error = Engine.openPackage(lua)) != Lua.LuaError.OK) return error;
    if ((error = LuaPlugin.add(lua)) != Lua.LuaError.OK) return error;
    if ((error = Shell.add(lua)) != Lua.LuaError.OK) return error;
    lua.pop(1); /* pop module */
    return error;
	}
  
  public static int turbineImport(Lua lua) {
  	return turbineImport(lua, Lua51Consts.LUA_ENVIRONINDEX);
  }

  public static int turbineImport(Lua lua, int globalsIndex) {
  	if (LuaTools.isLoadedModule(lua)) {
      return 1;
    }
  	lua.pop(1); /* pop empty library */

  	String libraryName = lua.toString(1);
    //Apartment apartment=LuaTools.optUserdata(OperationHelper.getTable(state,env,valueOf("Apartment")),Apartment.class,null);

    switch (libraryName) {
      case "Turbine.Gameplay":
        if (Gameplay.openPackage(lua, globalsIndex) != Lua.LuaError.OK) return -1;
        if (LuaTools.require(lua, globalsIndex) == -1) {
      		lua.pop(1); /* remove error message */
      		lua.push(true); /* return true */
      	}
        break;
      case "Turbine.UI":
      	if (UI.openPackage(lua, globalsIndex) != Lua.LuaError.OK) return -1;
      	if (LuaTools.require(lua, globalsIndex) == -1) {
      		lua.pop(1); /* remove error message */
      		lua.push(true); /* return true */
      	}
        break;
      case "Turbine.UI.Lotro":
      	if (UiLotro.openPackage(lua, globalsIndex) != Lua.LuaError.OK) return -1;
      	if (LuaTools.require(lua, globalsIndex) == -1) {
      		lua.pop(1); /* remove error message */
      		lua.push(true); /* return true */
      	}
        break;
      default:
      	if (LuaTools.require(lua, globalsIndex) == -1) return -1;
    }

    return 1;
  }
  
  private static int processEvents(Lua lua) {
    ModuleEvent event=(ModuleEvent)lua.get().toJavaObject();
    if ((event!=null)&&(event._args!=null)) {

      switch (event._name) {
        case "loadPlugin": {
          Plugin plugin=(Plugin)event._args[0];
          Apartment apartment=_apartments.get(plugin._apartmentName);
          if (apartment==null) {
            apartment=new Apartment(lua.get("_G"));
            _apartments.put(plugin._apartmentName,apartment);
          }

          LuaValue luaPlugin=LuaObject.findLuaObjectFromObject(plugin);
          if (luaPlugin==null) {
          	lua.getGlobal("_G");
            LuaPlugin.newLuaPlugin(lua, Lua51Consts.LUA_ENVIRONINDEX, plugin);
            lua.setField(-2, "plugin");
            lua.pop(1);
          }

          lua.push(plugin._package);
          turbineImport(lua);
          lua.pop(1);
          break;
        }
        default: {
          LOGGER.debug("Event name: "+event._name);
          // Apartment apartment = (Apartment)event._args[0];
          ((LuaValue)event._args[1]).call(event._args[2],event._args[3]);
          break;
        }
      }
    }
    return 1;
  }
}
