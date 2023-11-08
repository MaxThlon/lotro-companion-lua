package delta.common.framework.module;

import java.util.UUID;

import delta.common.framework.module.command.ModuleCommand;
import delta.common.framework.module.command.ModuleExecutorCommand;

/**
 * @author MaxThlon
 */
public interface Module {
	UUID getUuid();
	String getName();
	
	/**
   * can module accept this event.
   * @param command .
	 * @return true if can accept event.
   */
	default boolean canAccept(ModuleExecutorCommand command) {
		return true;
	}

	default ModuleExecutorCommand preOffer(ModuleExecutorCommand command) {
		return command;
	}
	
	/**
   * load.
   * @param command .
   */
	void load(ModuleExecutorCommand command);
  /**
   * execute command.
   * @param command .
   */
	void execute(ModuleCommand command);
	void unLoad();
}
