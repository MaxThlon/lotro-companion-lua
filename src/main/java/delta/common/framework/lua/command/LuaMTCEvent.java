package delta.common.framework.lua.command;

import java.util.UUID;

import delta.common.framework.lua.LuaModule;
import delta.common.framework.lua.event.LuaModuleEvent;

/**
 * @author MaxThlon
 */
public class LuaMTCEvent extends LuaModuleThreadCommand {
	private LuaModuleEvent _event;
	
	/**
	 * @param threadUuid .
	 * @param event .
	 */
	public LuaMTCEvent(UUID threadUuid,
										 LuaModuleEvent event) {
		super(LuaModule.Command.EVENT, threadUuid);
		_event = event;
	}

	/**
	 * @return a LuaModuleEvent.
	 */
	public LuaModuleEvent getEvent() {
		return _event;
	}
}
