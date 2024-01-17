package delta.games.lotro.lua.command;

import java.util.UUID;

import delta.common.framework.lua.LuaModule;
import delta.common.framework.lua.command.LuaModuleThreadCommand;

/**
 * @author MaxThlon
 */
public class LuaMTCRequireScriptFile extends LuaModuleThreadCommand {
	private String _scriptFile;

	/**
	 * @param threadUuid .
	 * @param scriptFile .
	 */
	public LuaMTCRequireScriptFile(UUID threadUuid,
	                            String scriptFile) {
		super(LuaModule.Command.COMMAND, threadUuid);
		_scriptFile = scriptFile;
	}

	/**
	 * @return string command.
	 */
	public String getScriptFile() {
		return _scriptFile;
	}
}
