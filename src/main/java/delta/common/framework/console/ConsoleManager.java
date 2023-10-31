package delta.common.framework.console;

import java.util.UUID;

import com.eleet.dragonconsole.CommandProcessor;

import delta.common.framework.module.ModuleEvent;
import delta.common.framework.module.ModuleExecutor;
import delta.common.framework.module.ModuleManager;
import delta.games.lotro.utils.events.EventsManager;

/**
 * PluginManager.
 * @author MaxThlon
 */
public class ConsoleManager {
  private static class ConsoleManagerHolder {
    private static final ConsoleManager CONSOLE_MANAGER = new ConsoleManager();
  }

  private ConsoleWindowController _consoleWindowController;
  private CommandProcessor _commandProcessor;
  private UUID _consoleModuleUuid;

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
  	_commandProcessor = new CommandProcessor();
  }
  
  public CommandProcessor getCommandProcessor() {
  	return _commandProcessor;
  }
  
  public UUID getModuleUuid() {
  	return _consoleModuleUuid;
  }

  public void activate() {
  	
  	if (_consoleWindowController == null) {
  		_consoleWindowController = new ConsoleWindowController();
  		_consoleWindowController.bringToFront();
  	}
  }
  
  public void activateByModule() {
  	_consoleModuleUuid = UUID.randomUUID();
  	
  	ModuleManager.getInstance().addModule(new ConsoleModule(_consoleModuleUuid));
  	EventsManager.invokeEvent(new ModuleEvent(
    		ModuleExecutor.ExecutorEvent.LOAD,
    		_consoleModuleUuid,
    		ModuleExecutor.ExecutorEvent.LOAD.name(),
    		null
    ));
  }
}
