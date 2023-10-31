package delta.games.lotro.lua.turbine;

import party.iroiro.luajava.Lua;
import party.iroiro.luajava.value.LuaValue;

/**
 * @author MaxThlon
 */
public class Apartment
{
  LuaValue _env;

  public Apartment(LuaValue env) {
    _env=/*createInheritedEnv(*/env;
  }

  public LuaValue getEnv() {
    return _env;
  }
  
  public static Apartment findApartment(Lua lua) {
    return null; /*LuaTools.checkUserdata(
        state.getCurrentThread().getfenv().rawget("__apartment"),
        Apartment.class
    );*/
  }
}
