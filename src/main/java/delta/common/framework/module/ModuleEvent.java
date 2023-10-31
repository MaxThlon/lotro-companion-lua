package delta.common.framework.module;

import java.util.UUID;

import javax.annotation.Nullable;

import delta.games.lotro.utils.events.Event;

/**
 * @author MaxThlon
 */
public class ModuleEvent extends Event {
	public ModuleExecutor.ExecutorEvent _executorEvent;
	public UUID _sender;
	public String _name;
  public @Nullable Object[] _args;
  
  public ModuleEvent(ModuleExecutor.ExecutorEvent executorEvent,
  									 UUID sender,
  									 String name,
  									 Object[] args) {
    _executorEvent = executorEvent;
    _sender = sender;
    _name = name;
    _args = args;
  }
}
