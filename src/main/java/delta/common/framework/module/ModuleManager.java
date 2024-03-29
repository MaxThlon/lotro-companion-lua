package delta.common.framework.module;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import delta.common.framework.module.ModuleExecutor.Command;
import delta.common.framework.module.command.ModuleExecutorCommand;
import delta.common.framework.module.event.ModuleExecutorEvent;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * PluginManager.
 * @author MaxThlon
 */
public class ModuleManager implements GenericEventsListener<ModuleExecutorEvent> {
  private static class ModuleManagerHolder {
    private static final ModuleManager MODULE_MANAGER = new ModuleManager();
  }
  //private static final Logger LOGGER=Logger.getLogger(ModuleManager.class);

  private ConcurrentHashMap<UUID, ModuleExecutor> _moduleExecutors;
  private ThreadPoolExecutor _threadPoolExecutor;

  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static ModuleManager getInstance()
  {
    return ModuleManagerHolder.MODULE_MANAGER;
  }
  
  /**
   * Constructor.
   */
  protected ModuleManager() {
  	EventsManager.addListener(ModuleExecutorEvent.class, this);
  }

  protected void initThreadPoolExecutor() {
    if (_threadPoolExecutor == null) {
    	_moduleExecutors = new ConcurrentHashMap<>();
    	_threadPoolExecutor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
    }
  }
  
  /**
   * Stop all modules, moules threads and clear ModuleManager data.
   */
  public void closeThreadPoolExecutor() {
    if (_threadPoolExecutor != null) {
    	_threadPoolExecutor.shutdown(); // Disable new tasks from being submitted
    	ModuleExecutorCommand command = new ModuleExecutorCommand(Command.SHUTDOWN, null);

      try {
      	_moduleExecutors.values().parallelStream().forEach(moduleExecutor -> moduleExecutor.offer(command));

        if (!_threadPoolExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
        	_threadPoolExecutor.shutdownNow(); // Cancel currently executing tasks
          if (!_threadPoolExecutor.awaitTermination(2, TimeUnit.SECONDS))
              System.err.println("Pool did not terminate");
        }
      } catch (InterruptedException ie) {
        _threadPoolExecutor.shutdownNow();
        Thread.currentThread().interrupt();
      }

      _threadPoolExecutor = null;
      _moduleExecutors = null;
    }
  }

  /**
   * add moduleExecutor with uuid as reference, if uuid is not already used.
   * @param moduleUuid .
   * @param moduleExecutor .
   */
  public void addModuleExecutor(UUID moduleUuid, ModuleExecutor moduleExecutor) {
  	initThreadPoolExecutor();
  	if (!containModule(moduleUuid)) {
    	_moduleExecutors.put(moduleUuid, moduleExecutor);
    	_threadPoolExecutor.submit(moduleExecutor);
  	}
  }
  
  /**
   * @param moduleUuid
   * @return true if module is loaded.
   */
  public boolean containModule(UUID moduleUuid) {
  	return (_moduleExecutors != null) && (_moduleExecutors.get(moduleUuid) != null);
  }

  /**
   * add module.
   * @param module .
   */
  public void addModule(Module module) {
  	UUID moduleUuid=module.getUuid();
  	if (!containModule(moduleUuid)) {
  		addModuleExecutor(moduleUuid, new ModuleExecutor(module));
  	}
  }
  
  /**
   * find module by uuid.
   * @param moduleUuid .
   * @return return corresponding module.
   */
  public @Nullable Module findModule(UUID moduleUuid) {
  	if (_moduleExecutors != null) {
  		ModuleExecutor moduleExecutor = _moduleExecutors.get(moduleUuid);
  		if (moduleExecutor != null) {
  			return moduleExecutor.getModule();
  		}
  	}
  	return null;
  }
  
  /**
   * offer a command to a moduleExecutor with same uuid.
   * @param command .
   */
  public void offer(ModuleExecutorCommand command) {
  	if (_moduleExecutors != null) {
    	ModuleExecutor moduleExecutor = _moduleExecutors.get(command.getModuleUuid());
    	if (moduleExecutor != null) {
    		moduleExecutor.offer(command);
    	}
  	}
  }
  
	@Override
	public void eventOccurred(ModuleExecutorEvent event) {
		ModuleExecutor moduleExecutor = event.getModuleExecutor();
		if (moduleExecutor != null) {
  		UUID moduleUuid = moduleExecutor.getModule().getUuid();
  
  		switch (event.getType()) {
  			case STARTED:
  				//_moduleExecutors.put(moduleUuid, moduleExecutor);
  				break;
  			case TERMINATED: {
  				_moduleExecutors.remove(moduleUuid);
  				break;
  			}
  		}
		}
	}
	
	/**
   * Release all managed resources.
   */
  public void dispose()
  {
  	closeThreadPoolExecutor();
  	EventsManager.removeListener(ModuleExecutorEvent.class, this);
  }
}
