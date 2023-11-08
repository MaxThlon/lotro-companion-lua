package delta.common.framework.console.command;

import javax.annotation.Nullable;

import delta.common.framework.console.ConsoleModule;
import delta.common.framework.module.command.ModuleCommand;

/**
 * @author MaxThlon
 */
public class ConsoleCommand implements ModuleCommand {
	private ConsoleModule.Command _command;
	private @Nullable Object[] _args;

  public ConsoleCommand(ConsoleModule.Command command,
			 								  Object... args) {
  	_command = command;
  	_args = args;
  }
	
	public ConsoleModule.Command getCommand() {
		return _command;
	}
	
	public @Nullable Object[] getArgs() {
		return _args;
	}
}
