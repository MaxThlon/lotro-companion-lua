package delta.games.lotro.lua.command;

import java.util.UUID;

import delta.common.framework.lua.LuaModule;
import delta.common.framework.lua.command.LuaModuleThreadCommand;

/**
 * @author MaxThlon
 */
public class LotroLMCNewScriptState extends LuaModuleThreadCommand {
	String _scriptState;
	
	/**
	 * @param threadUuid .
	 * @param scriptState .
	 */
	public LotroLMCNewScriptState(UUID threadUuid, String scriptState) {
		super(LuaModule.Command.NEW_THREAD, threadUuid);
		_scriptState = scriptState;
	}

	/**
	 * @return script state name.
	 */
	public String getScriptStateName() {
		return _scriptState;
	}
}
