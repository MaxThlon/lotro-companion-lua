package delta.common.framework.module;

import java.util.UUID;

import javax.annotation.Nullable;

import delta.common.framework.module.command.ModuleCommand;
import delta.common.framework.module.command.ModuleExecutorCommand;

/**
 * @author MaxThlon
 */
public interface Module {
	/**
	 * @return module uuid.
	 */
	UUID getUuid();
	/**
	 * @return module name.
	 */
	String getName();

	/**
   * load module.
   * @param commands .
   */
	void load(@Nullable ModuleCommand[] commands);
  /**
   * execute command.
   * @param command .
   */
	void execute(ModuleCommand command);
	/**
   * can module accept this event.
   * @param command .
	 * @return true if can accept event.
   */
	default boolean canAccept(ModuleExecutorCommand command) {
		return true;
	}
	/**
	 * @param command
	 * @return same command or updated command data.
	 */
	default ModuleExecutorCommand preOffer(ModuleExecutorCommand command) {
		return command;
	}
	/**
	 * unload module.
	 */
	void unLoad();
	/**
	 * dispose module.
	 */
	void dispose();
}
