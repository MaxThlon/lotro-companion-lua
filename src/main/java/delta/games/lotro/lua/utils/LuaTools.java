package delta.games.lotro.lua.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
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
import javax.xml.ws.Holder;

import org.apache.log4j.Logger;

import delta.common.framework.console.ConsoleManager;
import delta.common.framework.console.ConsoleModule;
import delta.common.framework.console.command.ConsoleCommand;
import delta.common.framework.lua.LuaModule;
import delta.common.framework.lua.command.LuaMTCEvent;
import delta.common.framework.lua.event.LuaModuleEvent;
import delta.common.framework.module.ModuleExecutor;
import delta.common.framework.module.ModuleManager;
import delta.common.framework.module.command.ModuleExecutorCommand;
import party.iroiro.luajava.ClassPathLoader.BufferOutputStream;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;
import party.iroiro.luajava.lua51.Lua51;
import party.iroiro.luajava.lua51.Lua51Consts;
import party.iroiro.luajava.lua51.Lua51Natives;
import party.iroiro.luajava.value.LuaValue;

/**
 * LuaTools library for lua scripts.
 * @author MaxThlon
 */
public final class LuaTools {
	/**
	 * Global registry key for threads
	 */
	public static String REGISTRY_THREAD_NAME = "__RegistryThread";
  /**
   * Global registry key for err func.
   */
  public static final String PCALL_ERR_FUNC_NAME = "pCallErrfunc";

	private static Logger LOGGER = Logger.getLogger(LuaTools.class);
  private static LuaValue _getModuleFilename;
  //private static LuaValue _mobdebug;
  
  /**
   * Initialize root environment
   * @param lua .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError openRootPackage(Lua lua) {
  	Lua.LuaError error;
  	lua.register("pCallErrfunc", LuaTools::pCallErrfunc);
  	lua.register("print", LuaTools::print);
  	if ((error = lua.load("local library = ... return debug.getinfo(library, 'S').source")) != Lua.LuaError.OK)
  		return error;
    _getModuleFilename = lua.get();

    if ((error = lua.run("metalua_compiler = require 'metalua.compiler'.new()")) != Lua.LuaError.OK)
  		return error;
		
    /*if ((error = lua.run("return require('mobdebug')")) != Lua.LuaError.OK) return error;
    _mobdebug = lua.get();*/

    return error;
  }

  /**
   * @param ressource .
   * @return a Buffer.
   */
  public static Buffer loadBuffer(Path ressource) {
    return loadBuffer(ressource, LuaModule.class);
  }
  
  /**
   * @param ressource .
   * @param clazz .
   * @return a Buffer.
   */
  public static Buffer loadBuffer(Path ressource, Class<?> clazz) {
  	return loadBuffer(Objects.requireNonNull(URLToolsLua.getFromClassPathAsStream(ressource.toString(), clazz)));
  }
  
  /**
   * @param script .
   * @return a Buffer.
   * @throws FileNotFoundException 
   */
  public static Buffer loadBuffer(File script) throws FileNotFoundException {
  	return loadBuffer(new FileInputStream(script));
  }
  
  /**
   * @param inputStream .
   * @return a Buffer.
   */
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
  
  /**
   * @param lua .
   * @param writer .
   * @param index .
   * @throws IOException 
   */
  public static void serialize(Lua lua, Writer writer, int index) throws IOException {
  	serialize(lua,writer, "", index);
  }

  /**
   * @param lua .
   * @param writer .
   * @param indent .
   * @param index .
   * @throws IOException 
   */
  public static void serialize(Lua lua, Writer writer, String indent, int index) throws IOException {
  	switch (lua.type(index)) {
			case BOOLEAN:
				writer.write(lua.toBoolean(index)?"true":"false");
				break;
			case NIL:
				writer.write("nil");
				break;
			case NUMBER:
				writer.write(Double.toString(lua.toNumber(index)));
				break;
			case STRING:
				 writer.write("\"" + lua.toString(index) + "\"");
				break;
			case TABLE: {
				writer.write(System.lineSeparator() + indent + "{" + System.lineSeparator());
				String localIndent = indent + "\t";
				boolean next = false;
				lua.pushNil();
				while (lua.next(relativizeIndex(index, -1)) != 0) {
					if (next) writer.write("," + System.lineSeparator());
					writer.write(localIndent + "[");
					next = true;
          serialize(lua, writer, localIndent, -2);
          writer.write("] = ");
          serialize(lua, writer, localIndent, -1);
          lua.pop(1); /* pop value */
        }
				writer.write(System.lineSeparator() + indent + "}");
				break;
			}
			default:
				writer.write(lua.type(index).name());
				break;
		}
  }
  
  /**
   * @param lua .
   * @return a {@link delta.common.framework.module.Module} uuid.
   */
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
  
  /**
   * Set to globals[LuaModuleUuid] a {@link delta.common.framework.module.Module} uuid.
   * @param lua
   * @param uuid
   */
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
  
  /**
   * @param lua .
   * @return LuaModule.
   */
  public static LuaModule getJavaLuaModule(Lua lua) {
    if (lua.getLuaNative() instanceof Lua51Natives) {
      new Lua51Natives() {
        {
          lua.getField(getRegistryIndex(), "LuaModule");
        }
      };
    }
    return (LuaModule)lua.get().toJavaObject();
  }
  
  /**
   * @param lua .
   * @param luaModule .
   */
  public static void setJavaLuaModule(Lua lua, LuaModule luaModule) {
  	lua.push(luaModule, Conversion.NONE);
    if (lua.getLuaNative() instanceof Lua51Natives) {
      new Lua51Natives() {
        {
          lua.setField(getRegistryIndex(), "LuaModule");
        }
      };
    }
  }

	/**
   * Pushes onto the stack the RegistryObject table
	 * @param lua .
   */
  private static void pushRegistryThread(Lua lua) {
    if (lua.getLuaNative() instanceof Lua51Natives) {
      new Lua51Natives() {
        {
          lua.getField(getRegistryIndex(), REGISTRY_THREAD_NAME);
          if (lua.isNil(-1)) { /* No registry */
          	lua.pop(1);
          	lua.createTable(0, 0); /* Create one */
          	lua.createTable(0, 0); /* new metatable */
          	lua.push("k");
          	lua.setField(-2, "__mode"); /* with weak keys */
          	lua.setMetatable(-2);
          	lua.pushValue(-1); /* push duplicate new table for registry */
          	lua.setField(getRegistryIndex(), REGISTRY_THREAD_NAME); /* Set it to registry */
          }
        }
      };
    }
  }
  
  /**
   * @param lua .
   * @return a {@link Lua} uuid.
   */
  public static UUID getJavaThreadUuid(Lua lua) {
  	pushRegistryThread(lua);
  	lua.pushThread();
  	lua.rawGet(-2);
  	UUID uuid = (UUID)lua.toJavaObject(-1);
  	lua.pop(2); /* pop RegistryThread, uuid */
  	return uuid;
  }
  
  /**
   * Set a {@link Lua} uuid.
   * @param lua .
   * @param uuid .
   */
  public static void setJavaThreadUuid(Lua lua, UUID uuid) {
  	pushRegistryThread(lua);
  	lua.pushThread();
  	lua.push(uuid, Conversion.NONE);
  	lua.rawSet(-3);
  	lua.pop(1); /* pop RegistryThread */
  }

  /**
   * Compute a stack index with decal to a modified stack index,
   * keep positive and system index as it,
   * decrement negative indexes with decal (decal must be an negative value)
   * @param index .
   * @param decal a negative value corresponding of pushed elements.
   * @return int.
   */
  public static int relativizeIndex(int index, int decal) {
  	return ((index > 0) || (index < -9999))? index : (index + decal);
  }
  
  /**
   * Push on stack a value with relative position from decal {@link #relativizeIndex}.
   * @param lua .
   * @param index .
   * @param decal a negative value corresponding of pushed elements.
   */
  public static void pushValueRelative(Lua lua, int index, int decal) {
  	lua.pushValue(relativizeIndex(index, decal));
  }
  
  /**
   * Push enum class on stack.
   * @param lua .
   * @return .
   */
  public static Lua.LuaError pushEnum(Lua lua) {
    Lua.LuaError error;
    lua.getGlobal("enum");
    error = lua.pCall(0, 1);
    return error;
  }

  /**
   * Push a inherited class on stack.
   * @param lua .
   * @param errfunc .
   * @param parentClassName .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError pushClass(Lua lua, int errfunc, @Nullable String... parentClassName) {
    Lua.LuaError error;
    lua.getGlobal("class");
    if ((parentClassName != null) && (parentClassName.length != 0)) {
    	if ((error = LuaTools.pushValue(lua, Lua51Consts.LUA_GLOBALSINDEX, parentClassName)) != Lua.LuaError.OK) {
    		return error;
    	}
    	error = pCall(lua, 1, 1, relativizeIndex(errfunc, -2));
    } else {
    	error = pCall(lua, 0, 1, relativizeIndex(errfunc, -1));
    }
    return error;
  }
  
  /**
   * Set a function to a class instance or array.
   * @param lua .
   * @param index .
   * @param envIndex .
   * @param name .
   * @param function .
   */
  public static void setFunction(Lua lua, int index, int envIndex, String name, JFunction function) {
  	lua.push(function);
  	pushValueRelative(lua, envIndex, -1);
  	setfenv(lua, -2);
  	lua.setField(relativizeIndex(index, -1), name);
  }

  /**
   * Error func used when calling {@link #pCall}.
   * @param lua .
   * @return int.
   */
  public static int pCallErrfunc(Lua lua) {
  	invokeErrorEvent(lua, Lua.LuaError.RUNTIME);
  	return 1;
  }

  /**
   * pCall with error function.
   * @param lua .
   * @param nargs .
   * @param nresults .
   * @param errfunc .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError pCall(Lua lua, int nargs, int nresults, int errfunc) {
  	Holder<Lua.LuaError> error = new Holder<Lua.LuaError>();
  	if (lua.getLuaNative() instanceof Lua51Natives) {
      new Lua51Natives() {{
      	error.value = ((Lua51)lua).convertError(lua_pcall(lua.getPointer(), nargs, nresults, errfunc));
			}};
    }
  	return error.value;
  }
  
  /**
   * see {@link SwingUtilities#invokeAndWait}
   * @param lua .
   * @param doRun .
   * @return int.
   */
  public static int invokeAndWait(Lua lua, final Runnable doRun) {
  	try {
  		SwingUtilities.invokeAndWait(doRun);
  	} catch (InvocationTargetException | InterruptedException exception) {
  		return handleJavaError(lua, exception);
  	}
  	return 1;
  }

  /**
   * Push value to stack
   * Retrieve value from top in stack
   * @param lua .
   * @param envIndex .
   * @param names names list.
   * @return Lua.LuaError.
   */
  public static Lua.LuaError pushValue(Lua lua, int envIndex, String... names) {
  	lua.pushValue(envIndex);
  	for (String name:names) {
  		lua.getField(-1, name);
  		lua.replace(-2);
    }
  	return Lua.LuaError.OK;
  }
  
  /**
   * Push tables to stack
   * Retrieve table from tree at top of stack or create a tree of table
   * @param lua Lua thread
   * @param index .
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
  
  /**
   * @param lua .
   * @param index .
   */
  public static void getfenv(Lua lua, int index) {
    if (lua.getLuaNative() instanceof Lua51Natives) {
      new Lua51Natives() {
        {
          lua_getfenv(lua.getPointer(), index);
        }
      };
    }
  }

  /**
   * @param lua .
   * @param index .
   */
  public static void setfenv(Lua lua, int index) {
    if (lua.getLuaNative() instanceof Lua51Natives) {
      new Lua51Natives() {
        {
          lua_setfenv(lua.getPointer(), index);
        }
      };
    }
  }

  /**
   * Test if lua library is loaded.
   * @param lua .
   * @param envIndex .
   * @return boolean.
   */
  public static boolean isLoadedModule(Lua lua, int envIndex) {
    getLoadedTable(lua, envIndex);
    lua.pushValue(1); /* library name */
    lua.getTable(-2); /* LoadedTable[libraryName] */
    lua.replace(-2);  /* replace stack LoadedTable <- module */
    return !lua.isNil(-1);
  }

  /**
   * Set true as module inside lua loaded table.
   * @param lua .
   * @param envIndex .
   */
  public static void setLoadedModuleToTrue(Lua lua, int envIndex) {
  	lua.push(true);
  	setLoadedModule(lua, envIndex);
  	lua.pop(1); /* pop true */
  }

  /**
   * Set top stack value as module inside lua loaded table.
   * @param lua .
   * @param envIndex .
   */
  public static void setLoadedModule(Lua lua, int envIndex) {
    getLoadedTable(lua, envIndex);
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
  
  /**
   * Split library name to array of module
   * @param libraryName .
   * @param removelast .
   * @return a array of libraryName splits, with last removed if removelast is true.
   */
  public static String[] libraryNameSplit(String libraryName, boolean removelast) {
  	String[] splits = libraryName.split("\\.");
  	if (removelast) {
      splits = Arrays.copyOfRange(splits, 0, splits.length-1);
    }
  	return splits;
  }

  /**
   * @param lua .
   * @param envIndex .
   */
  public static void getloaders(Lua lua, int envIndex) {
  	pushValue(lua, envIndex, "package", "loaders");
  }

  /**
   * @param lua .
   * @param envIndex .
   */
  public static void getLoadedTable(Lua lua, int envIndex) {
  	pushValue(lua, envIndex, "package", "loaded");
  }

  /**
   * @param lua .
   * @param error .
   */
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
  	values.add(error.name());

    if (lua.getTop() != 0 && lua.isString(-1)) {
    	values.add(lua.toString(-1));
    }
    
    /*if (lua.getLuaNative() instanceof Lua51Natives) {
      new Lua51Natives() {
        {
        	luaL_where(getRegistryIndex(), 0);
        }
      };
    }*/
    
    /*luaL_traceback(long ptr, long L1, String msg, int level)
    lua.push(_getTraceback, Conversion.NONE);
  	if (lua.pCall(0, 1) == Lua.LuaError.OK) { /* push stack trace */
  	//values.add(lua.toString(-1));
  	//}

    Throwable javaError = lua.getJavaError();
    if (javaError != null) {
    	values.add(javaError);
    	lua.error((Throwable) null);
    }
    return values;
  }
  
  /**
   * @param lua .
   * @param exception .
   * @return int.
   */
  public static int handleJavaError(Lua lua, Exception exception) {
  	lua.push(exception.toString());
  	return -1;
  }
  
  /**
   * @param lua .
   * @param error .
   * @throws Exception
   */
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
  
  /**
   * @param lua .
   * @param error .
   */
  public static void invokeErrorEvent(Lua lua, Lua.LuaError error) {
  	ModuleManager.getInstance().offer(new ModuleExecutorCommand(
    		ModuleExecutor.Command.EXECUTE,
    		ConsoleManager.getInstance().getModuleUuid(),
    		new ConsoleCommand(ConsoleModule.Command.PRINT, handleLuaError(lua, error).toArray())
    ));
  }

  /**
   * @param lua .
   * @param message .
   */
  public static void invokeConsoleEvent(Lua lua, String message) {
  	ModuleManager.getInstance().offer(new ModuleExecutorCommand(
    		ModuleExecutor.Command.EXECUTE,
    		ConsoleManager.getInstance().getModuleUuid(),
    		new ConsoleCommand(ConsoleModule.Command.PRINT, message)
    ));
  }

  /**
   * @param lua .
   * @param eventHandlers .
   * @param args .
   */
  public static void invokeEvent(Lua lua, LuaValue eventHandlers, LuaValue... args) {
  	ModuleManager.getInstance().offer(new ModuleExecutorCommand(
  			ModuleExecutor.Command.EXECUTE,
    		getJavaModuleUuid(lua),
    		new LuaMTCEvent(getJavaThreadUuid(lua), new LuaModuleEvent(eventHandlers, args))
    ));
  }
  
  /**
   * @param lua .
   * @param index .
   * @param name .
   */
  public static void setEnvInfos(Lua lua, int index, String name) {
  	lua.push("_");
  	lua.createTable(0, 0);
  	lua.push("Name");
  	lua.push(name);
  	lua.rawSet(-3);
  	lua.rawSet(relativizeIndex(index, -2));
  }
  
  /**
   * @param lua .
   * @param index .
   */
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
  
  /**
   * @param lua .
   * @param index .
   * @param value .
   */
  public static void logEnvInfos__index(Lua lua, int index, @Nullable String value) {
  	if (!baseFuncs.contains(value)) {
  		logEnvInfos(lua, index, "__index: " + value);
  	}
  }

  /**
   * @param lua .
   * @param index .
   * @param message .
   */
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
  
  /**
   * @param lua .
   * @param index .
   * @return a module file name.
   */
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
  
  /**
   * @param lua .
   * @return int.
   */
  public static int print(Lua lua) {
  	invokeConsoleEvent(lua, lua.toString(1));
  	return 1;
  }
  /*
  public static void mobdebugStart() {
  	_mobdebug.get("start").call();
  }*/
  
  /**
   * @param lua .
   * @param index .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError metaluaCompiler_src_to_ast(Lua lua, int index) {
  	lua.getGlobal(LuaTools.PCALL_ERR_FUNC_NAME);
  	lua.getGlobal("metalua_compiler");
  	lua.pushValue(-1);
  	lua.getField(-1, "src_to_ast");
  	lua.replace(-3);
  	pushValueRelative(lua, index, -2);
  	return pCall(lua, 2, 1, -4);
  }
  
  /**
   * @param lua .
   * @param index .
   * @param errfunc .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError metaluaCompiler_function_to_src(Lua lua, int index, int errfunc) {
  	lua.getGlobal("metalua_compiler");
  	lua.pushValue(-1);
  	lua.getField(-1, "bytecode_to_ast");
  	lua.replace(-3);
  	pushValueRelative(lua, index, -2);
  	return pCall(lua, 2, 1, relativizeIndex(errfunc, -3));
  }
}
