package delta.games.lotro.lua.command;

import java.util.UUID;

import delta.common.framework.lua.LuaModule;
import delta.common.framework.lua.command.LuaModuleThreadCommand;
import delta.common.framework.plugin.LocalizedPlugin;

/**
 * @author MaxThlon
 */
public class LuaMTCLoadLocalizedPlugin extends LuaModuleThreadCommand {
  private LocalizedPlugin _localizedPlugin;

	/**
	 * @param threadUuid .
	 * @param localizedPlugin .
	 */
	public LuaMTCLoadLocalizedPlugin(UUID threadUuid,
	                                 LocalizedPlugin localizedPlugin) {
		super(LuaModule.Command.COMMAND, threadUuid);
		_localizedPlugin = localizedPlugin;
	}
	
	 /**
   * @return a localizedPlugin.
   */
  public LocalizedPlugin getLocalizedPlugin() {
    return _localizedPlugin;
  }
}
