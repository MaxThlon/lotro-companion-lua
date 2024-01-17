package delta.games.lotro.lua.command;

import java.util.UUID;

import delta.common.framework.lua.LuaModule;
import delta.common.framework.lua.command.LuaModuleThreadCommand;

/**
 * @author MaxThlon
 */
public class LotroLMCUnloadScriptState extends LuaModuleThreadCommand {
	String _scriptStateName;
	
	/**
	 * @param threadUuid .
	 * @param scriptStateName .
	 */
	public LotroLMCUnloadScriptState(UUID threadUuid, String scriptStateName) {
		super(LuaModule.Command.INTERRUPT_THREAD, threadUuid);
		_scriptStateName = scriptStateName;
	}

	/**
	 * @return script state name.
	 */
	public String getScriptStateName() {
		return _scriptStateName;
	}
}