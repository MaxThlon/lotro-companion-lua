package delta.games.lotro.lua.turbine;

import static org.squiddev.cobalt.ValueFactory.tableOf;
import static org.squiddev.cobalt.ValueFactory.userdataOf;
import static org.squiddev.cobalt.ValueFactory.valueOf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

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
import org.squiddev.cobalt.Varargs;
import org.squiddev.cobalt.function.LibFunction;
import org.squiddev.cobalt.function.LuaClosure;
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.common.framework.plugin.PluginEvent;
import delta.games.lotro.client.plugin.Plugin;
import delta.games.lotro.lua.turbine.gameplay.Gameplay;
import delta.games.lotro.lua.turbine.plugin.LuaPlugin;
import delta.games.lotro.lua.turbine.plugin.PluginData;
import delta.games.lotro.lua.turbine.plugin.PluginManager;
import delta.games.lotro.lua.turbine.shell.Shell;
import delta.games.lotro.lua.turbine.shell.ShellCommand;
import delta.games.lotro.lua.turbine.ui.Lotro;
import delta.games.lotro.lua.turbine.ui.UI;
import delta.games.lotro.lua.utils.LuaTools;

/**
 * Turbine library for lua scripts.
 * @author MaxThlon
 */
public final class Turbine {
  private static Logger LOGGER = Logger.getLogger(Turbine.class);
  private static LuaFunction _lua_loader;
  private static LuaFunction _require;
  public static LuaFunction _luaClass;
  public static LuaTable _luaObjectClass;

  private static Map<String, Apartment> _apartments=new HashMap<String,Apartment>();

  public static void add(LuaState state, LuaTable env) throws LuaError {
    LuaTable loaders = env.rawget("package").checkTable().rawget("loaders").checkTable();
    _lua_loader = loaders.rawget(2).checkFunction();
    loaders.rawset(2, LibFunction.createV(Turbine::loader_Lua));
    _require=env.rawget("require").checkFunction();

    RegisteredFunction.bind(env, new RegisteredFunction[]{
        RegisteredFunction.of("import", Turbine::import_lua),
        RegisteredFunction.of("processEvents", Turbine::processEvents)
    });
  }

  public static LuaValue processEvents(LuaState state, LuaValue value) throws LuaError, UnwindThrowable {
    PluginEvent event = LuaTools.checkUserdata(value, PluginEvent.class);
    if ((event != null) && (event._args != null)) {

      switch(event._name) {
        case "loadPlugin": {
          Plugin plugin = (Plugin)event._args[0];
          Apartment apartment = _apartments.get(plugin._apartmentName);
          if (apartment == null) {
            apartment=new Apartment(state.getMainThread().getfenv());
            _apartments.put(plugin._apartmentName, apartment);
          }
          
          LuaValue luaPlugin = LuaTools.findLuaObjectFromObject(plugin);
          if (luaPlugin == null) {
            luaPlugin = LuaPlugin.newLuaPlugin(state, plugin);
          }
          
          state.getCurrentThread().getfenv().rawset("plugin", luaPlugin);
          import_lua(state, valueOf(plugin._package));
          break;
        }
        default: {
          LOGGER.debug("Event name: " + event._name);
          //Apartment apartment = (Apartment)event._args[0];
          OperationHelper.call(state, (LuaValue)event._args[1], (LuaValue)event._args[2], (LuaValue)event._args[3]);
          break;
        }
      }
    }
    return Constants.NIL;
  }

  public static LuaTable generatefenv(LuaState state, LuaTable env, String[] splits) throws LuaError, UnwindThrowable {
    LuaTable currentEnv=env;
    for(String module:splits) {
      LuaTable newTable = OperationHelper.getTable(state, currentEnv, valueOf(module)).optTable(null);
      if (newTable == null) {
        LuaTable new_env_metatable=new LuaTable();
        new_env_metatable.rawset("__index", currentEnv);
        newTable = new LuaTable();
        newTable.setMetatable(state, new_env_metatable);
        currentEnv.rawset(module, newTable);
      }
      currentEnv = newTable;
    }
    return currentEnv;
  }
  
  public static LuaTable generatefenv(LuaState state, LuaTable env, String library) throws LuaError, UnwindThrowable {
    return generatefenv(state, env, library.split("\\."));
  }
  
  public static LuaTable libraryForceLoaded(LuaState state, LuaTable loaded, LuaString library) throws LuaError, UnwindThrowable {
    loaded.rawset(library, Constants.TRUE);
    return generatefenv(state, state.getCurrentThread().getfenv(), library.checkString());
  }

  private static Varargs loader_Lua(LuaState state, Varargs args) throws LuaError, UnwindThrowable {
    LuaString library = args.first().checkLuaString();
    
    Varargs resultArgs = _lua_loader.invoke(state, args);
    LuaFunction chunk = resultArgs.first().optFunction(null);
    if (chunk != null) {
      String[] splits = library.checkString().split("\\.");
      if (!((LuaClosure)chunk).getPrototype().source.checkString().endsWith("__init__.lua")) {
        splits = Arrays.copyOfRange(splits, 0, splits.length-1);
      }

      chunk.setfenv(generatefenv(state, state.getCurrentThread().getfenv(), splits));
    }
    return resultArgs;
  }

  public static LuaValue libraryExisting(LuaState state, LuaTable loaded, LuaString library) {
    LuaValue existing = loaded.rawget(library);
    if (existing.toBoolean()) {
      return existing;
    }
    return null;
  }

  private static LuaValue requireCall(LuaState state, @Nullable Apartment apartment, LuaTable loaded, LuaString library) throws LuaError, UnwindThrowable {
    LuaValue existing = libraryExisting(state, loaded, library);
    if (existing != null) {
      return existing;
    }

    return _require.call(state, library);
  }

  public static LuaValue import_lua(LuaState state, LuaValue value) throws LuaError, UnwindThrowable {
    LuaThread thread = state.getCurrentThread();
    LuaTable env = thread.getfenv();
    LuaTable loaded = state.registry().getSubTable(Constants.LOADED);
    LuaString library = value.checkLuaString();

    Apartment apartment = LuaTools.optUserdata(OperationHelper.getTable(state, env, valueOf("Apartment")), Apartment.class, null);

    LuaValue existing = libraryExisting(state, loaded, library);
    if (existing != null) {
      return existing;
    }
    
    switch(library.toString()) {
      /*case "Turbine.Type":
        library = LuaString.valueOf(
            FilenameUtils.removeExtension(URLToolsLua.getFromClassPath("turbine/Type.lua"))
        );
        return requireCall(state, apartment, globals, loaded, library);*/
      case "Turbine.Class": {
        LuaValue libraryValue = requireCall(state, apartment, loaded, library);
        _luaClass = env.rawget("class").checkFunction();
        _luaObjectClass = _luaClass.call(state).checkTable();
        env.rawget("Turbine").checkTable().rawset("Object", _luaObjectClass);
        RegisteredFunction.bind(_luaObjectClass, new RegisteredFunction[]{
            RegisteredFunction.of("Constructor", Turbine::ObjectConstructor)
        });
        return libraryValue;
      }
      /*case "Turbine.Enum":
        library = LuaString.valueOf(
            FilenameUtils.removeExtension(URLToolsLua.getFromClassPath("turbine/Enum.lua"))
        );
        return requireCall(state, apartment, globals, loaded, library);*/
      case "Turbine": {
        LuaValue libraryValue = requireCall(state, apartment, loaded, library);
        LuaTable turbineEnv = env.rawget("Turbine").checkTable();
        turbineEnv.rawset("Language", tableOf(
            valueOf("Invalid"),valueOf(0),
            valueOf("English"),valueOf(2),
            valueOf("EnglishGB"),valueOf(268435457),
            valueOf("French"),valueOf(268435459),
            valueOf("German"),valueOf(268435460),
            valueOf("Russian"),valueOf(268435463)
        ));
        turbineEnv.rawset("DataScope", tableOf(
            valueOf("Account"),valueOf(1),
            valueOf("Server"),valueOf(2),
            valueOf("Character"),valueOf(3)
        ));
        turbineEnv.rawset("ChatType", tableOf(
            valueOf("Undef"),valueOf(0),
            valueOf("Error"),valueOf(1),
            valueOf("Admin"),valueOf(3),
            valueOf("Standard"),valueOf(4),
            valueOf("Say"),valueOf(5),
            valueOf("Tell"),valueOf(6),
            valueOf("Emote"),valueOf(7),
            valueOf("Fellowship"),valueOf(11),
            valueOf("Kinship"),valueOf(12),
            valueOf("Officer"),valueOf(13),
            valueOf("Advancement"),valueOf(14),
            valueOf("Trade"),valueOf(15),
            valueOf("LFF"),valueOf(16),
            valueOf("Advice"),valueOf(17),
            valueOf("OOC"),valueOf(18),
            valueOf("Regional"),valueOf(19),
            valueOf("Death"),valueOf(20),
            valueOf("Quest"),valueOf(21),
            valueOf("Raid"),valueOf(23),
            valueOf("Unfiltered"),valueOf(25),
            valueOf("Roleplay"),valueOf(27),
            valueOf("UserChat1"),valueOf(28),
            valueOf("UserChat2"),valueOf(29),
            valueOf("UserChat3"),valueOf(30),
            valueOf("UserChat4"),valueOf(31),
            valueOf("Tribe"),valueOf(32),
            valueOf("PlayerCombat"),valueOf(34),
            valueOf("EnemyCombat"),valueOf(35),
            valueOf("PlayerLoot"),valueOf(36),
            valueOf("FellowshipLoot"),valueOf(37),
            valueOf("World"),valueOf(38),
            valueOf("UserChat5"),valueOf(39),
            valueOf("UserChat6"),valueOf(40),
            valueOf("UserChat7"),valueOf(41),
            valueOf("UserChat8"),valueOf(42)
        ));
        LuaPlugin.add(state, turbineEnv);
        PluginData.add(state, turbineEnv);
        PluginManager.add(state, turbineEnv);
        ShellCommand.add(state, turbineEnv);
        Shell.add(state, turbineEnv);
        Gameplay.add(state, turbineEnv, _luaClass, _luaObjectClass);
        return libraryValue;
      }
      case "Turbine.Gameplay":
        library = LuaString.valueOf("delta.games.lotro.lua.turbine.gameplay.Gameplay");
        break;
      case "Turbine.UI": {
        LuaTable uiEnv = libraryForceLoaded(state, loaded, library);
        UI.add(state, uiEnv);
        break;
      }
      case "Turbine.UI.Lotro": {
        LuaTable uiLotroEnv = libraryForceLoaded(state, loaded, library);
        Lotro.add(state, uiLotroEnv);
        break;
      }
      default:
        LOGGER.debug("Import: " + library.toString());
        return requireCall(state, apartment, loaded, library);
    }
    
    return Constants.NIL;
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

