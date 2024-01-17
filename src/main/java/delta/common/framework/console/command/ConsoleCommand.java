package delta.common.framework.console.command;

import javax.annotation.Nullable;

import delta.common.framework.console.ConsoleModule;
import delta.common.framework.module.command.ModuleCommand;

/**
 * @author MaxThlon
 */
public class ConsoleCommand implements ModuleCommand {
	private ConsoleModule.Command _type;
	private @Nullable Object[] _args;

  /**
   * @param type .
   * @param args .
   */
  public ConsoleCommand(ConsoleModule.Command type,
			 								  Object... args) {
  	_type = type;
  	_args = args;
  }
	
	/**
	 * @return command type.
	 */
	public ConsoleModule.Command getCommand() {
		return _type;
	}
	
	/**
	 * @return args .
	 */
	public @Nullable Object[] getArgs() {
		return _args;
	}
}
