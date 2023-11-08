package delta.common.framework.module;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
  private static class RunnerManagerHolder {
    private static final ModuleManager RUNNER_MANAGER = new ModuleManager();
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
    return RunnerManagerHolder.RUNNER_MANAGER;
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
   * add moduleExecutor.
   * @param moduleExecutor .
   */
  public void addModuleExecutor(ModuleExecutor moduleExecutor) {
  	initThreadPoolExecutor();
  	_threadPoolExecutor.submit(moduleExecutor);
  }
  
  /**
   * add module.
   * @param module .
   */
  public void addModule(Module module) {
    addModuleExecutor(new ModuleExecutor(module));
  }

  public void offer(ModuleExecutorCommand command) {
  	ModuleExecutor moduleExecutor = _moduleExecutors.get(command.getModuleUuid());
  	if (moduleExecutor != null) {
  		moduleExecutor.offer(command);
  	}
  }
  
	@Override
	public void eventOccurred(ModuleExecutorEvent event) {
		ModuleExecutor moduleExecutor = event.getModuleExecutor();
		UUID moduleUuid = moduleExecutor.getModule().getUuid();

		switch (event.getType()) {
			case STARTED:
				_moduleExecutors.put(moduleUuid, moduleExecutor);
				break;
			case INTERRUPTED:
				_moduleExecutors.remove(moduleUuid);
				break;
		}
	}
}
