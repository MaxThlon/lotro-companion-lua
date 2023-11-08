package delta.common.framework.lua.event;

import javax.annotation.Nullable;

import delta.games.lotro.utils.events.Event;
import party.iroiro.luajava.value.LuaValue;

/**
 * @author MaxThlon
 */
public class LuaModuleEvent extends Event {
	private LuaValue _eventHandlers;
	private @Nullable LuaValue[] _args;
	
	/**
	 * @param eventHandlers .
	 * @param args .
	 */
	public LuaModuleEvent(LuaValue eventHandlers,
			 									LuaValue... args) {
    _eventHandlers = eventHandlers;
    _args = args;
	}

  /**
   * @return EventHandlers.
   */
  public LuaValue getEventHandlers() {
  	return _eventHandlers;
  }
  
  /**
   * @return Args.
   */
  public @Nullable LuaValue[] getArgs() {
  	return _args;
  }
}
