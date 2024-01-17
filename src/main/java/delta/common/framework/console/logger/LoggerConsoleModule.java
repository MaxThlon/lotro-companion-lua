package delta.common.framework.console.logger;

import java.util.UUID;

import javax.annotation.Nullable;

import org.apache.log4j.Logger;

import delta.common.framework.console.ConsoleModule;
import delta.common.framework.console.command.ConsoleCommand;
import delta.common.framework.module.command.ModuleCommand;

/**
 * @author MaxThlon
 */
public class LoggerConsoleModule extends ConsoleModule {
	private static Logger LOGGER = Logger.getLogger(LoggerConsoleModule.class);

	/**
	 * Constructor.
	 * @param uuid
	 */
	public LoggerConsoleModule(UUID uuid) {
		super(uuid);
	}

	@Override
	public void load(@Nullable ModuleCommand[] commands) {
		/* empty */
	}
	
	@Override
	public void execute(ModuleCommand command) {
		ConsoleCommand cCommand = (ConsoleCommand)command;
		if (cCommand.getArgs() != null) {
			for (Object arg:cCommand.getArgs()) {
				if (arg instanceof Throwable) {
					LOGGER.error("Exception", (Throwable)arg);
				} else {
					LOGGER.error((arg != null)?arg.toString(): "null");
				}
			}
		}
	}

	@Override
	public void unLoad() {
		/* empty */
	}

	@Override
	public void dispose() {
		/* empty */
	}
}
