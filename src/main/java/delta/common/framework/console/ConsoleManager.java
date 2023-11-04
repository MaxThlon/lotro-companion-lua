package delta.common.framework.console;

import java.util.UUID;

import com.eleet.dragonconsole.CommandProcessor;

import delta.common.framework.console.dragonconsole.ConsoleWindowController;
import delta.common.framework.console.dragonconsole.DragonConsoleModuleImpl;
import delta.common.framework.console.logger.LoggerConsoleModuleImpl;
import delta.common.framework.module.ModuleEvent;
import delta.common.framework.module.ModuleExecutor;
import delta.common.framework.module.ModuleManager;
import delta.common.framework.plugin.PluginManager;
import delta.games.lotro.utils.events.EventsManager;

/**
 * PluginManager.
 * @author MaxThlon
 */
public class ConsoleManager {
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
  			PluginManager.getInstance().processCommand(input);
  		}
  	};
  	
  }
  
  public UUID getModuleUuid() {
  	return _consoleModuleUuid;
  }

  public CommandProcessor getCommandProcessor() {
  	return _commandProcessor;
  }

  public void activate() {
  	if (_consoleWindowController == null) {
  		_consoleWindowController = new ConsoleWindowController();
  		_consoleWindowController.bringToFront();
  	}
  }
  
  public void activateByModule() {
  	
  	
  	ModuleManager.getInstance().addModule(new ConsoleModule(_consoleModuleUuid, new DragonConsoleModuleImpl()));
  	EventsManager.invokeEvent(new ModuleEvent(
    		ModuleExecutor.ExecutorEvent.LOAD,
    		_consoleModuleUuid,
    		ModuleExecutor.ExecutorEvent.LOAD.name(),
    		null
    ));
  }
  
  public void activateByModuleLogger() {
  	_consoleModuleUuid = UUID.randomUUID();
  	
  	ModuleManager.getInstance().addModule(new ConsoleModule(_consoleModuleUuid, new LoggerConsoleModuleImpl()));
  	EventsManager.invokeEvent(new ModuleEvent(
    		ModuleExecutor.ExecutorEvent.LOAD,
    		_consoleModuleUuid,
    		ModuleExecutor.ExecutorEvent.LOAD.name(),
    		null
    ));
  }
}
