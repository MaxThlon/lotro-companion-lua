package delta.common.framework.console.event;

import javax.annotation.Nullable;

import delta.games.lotro.utils.events.Event;

/**
 * @author MaxThlon
 */
public class ConsoleCommandEvent extends Event {
  private String _command;
  private String _args;

  /**
   * @param command .
   * @param args .
   */
  public ConsoleCommandEvent(String command, String args) {
    _command = command;
    _args = args;
  }

  /**
   * @return command.
   */
  public String getCommand() {
    return _command;
  }

  /**
   * @return args.
   */
  public @Nullable String getArgs() {
    return _args;
  }
}
