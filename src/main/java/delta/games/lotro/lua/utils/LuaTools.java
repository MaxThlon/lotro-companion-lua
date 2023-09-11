package delta.games.lotro.lua.utils;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.ErrorFactory;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaUserdata;
import org.squiddev.cobalt.LuaValue;

public abstract class LuaTools {

  static public Object checkUserdata(LuaValue value) {
    return (value.type() == Constants.TUSERDATA)?((LuaUserdata)value).instance : null;
  }

  static public <T> T checkUserdata(LuaValue value, Class<T> c) throws LuaError {
    Object instance = checkUserdata(value);
    if (!c.isAssignableFrom(instance.getClass())) throw ErrorFactory.typeError(value, c.getName());
    return c.cast(instance);
  }
  
  static public <T> T optUserdata(LuaValue value, Class<T> c, T defval) throws LuaError {
    
    return value == Constants.NIL ? defval : checkUserdata(value, c);
  }
  
}
