package delta.common.framework.console.dragonconsole;

import com.eleet.dragonconsole.CommandProcessor;

import delta.common.framework.console.ConsoleManager;
import delta.common.framework.console.ConsoleModuleImpl;
import delta.common.framework.module.ModuleEvent;

/**
 * @author MaxThlon
 */
public class DragonConsoleModuleImpl implements ConsoleModuleImpl {
	ConsoleWindowController _consoleWindowController;

	@Override
	public void load() {
		_consoleWindowController = new ConsoleWindowController();
		_consoleWindowController.bringToFront();
	}
	
	@Override
	public void execute(ModuleEvent event) {
		CommandProcessor commandProcessor = ConsoleManager.getInstance().getCommandProcessor();
		String eventName = (event._name != null)?event._name:"";
    switch (eventName) {
    	default:
    		if (event._args != null) {
    			for (Object arg:event._args) {
    				commandProcessor.output(arg.toString() + "\n");
    			}
    		} else {
    			commandProcessor.outputError(eventName + "\n");
    		}
    }
	}
	
	@Override
	public void unLoad() {
    _consoleWindowController.dispose();
    _consoleWindowController = null;
	}
}
