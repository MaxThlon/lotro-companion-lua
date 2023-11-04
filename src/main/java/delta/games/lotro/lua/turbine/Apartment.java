package delta.games.lotro.lua.turbine;

import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class Apartment
{
	Lua _thread;

  public Apartment(Lua thread) {
  	_thread = thread;
  }

  public Lua getThread() {
    return _thread;
  }
}
