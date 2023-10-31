package delta.games.lotro.lua.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import delta.common.framework.module.ModuleEvent;
import delta.common.framework.module.ModuleExecutor;
import delta.games.lotro.lua.LuaModule;
import delta.games.lotro.utils.events.EventsManager;
import party.iroiro.luajava.ClassPathLoader.BufferOutputStream;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;
import party.iroiro.luajava.lua51.Lua51Consts;
import party.iroiro.luajava.lua51.Lua51Natives;
import party.iroiro.luajava.value.LuaValue;

/**
 * LuaTools library for lua scripts.
 * @author MaxThlon
 */
public final class LuaTools {
	private static Logger LOGGER = Logger.getLogger(LuaTools.class);
  private static LuaValue _getModuleFilename;

  /*
   * Initialize root environment
   */
  public static Lua.LuaError openRootPackage(Lua lua) {
  	Lua.LuaError error;
    lua.register("require", LuaTools::require);

    error = lua.load("local library = ... return debug.getinfo(library, 'S').source");
    if (error != Lua.LuaError.OK) return error;
    _getModuleFilename = lua.get();
    
    return error;
  }
  
  /*
   * Initialize environment
   */
  public static void openPackage(Lua lua) {
  	setFunction(lua, -1, -1, "require", LuaTools::require);
  }

  public static Buffer loadBuffer(Path ressource) {
    return loadBuffer(ressource, LuaModule.class);
  }
  
  public static Buffer loadBuffer(Path ressource, Class<?> clazz) {
  	return loadBuffer(Objects.requireNonNull(URLToolsLua.getFromClassPathAsStream(ressource.toString(), clazz)));
  }
  
  public static Buffer loadBuffer(File script) throws FileNotFoundException {
  	return loadBuffer(new FileInputStream(script));
  }
  
  public static Buffer loadBuffer(InputStream inputStream) {
    try {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] bytes = new byte[4096];
        int i;
        do {
            i = inputStream.read(bytes);
            if (i != -1) {
                output.write(bytes, 0, i);
            }
        } while (i != -1);
        ByteBuffer buffer = ByteBuffer.allocateDirect(output.size());
        output.writeTo(new BufferOutputStream(buffer));
        buffer.flip();
        return buffer;
    } catch (Exception e) {
    	LOGGER.error(e);
    	return null;
    }
  }
  
  public static void setJavaModuleUuid(Lua lua, UUID uuid) {
  	lua.push(uuid, Conversion.NONE);
    if (lua.getLuaNative() instanceof Lua51Natives) {
      new Lua51Natives() {
        {
          lua.setField(getRegistryIndex(), "LuaModuleUuid");
        }
      };
    }
  }
  
  public static UUID getJavaModuleUuid(Lua lua) {
    if (lua.getLuaNative() instanceof Lua51Natives) {
      new Lua51Natives() {
        {
          lua.getField(getRegistryIndex(), "LuaModuleUuid");
        }
      };
    }
    return (UUID)lua.get().toJavaObject();
  }

  public static int relativizeIndex(int index, int decal) {
  	return ((index > 0) || (index < -9999))? index : (index + decal);
  }
  
  public static void pushValueRelative(Lua lua, int index, int decal) {
  	lua.pushValue(relativizeIndex(index, decal));
  }
  
  public static void setFunction(Lua lua, int index, int envIndex, String name, JFunction function) {
  	lua.push(function);
  	pushValueRelative(lua, envIndex, -1);
  	setfenv(lua, -2);
  	lua.setField(relativizeIndex(index, -1), name);
  }
  
  public static int invokeAndWait(Lua lua, final Runnable doRun) {
  	try {
  		SwingUtilities.invokeAndWait(doRun);
  	} catch (InvocationTargetException | InterruptedException exception) {
  		return handleJavaError(lua, exception);
  	}
  	return 1;
  }

  /*
   * Push value to stack
   * Retrieve value from top in stack
   * @param lua Lua thread
   * @param names names list
   */
  public static Lua.LuaError pushValue(Lua lua, int index, String... names) {
  	lua.pushValue(index);
  	for (String name:names) {
  		if (lua.isNil(-1)) {
  			lua.push("pushValue: attempt to index a nil value");
  			return Lua.LuaError.RUNTIME;
  		}
  		lua.getField(-1, name);
  		lua.replace(-2);
    }
  	return Lua.LuaError.OK;
  }
  
  /*
   * Push tables to stack
   * Retrieve table from tree at top of stack or create a tree of table
   * @param lua Lua thread
   * @param names names list
   */
  private static void pushTables(Lua lua, int index, String... names) {
  	lua.pushValue(index); /* push parent_table */
    for (String name:names) {
    	lua.push(name);
      lua.rawGet(-2); /* push table */
      if (!lua.isTable(-1)) { /* if found value is not a table create an new one */
        lua.pop(1); /* pop nil */
        lua.createTable(0, 0); /* new table */
        lua.push(name);
        lua.pushValue(-2); /* re_push table */
        lua.rawSet(-4); /* parent_table[name] = table */
      }
      lua.replace(-2); /* replace parent_table <- table */
    }
  }

  public static void setfenv(Lua lua, int index) {
    if (lua.getLuaNative() instanceof Lua51Natives) {
      new Lua51Natives() {
        {
          lua_setfenv(lua.getPointer(), index);
        }
      };
    }
  }
  
  public static boolean isLoadedModule(Lua lua) {
    getLoadedTable(lua);
    lua.pushValue(1); /* library name */
    lua.getTable(-2); /* LoadedTable[libraryName] */
    lua.replace(-2);  /* replace stack LoadedTable <- module */
    return !lua.isNil(-1);
  }

  /*
   * Set true as module inside lua loaded table
   */
  public static void setLoadedModuleToTrue(Lua lua) {
  	lua.push(true);
  	setLoadedModule(lua);
  	lua.pop(1); /* pop true */
  }

  /*
   * Set top stack value as module inside lua loaded table
   */
  public static void setLoadedModule(Lua lua) {
    getLoadedTable(lua);
    lua.pushValue(1); /* library name */
    lua.getTable(-2); /* loaded library */
    if (lua.get().type() == Lua.LuaType.NIL) { /* no loaded library */
    	lua.pushValue(1); /* library name */
    	lua.pushValue(-3); /* library value from caller */
    	lua.setTable(-3); /* LoadedTable[libraryName] = library value from caller */
    }
    lua.pop(1); /* pop LoadedTable */
  }
  
  /*
   * Split library name to array of module
   */
  public static String[] libraryNameSplit(Lua lua, String libraryName, boolean removelast) {
  	String[] splits = libraryName.split("\\.");
  	if (removelast) {
      splits = Arrays.copyOfRange(splits, 0, splits.length-1);
    }
  	return splits;
  }

  /*
   * Push module table to stack
   * @param lua Lua thread
   * @param index globals index.
   * @param modules modules list
   */
  public static void pushModule(Lua lua, int index, String... modules) {
  	//logEnvInfos(lua, index, "pushModule:" + String.join(".", modules));
  	pushTables(lua, index, modules);
  }
  
  /*
   * Push library env to stack with globals _G
   * @param lua Lua thread
   * @param modules modules list
   */
  public static void pushfenv(Lua lua, int globalsIndex, String name, String... modules) {
  	//logEnvInfos(lua, globalsIndex);
  	pushModule(lua, globalsIndex, modules); /* push module */
  	lua.createTable(0, 0); /* new env */
  	LuaTools.setEnvInfos(lua, -1, name);
    lua.createTable(0, 0); /* new metatable */
    lua.push("__newindex");
    lua.pushValue(-4); /* push module */
    lua.rawSet(-3); /* metatable["__newindex"] = module */
    lua.push("__index");
    lua.push(LuaTools::fenvIndexMetaFunc);
    pushfenvIndexMetaFuncEnv(lua, -5, relativizeIndex(globalsIndex, -5));
    LuaTools.setfenv(lua, -2); /* setfenv to fenvIndexMetaFunc */
    lua.rawSet(-3); /* metatable["__index"] = fenvIndexMetaFunc */
    lua.setMetatable(-2); /* set new env metatable */
    lua.replace(-2); /* replace module <- new env */
    lua.push("_G"); /* key for new_env["_G"] */
    pushValueRelative(lua, globalsIndex, -1); /* push globals */
    lua.rawSet(-3); /* new_env["_G"] = globals */
  }

  public static void pushGlobals(Lua lua, String name, String... modules) {
  	lua.createTable(0, 0); /* new globals */
  	LuaTools.setEnvInfos(lua, -1, name);
    lua.createTable(0, 0); /* new metatable */
    lua.push("__index");
    lua.push(LuaTools::fenvIndexMetaFunc);
    pushModule(lua, -4, modules); /* push module */
    pushfenvIndexMetaFuncEnv(lua, -1, Lua51Consts.LUA_GLOBALSINDEX);
    LuaTools.setfenv(lua, -3); /* setfenv to fenvIndexMetaFunc */
    lua.pop(1); /* pop module */
    lua.rawSet(-3); /* metatable["__index"] = fenvIndexMetaFunc */
    lua.setMetatable(-2); /* set new env metatable */
  	lua.push("_G");
  	lua.pushValue(-2); /* push new globals */
  	lua.rawSet(-3); /* globals["_G"] = globals */
  }

  private static void pushfenvIndexMetaFuncEnv(Lua lua, int... indexs) {
  	lua.createTable(indexs.length, 0);
  	int tableIndex = 1;
  	for (int index:indexs) {
  		pushValueRelative(lua, index, -1);
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
    		//logEnvInfos__index(lua, -2, lua.toString(2));
    		return 1;
    	}
    	lua.pop(2); /* pop table, value */
  	}
  	return 0;
  }
  
  public static void getLoadedTable(Lua lua) {
    if (lua.getLuaNative() instanceof Lua51Natives) {
      new Lua51Natives() {
        {
          lua.getField(getRegistryIndex(), "_LOADED");
        }
      };
    }
  }
  
  public static void getloaders(Lua lua) {
    lua.getGlobal("package");
    lua.getField(-1, "loaders");
    lua.replace(-2);
  }
  
  public static int require(Lua lua) {
  	return require(lua, Lua51Consts.LUA_ENVIRONINDEX); /* This env is the globals */
  }

  public static int require(Lua lua, int globalsIndex) {
  	if (isLoadedModule(lua)) return 1;
		lua.pop(1); /* remove isLoadedLibrary result */
		setLoadedModuleToTrue(lua);
		getloaders(lua);
		{
			String errorMessageAccumulator = "";
  		for (int i=1; ; i++) {
  			lua.rawGetI(-1, i);  /* get a loader */
  	    if (lua.isNil(-1))
  	    	lua.error(String.format("module '%s' not found:%s", lua.toString(1), errorMessageAccumulator));
  	    lua.pushValue(1); /* pass library name as argument */
  	    if (lua.pCall(1, 1) != Lua.LuaError.OK) return -1;  /* call it */
  	    if (lua.isFunction(-1))  /* did it find module ? */
  	      break;  /* module loaded successfully */
  	    else if (lua.isString(-1))  /* loader returned error message? */
  	    	errorMessageAccumulator += (String)lua.get().toJavaObject();
  	    else
  	      lua.pop(1);
  		}
		}
		lua.replace(-2); /* replace loaders <- module */

		lua.push(_getModuleFilename, Conversion.NONE);
    lua.pushValue(-2); /* get loaded module */
    if (lua.pCall(1, 1) != Lua.LuaError.OK) return -1; /* get loaded module filename */
    String moduleFilename = ((String)lua.get().toJavaObject());
		pushfenv(
    		lua,
    		relativizeIndex(globalsIndex, -1),
    		lua.toString(1),
    		libraryNameSplit(lua, lua.toString(1), !(moduleFilename.endsWith("__init__.lua")))
    );
    setfenv(lua, -2); /* set module fenv */
    lua.pushValue(1); /* pass name as argument to module */
    if (lua.pCall(1, 1) != Lua.LuaError.OK) return -1; /* run loaded module */
		if (!lua.isNil(-1)) { /* non-nil return? */
			lua.pushValue(-1); /* duplicate module */
			setLoadedModule(lua); /* _LOADED[name] = returned value */
		}
		return 1;
  }

  public static void handleLuaResult(Lua lua, Lua.LuaError error) {
  	switch (error) {
      case OK:
      	break;
      case YIELD:
      	EventsManager.invokeEvent(new  ModuleEvent(
        		ModuleExecutor.ExecutorEvent.ERROR,
        		getJavaModuleUuid(lua),
        		Lua.LuaError.YIELD.name(),
        		new Object[] { error }
        ));
        break;
      default: {
      	invokeErrorEvent(lua, error);
      }
    }
  }
  
  private static List<Object> handleLuaError(Lua lua, Lua.LuaError error) {
  	List<Object> values = new ArrayList<Object>();
  	values.add(error);

    if (lua.getTop() != 0 && lua.isString(-1)) {
    	values.add(lua.toString(-1));
    }
    lua.setTop(0);
    Throwable javaError = lua.getJavaError();
    if (javaError != null) {
    	values.add(javaError);
    	lua.error((Throwable) null);
    }
    return values;
  }
  
  public static int handleJavaError(Lua lua, Exception exception) {
  	lua.push(exception.toString());
  	return -1;
  }
  
  public static void throwLuaError(Lua lua, Lua.LuaError error) throws Exception {
  	List<Object> args = handleLuaError(lua, error);
  	String message = null;
  	Throwable cause = null;
  	for (Object arg:args) {
  		if (arg instanceof String) message = (String)arg;
  		else if (arg instanceof Throwable) cause = (Throwable)arg;
  	}
  	throw new Exception(message, cause);
  }
  
  public static void invokeErrorEvent(Lua lua, Lua.LuaError error) {
  	EventsManager.invokeEvent(new  ModuleEvent(
    		ModuleExecutor.ExecutorEvent.ERROR,
    		getJavaModuleUuid(lua),
    		ModuleExecutor.ExecutorEvent.ERROR.name(),
    		handleLuaError(lua, error).toArray()
    ));
  }

  public static void invokeEvent(Lua lua, String name, Object[] args) {
  	EventsManager.invokeEvent(new ModuleEvent(
  			ModuleExecutor.ExecutorEvent.EXECUTE,
    		getJavaModuleUuid(lua),
    		name,
    		args
    ));
  }
  
  public static void setEnvInfos(Lua lua, int index, String name) {
  	lua.push("_");
  	lua.createTable(0, 0);
  	lua.push(name);
  	lua.setField(-2, "Name");
  	lua.rawSet(relativizeIndex(index, -2));
  }
  
  public static void logEnvInfos(Lua lua, int index) {
  	logEnvInfos(lua, index, null);
  }
  
  static final Set<String> baseFuncs = new HashSet<String>() {{
  	add("math"); add("tonumber"); add("string"); add("table"); add("pairs");
  	add("getfenv"); add("setfenv"); add("getmetatable"); add("setmetatable");
  	add("error");
  	add("type"); add("static_class"); add("class"); add("final_class"); add("import");
  }};
  
  public static void logEnvInfos__index(Lua lua, int index, @Nullable String value) {
  	if (!baseFuncs.contains(value)) {
  		logEnvInfos(lua, index, "__index: " + value);
  	}
  }

  public static void logEnvInfos(Lua lua, int index, @Nullable String message) {
  	if (lua.isTable(index)) {
    	lua.push("_");
    	lua.rawGet(relativizeIndex(index, -1));
    	if (lua.isTable(-1)) {
    		lua.getField(-1, "Name");
    		LOGGER.error("Env Name: " + lua.toString(-1) + ((message != null)?" " + message:""));
    		lua.pop(1);
    	}
    	lua.pop(1);
  	}
  }
}
