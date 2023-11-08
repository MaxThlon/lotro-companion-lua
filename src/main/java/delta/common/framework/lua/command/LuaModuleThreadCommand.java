package delta.common.framework.lua.command;

import java.util.UUID;

import delta.common.framework.lua.LuaModule;

/**
 * @author MaxThlon
 */
public class LuaModuleThreadCommand extends LuaModuleCommand {
	private UUID _threadUuid;
	
	public LuaModuleThreadCommand(LuaModule.Command type,
																UUID threadUuid) {
		super(type);
		_threadUuid = threadUuid;
	}

	public UUID getThreadUuid() {
		return _threadUuid;
	}
}
