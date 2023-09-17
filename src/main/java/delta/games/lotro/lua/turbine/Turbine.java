package delta.games.lotro.lua.turbine;

import static org.squiddev.cobalt.ValueFactory.tableOf;
import static org.squiddev.cobalt.ValueFactory.userdataOf;
import static org.squiddev.cobalt.ValueFactory.valueOf;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaString;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaThread;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.OperationHelper;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.ValueFactory;
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.games.lotro.lua.turbine.shell.Shell;
import delta.games.lotro.lua.turbine.shell.ShellCommand;
import delta.games.lotro.lua.turbine.ui.Lotro;
import delta.games.lotro.lua.turbine.ui.UI;
import delta.games.lotro.lua.utils.LuaTools;
import delta.games.lotro.lua.utils.URLToolsLua;

/**
 * Turbine library for lua scripts.
 * @author MaxThlon
 */
public abstract class Turbine {
  private static Logger LOGGER = Logger.getLogger(Turbine.class);

  public static void add(LuaState state, LuaTable env) {
    RegisteredFunction.bind(env, new RegisteredFunction[]{
        RegisteredFunction.of("import", Turbine::Import)
    });
  }

  public static LuaValue Import(LuaState state, LuaValue library) throws LuaError, UnwindThrowable {
    LuaThread thread = state.getCurrentThread();
    LuaTable globals = thread.getfenv();
    LuaFunction require = (LuaFunction) OperationHelper.getTable(state, globals, ValueFactory.valueOf("require"));

    LuaString _library = library.checkLuaString();
    switch(_library.toString()) {
      case "Turbine.Type":
        _library = LuaString.valueOf(
            FilenameUtils.removeExtension(URLToolsLua.getFromClassPath("turbine/Type.lua"))
        );
        return require.call(state, _library);
      case "Turbine.Class":
        _library = LuaString.valueOf(
            FilenameUtils.removeExtension(URLToolsLua.getFromClassPath("turbine/Class.lua"))
        );
        return require.call(state, _library);
      case "Turbine.Enum":
        _library = LuaString.valueOf(
            FilenameUtils.removeExtension(URLToolsLua.getFromClassPath("turbine/Enum.lua"))
        );
        return require.call(state, _library);
      case "Turbine":
        LuaFunction luaClass = globals.rawget("class").checkFunction();
        LuaTable luaObjectClass = luaClass.call(state).checkTable();

        RegisteredFunction.bind(luaObjectClass, new RegisteredFunction[]{
            RegisteredFunction.of("Constructor", Turbine::ObjectConstructor)
        });
        
        LuaTable luaTurbine = tableOf(
            valueOf("Object"), luaObjectClass,
            valueOf("Language"), tableOf(
                valueOf("Invalid"),valueOf(0),
                valueOf("English"),valueOf(2),
                valueOf("EnglishGB"),valueOf(268435457),
                valueOf("French"),valueOf(268435459),
                valueOf("German"),valueOf(268435460),
                valueOf("Russian"),valueOf(268435463)
            )
        );
        
        ShellCommand.add(state, luaTurbine, luaClass, luaObjectClass);
        Shell.add(state, luaTurbine);
        
        globals.rawset("Turbine", luaTurbine);
        break;
      case "Turbine.Gameplay":
        _library = LuaString.valueOf("delta.games.lotro.lua.turbine.gameplay.Gameplay");
        break;
      case "Turbine.UI":
        UI.add(state);
        break;
      case "Turbine.UI.Lotro":
        Lotro.add(state);
        break;
      default:
        LOGGER.debug("Import: " + _library.toString());
        return require.call(state, _library);
    }
    
    return Constants.NIL;
  }
  
  public static Object objectSelf(LuaState state, LuaValue self) throws LuaError {
    return LuaTools.checkUserdata(
        self.metatag(state, valueOf("__objectSelf")).checkTable().rawget(self)
    );
  }
  
  public static <T> T objectSelf(LuaState state, LuaValue self, Class<T> clazz) throws LuaError {
    return LuaTools.checkUserdata(
        self.metatag(state, valueOf("__objectSelf")).checkTable().rawget(self),
        clazz
    );
  }
  
  public static LuaValue ObjectConstructor(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue ObjectInheritedConstructor(LuaState state,
                                                    LuaValue self,
                                                    Object objectSelf,
                                                    LuaFunction luaNewIndexMetaFunc,
                                                    LuaFunction luaIndexMetaFunc) throws LuaError {
    LuaTable metatable = self.getMetatable(state);

    if (luaNewIndexMetaFunc != null) metatable.rawset(Constants.NEWINDEX, luaNewIndexMetaFunc);
    //if (luaIndexMetaFunc != null) metatable.rawset(Constants.INDEX, luaIndexMetaFunc);

    LuaTable __objectSelf = metatable.rawget("__objectSelf").optTable(null);
    if (__objectSelf == null) {
      __objectSelf = tableOf();
      self.getMetatable(state).rawset("__objectSelf", __objectSelf);
    }
    
    __objectSelf.rawset(self, userdataOf(objectSelf));
    return Constants.NIL;
  }
}

