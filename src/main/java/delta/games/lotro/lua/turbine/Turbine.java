package delta.games.lotro.lua.turbine;

import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.log4j.Logger;

import delta.common.framework.module.ModuleEvent;
import delta.games.lotro.client.plugin.Plugin;
import delta.games.lotro.lua.LuaLotro;
import delta.games.lotro.lua.LuaModule;
import delta.games.lotro.lua.LuaModuleImpl;
import delta.games.lotro.lua.turbine.engine.Engine;
import delta.games.lotro.lua.turbine.gameplay.Gameplay;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.turbine.plugin.LuaPlugin;
import delta.games.lotro.lua.turbine.shell.Shell;
import delta.games.lotro.lua.turbine.ui.UI;
import delta.games.lotro.lua.turbine.ui.lotro.UiLotro;
import delta.games.lotro.lua.ui.swing.LuaComponent;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;
import party.iroiro.luajava.lua51.Lua51Consts;
import party.iroiro.luajava.value.LuaValue;

/**
 * Turbine library for lua scripts.
 * @author MaxThlon
 */
public final class Turbine {
  private static Logger LOGGER=Logger.getLogger(Turbine.class);

  /*
   * Initialize root environment
   */
  public static Lua.LuaError openRootPackage(Lua lua) {
  	Lua.LuaError error;

    lua.register("import", Turbine::luaTurbineImport);

    Lua localLua = lua.newThread();
    localLua.push("Turbine.Class");
    localLua.pushValue(Lua51Consts.LUA_GLOBALSINDEX);
    if ((error = require(localLua, true, true)) != Lua.LuaError.OK) return error;
    localLua.pop(3); /* pop module, globals, "Turbine.Class" */
    localLua = null;
    lua.pop(1); /* pop Thread */

    return error;
  }
  
  /*
   * Initialize environment
   */
  @SuppressWarnings("boxing")
  public static Lua.LuaError openPackage(Lua lua) {
  	Lua.LuaError error;
  	pushModule(lua, -1, "Turbine"); /* push module */

  	LuaTools.setFunction(lua, -2, -2, "require", Turbine::require);
  	LuaTools.setFunction(lua, -2, -2, "import", Turbine::luaTurbineImport);

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
  
  /*
   * Push module table to stack
   * @param lua Lua thread
   * @param index globals index.
   * @param modules modules list
   */
  public static void pushModule(Lua lua, int index, String... modules) {
  	//LuaTools.logEnvInfos(lua, index, "pushModule:" + String.join(".", modules));
  	LuaTools.pushTables(lua, index, modules);
  }

  /*
   * Push library env to stack with globals _G
   * @param lua Lua thread
   * @param modules modules list
   */
  public static void pushfenv(Lua lua, int globalsIndex, String name, String... modules) {
  	//LuaTools.logEnvInfos(lua, globalsIndex);
  	pushModule(lua, globalsIndex, modules); /* push module */
  	lua.createTable(0, 0); /* new env */
  	LuaTools.setEnvInfos(lua, -1, name);
    lua.createTable(0, 0); /* new metatable */
    lua.push("__newindex");
    lua.pushValue(-4); /* push module */
    lua.rawSet(-3); /* metatable["__newindex"] = module */
    lua.push("__index");
    lua.push(Turbine::fenvIndexMetaFunc);
    pushfenvIndexMetaFuncEnv(lua, -5, LuaTools.relativizeIndex(globalsIndex, -5));
    LuaTools.setfenv(lua, -2); /* setfenv to fenvIndexMetaFunc */
    lua.rawSet(-3); /* metatable["__index"] = fenvIndexMetaFunc */
    lua.setMetatable(-2); /* set new env metatable */
    lua.replace(-2); /* replace module <- new env */
    lua.push("_G"); /* key for new_env["_G"] */
    LuaTools.pushValueRelative(lua, globalsIndex, -1); /* push globals */
    lua.rawSet(-3); /* new_env["_G"] = globals */
  }

  public static void pushGlobals(Lua lua, String... modules) {
  	lua.createTable(0, 0); /* new globals */
  	LuaTools.setEnvInfos(lua, -1, String.join(".", modules));
    lua.createTable(0, 0); /* new metatable */
    lua.push("__index");
    lua.push(Turbine::fenvIndexMetaFunc);
    pushModule(lua, -4, modules); /* push module */
    pushfenvIndexMetaFuncEnv(lua, -1, Lua51Consts.LUA_GLOBALSINDEX);
    LuaTools.setfenv(lua, -3); /* setfenv to fenvIndexMetaFunc */
    lua.pop(1); /* pop module */
    lua.rawSet(-3); /* metatable["__index"] = fenvIndexMetaFunc */
    lua.setMetatable(-2); /* set new env metatable */
  	lua.push("_G");
  	lua.pushValue(-2); /* push new globals */
  	lua.rawSet(-3); /* globals["_G"] = globals */
  	
  	/* add package.loaded */
  	LuaTools.pushTables(lua, -1, "package", "loaded");
  	lua.pop(1);
  }

  private static void pushfenvIndexMetaFuncEnv(Lua lua, int... indexs) {
  	lua.createTable(indexs.length, 0);
  	int tableIndex = 1;
  	for (int index:indexs) {
  		LuaTools.pushValueRelative(lua, index, -1);
  		lua.rawSetI(-2, tableIndex++);
  	}
  }

  /*
   * __index function for fenv to lookup in module table then _G
   */
  private static int fenvIndexMetaFunc(Lua lua) {
  	lua.pushValue(Lua51Consts.LUA_ENVIRONINDEX); /* push fenvIndexMetaFuncEnv */
  	int len = lua.rawLength(-1) + 1;
  	for (int index = 1; index != len; index ++) {
  		lua.rawGetI(-1, index); /* push table */
  		lua.pushValue(2); /* push key */
    	lua.getTable(-2); /* push table[key] */
    	if (!lua.isNil(-1)) {
    		//LuaTools.logEnvInfos__index(lua, -2, lua.toString(2));
    		return 1;
    	}
    	lua.pop(2); /* pop table, value */
  	}
  	return 0;
  }

  public static int require(Lua lua) {
  	Lua.LuaError error;

  	lua.pushValue(Lua51Consts.LUA_ENVIRONINDEX);
  	error = require(lua, false, true);
  	return (error == Lua.LuaError.OK)?1:-1;
  }

  /*
   * Internal require function, call with stack beginning with
   *   - Library name (1)
   *   - Globals (2)
   * return
   * 	 if already exist -> module at top of stack
   *   if not found:
   *     isRequired -> throw error
   *     !isRequired -> true at top of stack
   *   if loaded -> module at top of stack
   */
  public static Lua.LuaError require(Lua lua, boolean isImport, boolean isRequired) {
  	Lua.LuaError error;
  	if (LuaTools.isLoadedModule(lua, 2)) {
  		return Lua.LuaError.OK;
  	}
		lua.pop(1); /* remove isLoadedLibrary result */
		LuaTools.setLoadedModuleToTrue(lua, 2);
		LuaTools.getloaders(lua, Lua51Consts.LUA_GLOBALSINDEX);
		{
			String errorMessageAccumulator = "";
  		for (int i=1; ; i++) {
  			lua.rawGetI(-1, i);  /* get a loader */
  	    if (lua.isNil(-1)) {
  	    	if (isRequired) { /* If required throw a error */
  	    		lua.error(String.format("module '%s' not found:%s", lua.toString(1), errorMessageAccumulator));
  	    	}
  	    	/* Otherwise */
  	    	lua.pop(2); /* pop loaders, loader */
  	    	lua.push(true);
  	    	return Lua.LuaError.OK;
  	    }
  	    lua.pushValue(1); /* pass library name as argument */
  	    if ((error = lua.pCall(1, 1)) != Lua.LuaError.OK) return error;  /* call it */
  	    if (lua.isFunction(-1))  /* did it find module ? */
  	      break;  /* module loaded successfully */
  	    else if (lua.isString(-1))  /* loader returned error message? */
  	    	errorMessageAccumulator += (String)lua.get().toJavaObject();
  	    else
  	      lua.pop(1);
  		}
		}
		lua.replace(-2); /* replace loaders <- module */

		if (isImport) {
			String library = lua.toString(1);
  		String moduleFilename = LuaTools.getModuleFilename(lua, -1);		
  		pushfenv(
      		lua,
      		2,
      		library,
      		LuaTools.libraryNameSplit(lua, library, !(moduleFilename.endsWith("__init__.lua")))
      );
  		LuaTools.setfenv(lua, -2); /* set module fenv */
		} else {
			lua.pushValue(2); /* push Globals */
			LuaTools.setfenv(lua, -2); /* set module fenv */
		}
    lua.pushValue(1); /* pass name as argument to module */
    if ((error = lua.pCall(1, 1)) != Lua.LuaError.OK) return error; /* run loaded module */
		if (!lua.isNil(-1)) { /* non-nil return? */
			lua.pushValue(-1); /* duplicate module */
			LuaTools.setLoadedModule(lua, 2); /* _LOADED[name] = returned value */
		}
		return error;
  }
  
  /*
   * Lua import function
   */
  private static int luaTurbineImport(Lua lua) {
  	lua.pushValue(Lua51Consts.LUA_ENVIRONINDEX);
  	return turbineImport(lua);
  }

  /*
   * Internal import function, call with stack containing only
   *   - Library name (1)
   *   - Globals (2)
   */
  public static int turbineImport(Lua lua) {
  	if (LuaTools.isLoadedModule(lua, 2)) {
      return 1;
    }
  	lua.pop(1); /* pop nil */

    switch (lua.toString(1)) {
      case "Turbine.Gameplay":
        if (Gameplay.openPackage(lua, 2) != Lua.LuaError.OK) return -1;
        require(lua, true, false);
        break;
      case "Turbine.UI":
      	if (UI.openPackage(lua, 2) != Lua.LuaError.OK) return -1;
      	require(lua, true, false);
        break;
      case "Turbine.UI.Lotro":
      	if (UiLotro.openPackage(lua, 2) != Lua.LuaError.OK) return -1;
      	require(lua, true, false);
        break;
      case "UI.Swing":
      	if (LuaComponent.add(lua, 2) != Lua.LuaError.OK) return -1;
      	require(lua, true, false);
        break;
      default:
      	if (require(lua, true, true) != Lua.LuaError.OK) return -1;
    }

    return 1;
  }

  public static Lua.LuaError processEvents(Lua lua, LuaModuleImpl luaModuleImpl, ModuleEvent event) {
  	Lua.LuaError error;  	
  	LuaLotro luaLotro = (LuaLotro)luaModuleImpl;

    switch (event._name) {
    	case "load": {
    		Plugin plugin=(Plugin)event._args[0];
    		Apartment apartment = luaLotro.pushApartmentThread(lua, plugin);
    		Lua thread = apartment.getThread();

    		pushGlobals(thread, LuaTools.libraryNameSplit(lua, plugin._package, true));
    		error = Turbine.openPackage(thread);
    		if (error != Lua.LuaError.OK) {
    			LuaTools.handleLuaResult(thread, error);
    			return error;
    		}

    		error = thread.load(
    				LuaTools.loadBuffer(Paths.get("Turbine", "EventThread.lua"), LuaModule.class),
    				"Turbine.EventThread"
        );
  			if (error != Lua.LuaError.OK) {
    			LuaTools.handleLuaResult(thread, error);
    			return error;
    		}
    		thread.pushValue(-2); /* globals */
    		LuaTools.setfenv(thread, -2); /* set EventThread globals */
        thread.replace(-2); /* globals <- chunk */
        error = thread.pCall(0, 1); /* chunk <- EventThread */
        if (error != Lua.LuaError.OK) {
    			LuaTools.handleLuaResult(thread, error);
    			return error;
    		}
        thread.push(plugin._package);
        error = thread.resume(1);
        if (error != Lua.LuaError.YIELD) {
    			LuaTools.handleLuaResult(thread, error);
    			return error;
    		}
        lua.pop(1); /* pop thread */
    		break;
    	}
    	case "processCommand": {
    		LOGGER.debug("Event name: "+event._name);

    		String command = (String)event._args[0];
    		String[] splits = command.split("/", 1);
    		command = (splits.length == 1)?splits[0]:((splits.length == 2)?splits[1]:null);
    		if (command != null) {
    			splits = command.split(" ", 1);
    			String commandName = (splits.length != 0)?splits[0]:null;
    			String argumentText = (splits.length == 2)?splits[1]:null;
    	    
    			LuaValue luaCommand = luaLotro.findCommand(commandName);
	    		if (luaCommand != null) {
	    			Lua thread = luaCommand.state();
	    			if (thread.status() != Lua.LuaError.YIELD) {
	      			LuaTools.handleLuaResult(thread, thread.status());
	      			return thread.status();
	      		}
	    			
	    			/* Set stack to: ShellCommand.Execute, ShellCommand, argumentText  */
	    			thread.push(luaCommand, Conversion.NONE);
	    			thread.pushValue(-1);
	    			thread.getField(-1, "Execute");
	    			thread.replace(-3);
	    			thread.push(argumentText);
	    			error = thread.resume(3);
	          if (error != Lua.LuaError.YIELD) {
	      			LuaTools.handleLuaResult(thread, error);
	      			return error;
	      		}
	    		}
    		}
    	}
      default: {
        LOGGER.debug("Event name: "+event._name);
        Lua thread = ((LuaValue)event._args[0]).state();
        if (thread.status() != Lua.LuaError.YIELD) {
    			LuaTools.handleLuaResult(thread, thread.status());
    			return thread.status();
    		}

        for (Object luaValue:event._args) {
        	thread.push(luaValue, Conversion.NONE);
        }
        error = thread.resume(event._args.length);
        if (error != Lua.LuaError.YIELD) {
    			LuaTools.handleLuaResult(thread, error);
    			return error;
    		}
        break;
      }
    }
    return error;
  }
}
