package delta.common.framework.lua.command;

import delta.common.framework.lua.LuaModule;
import delta.common.framework.module.command.ModuleCommand;

/**
 * @author MaxThlon
 */
public class LuaModuleCommand implements ModuleCommand {
	private LuaModule.Command _type;
	
	/**
	 * @param type .
	 */
	public LuaModuleCommand(LuaModule.Command type) {
		_type = type;
	}
	
	/**
	 * @return command type.
	 */
	public LuaModule.Command getType() {
		return _type;
	}
}
