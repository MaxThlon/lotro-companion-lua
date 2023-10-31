package delta.common.framework.console;

import java.io.PrintWriter;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.eleet.dragonconsole.CommandProcessor;

import delta.common.framework.module.Module;
import delta.common.framework.module.ModuleEvent;

/**
 * ConsoleModule.
 * 
 * @author MaxThlon
 */
public class ConsoleModule implements Module {
  private static Logger LOGGER = Logger.getLogger(ConsoleModule.class);

  private volatile boolean _isClosed = false;
  private UUID _uuid;
  ConsoleWindowController _consoleWindowController;

  public ConsoleModule(UUID uuid) {
  	_uuid = uuid;
  }
  
  @Override
	public UUID getUuid() {
		return _uuid;
	}
  
	@Override
	public String getName() {
		return "ConsoleModule";
	}

	public PrintWriter getWriter() {
  	return null;
  }

  @Override
  public boolean canAccept(ModuleEvent event) {
  	return event._sender == _uuid;
  }

  @Override
  public void handleEvent(ModuleEvent event) {
  	LOGGER.error(event._name);
    if (_isClosed) throw new IllegalStateException("ConsoleModule has been closed");

    if (event._sender == _uuid) {
  		switch (event._executorEvent) {
  			case LOAD: {
  				_consoleWindowController = new ConsoleWindowController();
  				_consoleWindowController.bringToFront();
          break;
  			}
  			
  			case EXECUTE:
  				CommandProcessor commandProcessor = ConsoleManager.getInstance().getCommandProcessor();
  				String eventName = (event._name != null)?event._name:"";
  		    switch (eventName) {
  		    	default:
  		    		if (event._args != null) {
  		    			for (Object arg:event._args) {
  		    				commandProcessor.output(arg.toString());
  		    			}
  		    		} else {
  		    			commandProcessor.outputError(eventName);
  		    		}
  		    }
  				break;
  
        case UNLOAD:
        case ABORT:
          if (!_isClosed) {
            _isClosed = true;
            _consoleWindowController.dispose();
            _consoleWindowController = null;
        	}
          break;
  
        
        case ERROR:
          break;
  		}
    } else {
    	CommandProcessor commandProcessor = ConsoleManager.getInstance().getCommandProcessor();
    	String eventName = (event._name != null)?event._name:"Unknown";
    	commandProcessor.output(eventName + ": ");
    	if (event._args != null) {
  			for (Object arg:event._args) {
  				commandProcessor.output(eventName + arg.toString());
  			}
  		}
    }
  }
}
