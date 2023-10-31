package delta.common.framework.module;

import java.util.UUID;

import delta.common.framework.jobs.JobImpl;
import delta.common.framework.jobs.JobPool;
import delta.common.framework.jobs.JobState;
import delta.common.framework.jobs.MultiThreadedJobExecutor;
import delta.common.framework.jobs.Worker;
import delta.games.lotro.utils.events.EventsManager;

/**
 * PluginManager.
 * @author MaxThlon
 */
public class ModuleManager {
  private static class RunnerManagerHolder {
    private static final ModuleManager RUNNER_MANAGER = new ModuleManager();
  }
  //private static final Logger LOGGER=Logger.getLogger(ModuleManager.class);

  public UUID MANAGER_UUID = UUID.fromString("04c6c3cb-a903-43ba-b384-ec0b2370906a");
  private MultiThreadedJobExecutor _jobExecutor;

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
  protected ModuleManager() {}

  protected void initJobExecutor() {
    if (_jobExecutor == null) {
      _jobExecutor = new MultiThreadedJobExecutor(new JobPool(), 2);
      _jobExecutor.start();
    }
  }
  
  public void closeJobExecutor() {
    if (_jobExecutor != null) {
    	for (int i=0; i != _jobExecutor.getNbThreads(); i++) {
    		Worker worker = _jobExecutor.getWorker(i);
    		if ((worker != null) && (worker.getCurrentJob() != null)) {
    			JobImpl jobImpl = worker.getCurrentJob().getJobImpl();
    			if (jobImpl instanceof ModuleExecutor) {
    				ModuleExecutor moduleExecutor = (ModuleExecutor)jobImpl;

    				EventsManager.invokeEvent(new ModuleEvent(
    		  			ModuleExecutor.ExecutorEvent.ABORT,
    		  			moduleExecutor.getModule().getUuid(),
    		  			ModuleExecutor.ExecutorEvent.ABORT.name(),
    		  			null
    		    ));
    			}
    			worker.getCurrentJob().setState(JobState.FINISHED);
    		}
    	}
    	
      _jobExecutor.waitForCompletion();
      _jobExecutor = null;
    }
  }

  /**
   * add moduleExecutor.
   * @param moduleExecutor .
   */
  public void addModuleExecutor(ModuleExecutor moduleExecutor) {
    initJobExecutor();
    _jobExecutor.getPool().addJob(moduleExecutor);
    EventsManager.addListener(ModuleEvent.class, moduleExecutor);
  }
  
  /**
   * add module.
   * @param module .
   */
  public void addModule(Module module) {
    addModuleExecutor(new ModuleExecutor(module));
  }
}
