package delta.games.lotro.lua.turbine.plugin;

import static org.squiddev.cobalt.ValueFactory.userdataOf;

import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.OperationHelper;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.games.lotro.client.plugin.Plugin;
import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeNodeList;
import delta.games.lotro.lua.utils.LuaTools;

/**
 * @author MaxThlon
 */
public class LuaPlugin
{
  private static LuaTable _luaPluginClass;

  public static void add(LuaState state, LuaTable turbine) throws LuaError, UnwindThrowable {

    _luaPluginClass = Turbine._luaClass.call(state, Turbine._luaObjectClass).checkTable();
    RegisteredFunction.bind(_luaPluginClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaPlugin::constructor),
        RegisteredFunction.of("GetName", LuaPlugin::getName),
        RegisteredFunction.of("GetVersion", LuaPlugin::getVersion),
        RegisteredFunction.of("GetAuthor", LuaPlugin::getAuthor),
        RegisteredFunction.of("GetConfiguration", LuaPlugin::getConfiguration),
        RegisteredFunction.of("Load", LuaPlugin::getConfiguration),
        RegisteredFunction.of("Unload", LuaPlugin::getConfiguration),
        RegisteredFunction.of("GetOptionsPanel", LuaPlugin::getConfiguration)
    });
    turbine.rawset("Plugin", _luaPluginClass);
  }
  
  public static Plugin pluginSelf(LuaState state, LuaValue self) throws LuaError {
    return Turbine.objectSelf(state, self, Plugin.class);
  }

  public static LuaTable newLuaPlugin(LuaState state, Plugin plugin) throws LuaError, UnwindThrowable {
    LuaTable luaPlugin = OperationHelper.call(
        state,
        _luaPluginClass,
        userdataOf(plugin)
    ).checkTable();

    return luaPlugin;
  }
  
  public static LuaValue constructor(LuaState state, LuaValue self, LuaValue plugin) throws LuaError {
    Turbine.ObjectInheritedConstructor(
        state,
        self,
        LuaTools.optUserdata(plugin, Plugin.class, null),
        null,
        null
    );
    return Constants.NIL;
  }
  
  public static LuaValue getName(LuaState state, LuaValue self) {

    return Constants.NIL;
  }
  
  public static LuaValue getVersion(LuaState state, LuaValue self) {

    return Constants.NIL;
  }

  public static LuaValue getAuthor(LuaState state, LuaValue self) {

    return Constants.NIL;
  }
  
  public static LuaValue getConfiguration(LuaState state, LuaValue self) {

    return Constants.NIL;
  }
}
