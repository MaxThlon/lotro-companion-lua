package delta.games.lotro.lua.command;

import java.util.UUID;

import delta.common.framework.lua.LuaModule;
import delta.common.framework.lua.command.LuaModuleThreadCommand;
import delta.games.lotro.client.plugin.Plugin;

/**
 * @author MaxThlon
 */
public class LuaMTCLoadPlugin extends LuaModuleThreadCommand {
	private Plugin _plugin;

	/**
	 * @param threadUuid .
	 * @param plugin .
	 */
	public LuaMTCLoadPlugin(UUID threadUuid,
													Plugin plugin) {
		super(LuaModule.Command.COMMAND, threadUuid);
		_plugin = plugin;
	}

	/**
	 * @return a plugin.
	 */
	public Plugin getPlugin() {
		return _plugin;
	}
}
