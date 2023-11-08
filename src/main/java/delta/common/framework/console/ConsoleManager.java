package delta.common.framework.console;

import java.util.UUID;

import com.eleet.dragonconsole.CommandProcessor;

import delta.common.framework.console.dragonconsole.ConsoleWindowController;
import delta.common.framework.console.dragonconsole.DragonConsoleModule;
import delta.common.framework.console.logger.LoggerConsoleModule;
import delta.common.framework.module.ModuleExecutor;
import delta.common.framework.module.ModuleManager;
import delta.common.framework.module.command.ModuleExecutorCommand;
import delta.common.framework.module.event.ModuleEvent;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * PluginManager.
 * @author MaxThlon
 */
public class ConsoleManager implements GenericEventsListener<ModuleEvent> {
  private static class ConsoleManagerHolder {
    private static final ConsoleManager CONSOLE_MANAGER = new ConsoleManager();
  }
 
  //private static Logger LOGGER=Logger.getLogger(ConsoleManager.class);
  private UUID _consoleModuleUuid;
  private CommandProcessor _commandProcessor;
  private ConsoleWindowController _consoleWindowController;

  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static ConsoleManager getInstance() {
    return ConsoleManagerHolder.CONSOLE_MANAGER;
  }
  
  /**
   * Constructor.
   */
  protected ConsoleManager() {
  	_consoleModuleUuid = UUID.randomUUID();
  	_commandProcessor = new CommandProcessor() {
  		@Override
  		public void processCommand(String input) {
  			super.processCommand(input);
  			
  			input = input.trim();
  			if (input.charAt(0) == '/') {
  				String[] splits =  input.substring(1).split(" ", 2);
  				if (splits[0].length() != 0) {
  					EventsManager.invokeEvent(new ConsoleCommandEvent(splits[0], (splits.length == 2)?splits[1]:null));
  				}
  			}
  		}
  	};
  	EventsManager.addListener(ModuleEvent.class, this);
  }
  
  public UUID getModuleUuid() {
  	return _consoleModuleUuid;
  }

  public CommandProcessor getCommandProcessor() {
  	return _commandProcessor;
  }

  public void activate() {
  	if (_consoleWindowController == null) {
  		_consoleWindowController = new ConsoleWindowController(null);
  		_consoleWindowController.bringToFront();
  	}
  }
  
  public void activateByModule() {
  	ModuleManager.getInstance().addModule(new DragonConsoleModule(_consoleModuleUuid));
  }
  
  public void activateByModuleLogger() {
  	ModuleManager.getInstance().addModule(new LoggerConsoleModule(_consoleModuleUuid));
  }

	@Override
	public void eventOccurred(ModuleEvent event) {
		if ((event.getType() == ModuleExecutor.MEvent.STARTED) && (event.getModule().getUuid() == _consoleModuleUuid)) {
			ModuleManager.getInstance().offer(
					new ModuleExecutorCommand(ModuleExecutor.Command.LOAD, _consoleModuleUuid, null, null)
			);
		}
	}
}
