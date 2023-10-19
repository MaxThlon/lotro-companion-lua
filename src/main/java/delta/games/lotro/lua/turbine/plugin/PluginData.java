package delta.games.lotro.lua.turbine.plugin;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.games.lotro.lua.turbine.Turbine;

/**
 * @author MaxThlon
 */
public class PluginData
{
  public static void add(LuaState state, LuaTable turbine) {

    LuaTable pluginData=RegisteredFunction.bind(new RegisteredFunction[]{
        RegisteredFunction.of("Load", PluginData::load),
        RegisteredFunction.of("Save", PluginData::save)
    });
    turbine.rawset("PluginData", pluginData);
  }
  
  public static LuaValue load(LuaState state, LuaValue self) {

    return Constants.NIL;
  }
  
  public static LuaValue save(LuaState state, LuaValue self) {

    return Constants.NIL;
  }
}
