package delta.common.framework.console;

import javax.annotation.Nullable;

import delta.games.lotro.utils.events.Event;

/**
 * @author MaxThlon
 */
public class ConsoleCommandEvent extends Event {
	private String _command;
	private String _args;

  public ConsoleCommandEvent(String command, String args) {
  	_command = command;
  	_args = args;
  }
	
	public String getCommand() {
		return _command;
	}
	
	public @Nullable String getArgs() {
		return _args;
	}
}
