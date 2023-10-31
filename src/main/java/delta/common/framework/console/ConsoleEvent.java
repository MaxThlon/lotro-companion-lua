package delta.common.framework.console;

import java.util.UUID;

import delta.common.framework.module.ModuleEvent;
import delta.common.framework.module.ModuleExecutor;

/**
 * @author MaxThlon
 */
public class ConsoleEvent extends ModuleEvent {
	public enum Event {
    PRINT
  }

	public ConsoleEvent.Event _event;

  public ConsoleEvent(int i, ModuleExecutor.ExecutorEvent executorEvent,
			 								UUID sender,
			 								ConsoleEvent.Event event,
			 								String name,
			 								Object[] args) {
    super(executorEvent, sender, name, args);
    _event = event;
  }
}
