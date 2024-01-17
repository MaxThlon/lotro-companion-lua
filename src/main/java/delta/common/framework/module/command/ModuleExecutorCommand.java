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

  /**
   * @param command .
   * @param moduleUuid .
   * @param handlers .
   */
  public ModuleExecutorCommand(ModuleExecutor.Command command,
  														 UUID moduleUuid,
  														 ModuleCommand... handlers) {
    _command = command;
    _moduleUuid = moduleUuid;
    _handlers = handlers;
  }
  
  /**
   * @return handled command type.
   */
  public ModuleExecutor.Command getExecutorEvent() {
  	return _command;
  }

  /**
   * @return module uuid.
   */
  public UUID getModuleUuid() {
  	return _moduleUuid;
  }
  
  /**
   * @return module command handlers.
   */
  public @Nullable ModuleCommand[] getHandlers() {
  	return _handlers;
  }
}
