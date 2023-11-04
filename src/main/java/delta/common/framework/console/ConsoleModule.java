package delta.common.framework.console;

import java.io.PrintWriter;
import java.util.UUID;

import delta.common.framework.module.Module;
import delta.common.framework.module.ModuleEvent;
import delta.common.framework.module.ModuleExecutor;

/**
 * ConsoleModule.
 * 
 * @author MaxThlon
 */
public class ConsoleModule implements Module {
  private volatile boolean _isClosed = false;
  private UUID _uuid;
  ConsoleModuleImpl _consoleModuleImpl;

  public ConsoleModule(UUID uuid, ConsoleModuleImpl consoleModuleImpl) {
  	_uuid = uuid;
  	_consoleModuleImpl = consoleModuleImpl;
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
  	return true; //event._sender == _uuid;
  }

  @Override
  public ModuleEvent preOffer(ModuleEvent event) {
  	return (event._sender == _uuid)?event:
  		new ModuleEvent(
  			ModuleExecutor.ExecutorEvent.EXECUTE,
  			_uuid,
  			event._name,
    		new Object[] { event }
    );
  }
  
  @Override
  public void handleEvent(ModuleEvent event) {
    if (_isClosed) throw new IllegalStateException("ConsoleModule has been closed");

		switch (event._executorEvent) {
			case LOAD: {
				_consoleModuleImpl.load();
        break;
			}
			
			case EXECUTE:
				_consoleModuleImpl.execute(event);
				break;

      case UNLOAD:
      case ABORT:
        if (!_isClosed) {
          _isClosed = true;
          _consoleModuleImpl.unLoad();
      	}
        break;
      case ERROR:
        break;
		}
  }
}
