package delta.games.lotro.lua.turbine;

import java.util.Arrays;
import java.util.List;

import delta.games.lotro.lua.turbine.chat.LuaChat;
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
import party.iroiro.luajava.lua51.Lua51Consts;

/**
 * Turbine library for lua scripts.
 * @author MaxThlon
 */
public final class Turbine {
  //private static Logger LOGGER = Logger.getLogger(Turbine.class);
  private static List<String> LOTRO_LIBRARIES_NAME = Arrays.asList("Turbine", "Turbine.Gameplay", "Turbine.UI", "Turbine.UI.Lotro");
  
  /**
   * Initialize lua turbine package for root environment
   * @param lua
   * @return Lua.LuaError.
   */
  public static Lua.LuaError openRootPackage(Lua lua) {
  	Lua.LuaError error;

    lua.register("import", Turbine::luaTurbineImport);
    
    Lua thread = lua.newThread();
    thread.push("Turbine.Enum");
    thread.pushValue(Lua51Consts.LUA_GLOBALSINDEX);
    thread.getGlobal(LuaTools.PCALL_ERR_FUNC_NAME);
    if ((error = require(thread, true, true)) != Lua.LuaError.OK) return error;
    thread.pop(2); /* pop ERR_FUNC, module */
    thread.pushValue(Lua51Consts.LUA_REGISTRYINDEX);
    thread.getField(-2, "Turbine"); /* push Turbine module from globals */
    thread.setField(-2, "Turbine"); /* add it to thread registry */
    thread.pop(3); /* pop "Turbine.Enum", globals , registry  */
    thread = null;
    lua.pop(1); /* pop Thread */
    return error;
  }

  /**
   * Initialize lua turbine package
   * @param lua thread.
   * @param envIndex .
   */
  public static void openPackage(Lua lua, int envIndex) {
  	pushNamespace(lua, envIndex, "Turbine"); /* push namespace */
  	LuaTools.setFunction(
  			lua,
  			LuaTools.relativizeIndex(envIndex, -1),
  			LuaTools.relativizeIndex(envIndex, -1),
  			"require",
  			Turbine::require
  	);
  	LuaTools.setFunction(
  			lua,
  			LuaTools.relativizeIndex(envIndex, -1),
  			LuaTools.relativizeIndex(envIndex, -1),
  			"import",
  			Turbine::luaTurbineImport
  	);
    
    LuaObject.openPackage(lua, LuaTools.relativizeIndex(envIndex, -1));
    Engine.openPackage(lua, LuaTools.relativizeIndex(envIndex, -1));
    LuaPlugin.openPackage(lua, LuaTools.relativizeIndex(envIndex, -1), -1);
    LuaChat.add(lua, LuaTools.relativizeIndex(envIndex, -1), -1);
    Shell.add(lua, LuaTools.relativizeIndex(envIndex, -1));
    lua.pop(1); /* pop namespace */
	}

  /**
   * Push namespace table to stack
   * @param lua Lua thread
   * @param index globals index.
   * @param namespace namespaces list
   */
  public static void pushNamespace(Lua lua, int index, String... namespace) {
  	//LuaTools.logEnvInfos(lua, index, "pushNamespace:" + String.join(".", namespace));
  	LuaTools.pushTables(lua, index, namespace);
  }

  /**
   * Push library env to stack with globals _G
   * @param lua .
   * @param globalsIndex .
   * @param name .
   * @param namespace namespaces list.
   */
  public static void pushfenv(Lua lua, int globalsIndex, String name, String... namespace) {
  	//LuaTools.logEnvInfos(lua, globalsIndex);
  	pushNamespace(lua, globalsIndex, namespace); /* push namespace */
  	lua.createTable(0, 0); /* new env */
    lua.createTable(0, 0); /* new metatable */
    lua.push("__newindex");
    lua.pushValue(-4); /* push namespace */
    lua.rawSet(-3); /* metatable["__newindex"] = namespace */
    lua.push("__index");
    lua.push(Turbine::fenvIndexMetaFunc);
    pushfenvIndexMetaFuncEnv(lua, -5, LuaTools.relativizeIndex(globalsIndex, -5));
    LuaTools.setfenv(lua, -2); /* setfenv to fenvIndexMetaFunc */
    lua.rawSet(-3); /* metatable["__index"] = fenvIndexMetaFunc */
    lua.setMetatable(-2); /* set new env metatable */
    lua.replace(-2); /* replace namespace <- new env */
    lua.push("_G"); /* key for new_env["_G"] */
    LuaTools.pushValueRelative(lua, globalsIndex, -1); /* push globals */
    lua.rawSet(-3); /* new_env["_G"] = globals */
    if (!LOTRO_LIBRARIES_NAME.contains(name)) {
    	LuaTools.setEnvInfos(lua, -1, name);
    }
  }

  /**
   * @param lua .
   * @param namespace .
   */
  public static void pushGlobals(Lua lua, String... namespace) {
  	lua.createTable(0, 0); /* new globals */
  	LuaTools.setEnvInfos(lua, -1, String.join(".", namespace));
    lua.createTable(0, 0); /* new metatable */
    lua.push("__index");
    lua.push(Turbine::fenvIndexMetaFunc);
    pushNamespace(lua, -4, namespace); /* push namespace */
    pushfenvIndexMetaFuncEnv(lua, -1, Lua51Consts.LUA_GLOBALSINDEX);
    LuaTools.setfenv(lua, -3); /* setfenv to fenvIndexMetaFunc */
    lua.pop(1); /* pop namespace */
    lua.rawSet(-3); /* metatable["__index"] = fenvIndexMetaFunc */
    lua.setMetatable(-2); /* set new env metatable */
  	lua.push("_G");
  	lua.pushValue(-2); /* push new globals */
  	lua.rawSet(-3); /* globals["_G"] = globals */
  	
  	/* add package.loaded */
  	LuaTools.pushTables(lua, -1, "package");
  	LuaTools.pushValue(lua, Lua51Consts.LUA_GLOBALSINDEX, "package", "loaders");
  	lua.setField(-2, "loaders");
  	LuaTools.pushTables(lua, -1, "loaded");
  	lua.pop(2); /* pop package, loaded */
  }

  private static void pushfenvIndexMetaFuncEnv(Lua lua, int... indexs) {
  	lua.createTable(indexs.length, 0);
  	int tableIndex = 1;
  	for (int index:indexs) {
  		LuaTools.pushValueRelative(lua, index, -1);
  		lua.rawSetI(-2, tableIndex++);
  	}
  }

  /**
   * __index function for fenv to lookup in tables inside LUA_ENVIRONINDEX
   * @param lua .
   * @return int.
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
  
  /**
   * @param lua .
   * @param envIndex .
   * @param errfunc .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError pushLuaEvent(Lua lua, int envIndex, int errfunc) {
    LuaTools.pushValue(lua, envIndex, "Turbine", "Event");
  	return LuaTools.pCall(lua, envIndex, envIndex, LuaTools.relativizeIndex(errfunc, -1));
  }

  /**
   * @param lua .
   * @return int.
   */
  public static int require(Lua lua) {
  	Lua.LuaError error;

  	lua.pushValue(Lua51Consts.LUA_ENVIRONINDEX);
  	lua.getGlobal(LuaTools.PCALL_ERR_FUNC_NAME);
  	error = require(lua, false, true);
  	return (error == Lua.LuaError.OK)?1:-1;
  }

  /**
   * Internal require function, call with stack beginning with
   *   - Library name (1)
   *   - Globals (2)
   * return
   * 	 if already exist -> module at top of stack
   *   if not found:
   *     isRequired -> throw error
   *     !isRequired -> true at top of stack
   *   if loaded -> module at top of stack
   * @param lua .
   * @param isImport .
   * @param isRequired .
   * @return Lua.LuaError.
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
  	    if ((error = LuaTools.pCall(lua, 1, 1, 3)) != Lua.LuaError.OK) return error;  /* call it */
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
      		LuaTools.libraryNameSplit(library, !(moduleFilename.endsWith("__init__.lua")))
      );
  		LuaTools.setfenv(lua, -2); /* set module fenv */
		} else {
			lua.pushValue(2); /* push Globals */
			LuaTools.setfenv(lua, -2); /* set module fenv */
		}
    lua.pushValue(1); /* pass name as argument to module */
    if ((error = LuaTools.pCall(lua, 1, 1, 3)) != Lua.LuaError.OK) return error; /* run loaded module */
		if (!lua.isNil(-1)) { /* non-nil return? */
			//lua.pushValue(-1); /* duplicate module */
			LuaTools.setLoadedModule(lua, 2); /* _LOADED[name] = returned value */
		}
		return error;
  }
  
  /**
   * Lua import function
   * @param lua .
   * @return int.
   */
  private static int luaTurbineImport(Lua lua) {
  	lua.pushValue(Lua51Consts.LUA_ENVIRONINDEX);
  	lua.getGlobal(LuaTools.PCALL_ERR_FUNC_NAME);
  	return turbineImport(lua);
  }

  /**
   * Internal import function, call with stack containing only
   *   - Library name (1)
   *   - Globals (2)
   * @param lua .
   * @return int.
   */
  public static int turbineImport(Lua lua) {
  	if (LuaTools.isLoadedModule(lua, 2)) {
      return 1;
    }
  	lua.pop(1); /* pop nil */

    switch (lua.toString(1)) {
      case "Turbine.Gameplay":
        Gameplay.openPackage(lua, 2);
        require(lua, true, false);
        break;
      case "Turbine.UI":
      	UI.openPackage(lua, 2);
      	require(lua, true, false);
        break;
      case "Turbine.UI.Lotro":
      	UiLotro.openPackage(lua, 2);
      	require(lua, true, false);
        break;
      case "UI.Swing":
      	LuaComponent.add(lua, 2);
      	require(lua, true, false);
        break;
      default:
      	if (require(lua, true, true) != Lua.LuaError.OK) return -1;
    }

    return 1;
  }
}
