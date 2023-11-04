package delta.common.framework.console.logger;

import org.apache.log4j.Logger;

import delta.common.framework.console.ConsoleModuleImpl;
import delta.common.framework.module.ModuleEvent;

/**
 * @author MaxThlon
 */
public class LoggerConsoleModuleImpl implements ConsoleModuleImpl {
	private static Logger LOGGER = Logger.getLogger(LoggerConsoleModuleImpl.class);

	@Override
	public void load() {
		/* empty */
	}
	
	@Override
	public void execute(ModuleEvent event) {
		String eventName = (event._name != null)?event._name:"";
    switch (eventName) {
    	default:
    		logEvent(event);
    }
	}
	
	void logEvent(ModuleEvent event) {
		if (event._args != null) {
			for (Object arg:event._args) {
				if (arg instanceof ModuleEvent) {
					logEvent((ModuleEvent)arg);
				} else if (arg instanceof Exception) {
					LOGGER.error("Exception", (Exception)arg);
				} else {
					LOGGER.error((arg != null)?arg.toString(): "null");
				}
			}
		} else {
			LOGGER.error(event._name);
		}
	}
	
	@Override
	public void unLoad() {
		/* empty */
	}
}
