package delta.common.framework.console.dragonconsole;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.eleet.dragonconsole.CommandProcessor;

import delta.common.framework.console.ConsoleManager;
import delta.common.framework.console.ConsoleModule;
import delta.common.framework.console.command.ConsoleCommand;
import delta.common.framework.console.logger.LoggerConsoleModule;
import delta.common.framework.module.ModuleExecutor;
import delta.common.framework.module.ModuleManager;
import delta.common.framework.module.command.ModuleCommand;
import delta.common.framework.module.command.ModuleExecutorCommand;

/**
 * @author MaxThlon
 */
public class DragonConsoleModule extends ConsoleModule {
	private static Logger LOGGER = Logger.getLogger(LoggerConsoleModule.class);

	/**
	 * Constructor.
	 * @param uuid
	 */
	public DragonConsoleModule(UUID uuid) {
		super(uuid);
	}

	private boolean _isClosed = false;
	ConsoleWindowController _consoleWindowController;

	@Override
	public void load(ModuleExecutorCommand command) {
		_consoleWindowController = new ConsoleWindowController(this);
		_consoleWindowController.bringToFront();
	}
	
	@Override
	public void execute(ModuleCommand command) {
		ConsoleCommand cCommand = (ConsoleCommand)command;
		CommandProcessor commandProcessor = ConsoleManager.getInstance().getCommandProcessor();
		if (cCommand.getArgs() != null) {
			for (Object arg:cCommand.getArgs()) {
				if (arg instanceof Throwable) {
					commandProcessor.output("Exception " + ((Throwable)arg).getMessage() + "\n");
				} else {
					commandProcessor.output(((arg != null)?arg: "null") + "\n");
				}
			}
		}
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
		if (!_isClosed) {
			_isClosed = true;
			_consoleWindowController.dispose();
			_consoleWindowController = null;
		}
	}
	
	public void close() {
		if (!_isClosed) {
  		_isClosed = true;
      _consoleWindowController = null;
      ModuleManager.getInstance().offer(new ModuleExecutorCommand(
      		ModuleExecutor.Command.SHUTDOWN,
    			ConsoleManager.getInstance().getModuleUuid()
      ));
		}
	}
}
