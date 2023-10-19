package delta.games.lotro.lua.turbine;

import static org.squiddev.cobalt.ValueFactory.userdataOf;

import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;

import delta.games.lotro.lua.utils.LuaTools;

/**
 * @author MaxThlon
 */
public class Apartment
{
  LuaTable _env;

  public Apartment(LuaTable env) {
    _env=/*createInheritedEnv(*/env;
    _env.rawset("__apartment", userdataOf(this));
  }

  public LuaTable getEnv() {
    return _env;
  }
  
  private LuaTable createInheritedEnv(LuaTable env) {
    LuaTable new_env = new LuaTable();
    LuaTable new_env_metatable = new LuaTable();
    new_env_metatable.rawset("__index", env);
    new_env.setMetatable(null, new_env_metatable);
    return new_env;
  }
  
  public static Apartment findApartment(LuaState state) throws LuaError {
    return LuaTools.checkUserdata(
        state.getCurrentThread().getfenv().rawget("__apartment"),
        Apartment.class
    );
  }
}
