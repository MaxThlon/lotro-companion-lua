package delta.games.lotro.lua.command;

import java.util.UUID;

import delta.common.framework.lua.LuaModule;
import delta.common.framework.lua.command.LuaModuleCommand;
import delta.common.framework.lua.command.LuaModuleThreadCommand;
import delta.games.lotro.client.plugin.Plugin;

/**
 * @author MaxThlon
 */
public class LotroLMCLoadThread extends LuaModuleThreadCommand {
	Plugin _plugin;
	
	public LotroLMCLoadThread(UUID threadUuid, Plugin plugin) {
		super(LuaModule.Command.EVENT, threadUuid);
		_plugin = plugin;
	}

	public Plugin getPlugin() {
		return _plugin;
	}
}
