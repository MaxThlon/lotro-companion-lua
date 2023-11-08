package delta.common.framework.lua.command;

import java.util.UUID;

import javax.annotation.Nullable;

import delta.common.framework.lua.LuaModule;

/**
 * @author MaxThlon
 */
public class LuaMTCCommand extends LuaModuleThreadCommand {
	private String _command;
	private @Nullable String[] _args;

	/**
	 * @param threadUuid .
	 * @param command .
	 * @param args .
	 */
	public LuaMTCCommand(UUID threadUuid,
											 String command,
											 @Nullable String... args) {
		super(LuaModule.Command.COMMAND, threadUuid);
		_command = command;
		_args = args;
	}

	/**
	 * @return string command.
	 */
	public String getCommand() {
		return _command;
	}
	
	/**
	 * @return string array of args.
	 */
	public @Nullable String[] getArgs() {
		return _args;
	}
}
