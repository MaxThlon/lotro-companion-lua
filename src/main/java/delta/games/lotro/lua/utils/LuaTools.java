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

import delta.common.framework.console.ConsoleManager;
import delta.common.framework.module.ModuleEvent;
import delta.common.framework.module.ModuleExecutor;
import delta.games.lotro.lua.LuaModule;
import delta.games.lotro.lua.LuaModuleImpl;
import delta.games.lotro.utils.events.EventsManager;
import party.iroiro.luajava.ClassPathLoader.BufferOutputStream;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;
import party.iroiro.luajava.lua51.Lua51Natives;
import party.iroiro.luajava.value.LuaValue;

/**
 * LuaTools library for lua scripts.
 * @author MaxThlon
 */
public final class LuaTools {
	private static Logger LOGGER = Logger.getLogger(LuaTools.class);
  private static LuaValue _getModuleFilename;
  //private static LuaValue _mobdebug;
  
  /*
   * Initialize root environment
   */
  public static Lua.LuaError openRootPackage(Lua lua) {
  	Lua.LuaError error;
  	lua.register("print", LuaTools::print);
  	
  	if ((error = lua.load("local library = ... return debug.getinfo(library, 'S').source")) != Lua.LuaError.OK)
  		return error;
    _getModuleFilename = lua.get();

    /*if ((error = lua.run("return require('mobdebug')")) != Lua.LuaError.OK) return error;
    _mobdebug = lua.get();*/

    return error;
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
  
  public static void setJavaLuaModuleImpl(Lua lua, LuaModuleImpl luaModuleImpl) {
  	lua.push(luaModuleImpl, Conversion.NONE);
    if (lua.getLuaNative() instanceof Lua51Natives) {
      new Lua51Natives() {
        {
          lua.setField(getRegistryIndex(), "LuaModule");
        }
      };
    }
  }
  
  public static LuaModuleImpl getJavaLuaModuleImpl(Lua lua) {
    if (lua.getLuaNative() instanceof Lua51Natives) {
      new Lua51Natives() {
        {
          lua.getField(getRegistryIndex(), "LuaModule");
        }
      };
    }
    return (LuaModuleImpl)lua.get().toJavaObject();
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
  public static void pushTables(Lua lua, int index, String... names) {
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

  /*
   * Test if lua library is loaded.
   */
  public static boolean isLoadedModule(Lua lua, int globalsIndex) {
    getLoadedTable(lua, globalsIndex);
    lua.pushValue(1); /* library name */
    lua.getTable(-2); /* LoadedTable[libraryName] */
    lua.replace(-2);  /* replace stack LoadedTable <- module */
    return !lua.isNil(-1);
  }

  /*
   * Set true as module inside lua loaded table.
   */
  public static void setLoadedModuleToTrue(Lua lua, int globalsIndex) {
  	lua.push(true);
  	setLoadedModule(lua, globalsIndex);
  	lua.pop(1); /* pop true */
  }

  /*
   * Set top stack value as module inside lua loaded table.
   */
  public static void setLoadedModule(Lua lua, int globalsIndex) {
    getLoadedTable(lua, globalsIndex);
    lua.pushValue(1); /* library name */
    lua.getTable(-2); /* loaded library */
    Lua.LuaType type = lua.type(-1);
    lua.pop(1); /* pop module */
    if ((type == Lua.LuaType.NIL) || (type == Lua.LuaType.BOOLEAN)) { /* no loaded library */
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

  public static void getloaders(Lua lua, int globalsIndex) {
  	pushTables(lua, globalsIndex, "package", "loaders");
  }

  public static void getLoadedTable(Lua lua, int globalsIndex) {
  	pushTables(lua, globalsIndex, "package", "loaded");
  }

  public static void handleLuaResult(Lua lua, Lua.LuaError error) {
  	switch (error) {
      case OK:
      case YIELD:
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
  
  public static void invokeConsoleEvent(Lua lua, String message) {
  	EventsManager.invokeEvent(new  ModuleEvent(
    		ModuleExecutor.ExecutorEvent.EXECUTE,
    		ConsoleManager.getInstance().getModuleUuid(),
    		ModuleExecutor.ExecutorEvent.EXECUTE.name(),
    		new Object[] { message }
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
  	lua.push("Name");
  	lua.push(name);
  	lua.rawSet(-3);
  	lua.rawSet(relativizeIndex(index, -2));
  }
  
  public static void logEnvInfos(Lua lua, int index) {
  	logEnvInfos(lua, index, null);
  }
  
  static final Set<String> baseFuncs = new HashSet<String>() {{
  	add("math"); add("tonumber"); add("string"); add("table"); add("pairs");
  	add("getfenv"); add("setfenv"); add("getmetatable"); add("setmetatable");
  	add("error");
  	//add("type"); 
  	add("static_class"); add("class"); add("final_class"); add("import");
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
    	} else {
    		LOGGER.error("Module: " + ((message != null)?" " + message:""));
    	}
    	lua.pop(1);
  	}
  }
  
  public static String getModuleFilename(Lua lua, int index) {
  	String filename = "Unknown";
  	lua.push(_getModuleFilename, Conversion.NONE);
  	pushValueRelative(lua, index, -1); /* push module */
  	if (lua.pCall(1, 1) == Lua.LuaError.OK) { /* push module filename */
  		filename = lua.toString(-1);
  	}
  	lua.pop(1); /* pop result */
  	
  	return filename;
  }
  
  public static int print(Lua lua) {
  	invokeConsoleEvent(lua, lua.toString(1));
  	return 1;
  }
  /*
  public static void mobdebugStart() {
  	_mobdebug.get("start").call();
  }*/
}
