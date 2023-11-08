package delta.common.framework.module.command;

import java.util.UUID;

import javax.annotation.Nullable;

import delta.common.framework.module.ModuleExecutor;

/**
 * @author MaxThlon
 */
public class ModuleExecutorCommand {
	private ModuleExecutor.Command _command;
	private UUID _moduleUuid;
	private @Nullable ModuleCommand[] _handlers;

  public ModuleExecutorCommand(ModuleExecutor.Command command,
  														 UUID moduleUuid,
  														 ModuleCommand... handlers) {
    _command = command;
    _moduleUuid = moduleUuid;
    _handlers = handlers;
  }
  
  public ModuleExecutor.Command getExecutorEvent() {
  	return _command;
  }

  public UUID getModuleUuid() {
  	return _moduleUuid;
  }
  
  public @Nullable ModuleCommand[] getHandlers() {
  	return _handlers;
  }
}
