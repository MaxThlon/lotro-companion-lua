package delta.common.framework.module;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import delta.common.framework.console.ConsoleManager;
import delta.common.framework.jobs.JobImpl;
import delta.common.framework.jobs.JobState;
import delta.common.framework.jobs.JobSupport;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * @author MaxThlon
 */
public class ModuleExecutor implements JobImpl, GenericEventsListener<ModuleEvent>
{
  public enum ExecutorEvent {
    LOAD,
    EXECUTE,
    UNLOAD,
    ABORT,
    ERROR
  }
  public enum ExecutorState {
  	UNLOADED,
  	LOADING,
  	PENNDING,
    RUNNING,
    UNLOADING,
  }
  
  static final List<ExecutorState> ACCEPT_EVENT_EXECUTOR_STATE = Collections.unmodifiableList(Arrays.asList(
  		ExecutorState.UNLOADED,
  		ExecutorState.LOADING,
  		ExecutorState.PENNDING,
  		ExecutorState.RUNNING
  ));
  static final List<ExecutorState> ACCEPT_UNLOAD_EXECUTOR_STATE = Collections.unmodifiableList(Arrays.asList(
  		ExecutorState.LOADING,
  		ExecutorState.PENNDING,
  		ExecutorState.RUNNING
  ));

  private static Logger LOGGER = Logger.getLogger(ModuleExecutor.class);
  private static final int QUEUE_LIMIT = 256;
  
  private Module _module;
  /**
   * Whether the plugin is currently on. This is set to false when a shutdown starts, or when turning on completes
   * (but just before the plugin is started).
   *
   * @see #_isStateLock
   */
  private volatile ExecutorState _state;
  /**
   * The lock to acquire when you need to modify the "state" of a the executor.
   */
  private final ReentrantLock _isStateLock;
  /**
   * A lock used for any changes to {@link #_eventQueue}. This will be
   * used on the main thread, so locks should be kept as brief as possible.
   */
  private final Object _queueLock;
  /**
   * The queue of events which should be executed when this plugin is on.
   */
  private final Queue<ModuleEvent> _eventQueue;

  /**
   * Constructor.
   * @param module .
   */
  public ModuleExecutor(Module module) {
    _module = module;
    _state = ExecutorState.UNLOADED;
    _isStateLock = new ReentrantLock();
    _queueLock = new Object();
    _eventQueue = new ArrayDeque<>(4);
  }

  @Override
  public String getLabel()
  {
    return _module.getName();
  }
  
  public Module getModule()
  {
    return _module;
  }
  
  private void setState(ExecutorState state) throws InterruptedException {
  	_isStateLock.lockInterruptibly();
    _state = state;
    _isStateLock.unlock();
  }
  
  /**
   * The main jobImpl function, called by {@link delta.common.framework.jobs.Job}.
   */
  @Override
  public void doIt(JobSupport support) {
    while (support.getState() == JobState.RUNNING) try {
      ModuleEvent event = null;
      synchronized (_queueLock) {
      	event = _eventQueue.poll();
      }
      
      if (event != null)
    	switch (event._executorEvent) {
        case LOAD:
          if (_state == ExecutorState.UNLOADED) {
          	setState(ExecutorState.LOADING);
            //clear();
          	_module.handleEvent(event);
          	setState(ExecutorState.PENNDING);
          }
          break;
        case EXECUTE:
        	if (_state == ExecutorState.PENNDING) {
          	setState(ExecutorState.RUNNING);
          	_module.handleEvent(event);
          	setState(ExecutorState.PENNDING);
        	}
        	break;
        case UNLOAD:
        	if (ACCEPT_UNLOAD_EXECUTOR_STATE.contains(_state)) {
        		setState(ExecutorState.UNLOADING);
            _module.handleEvent(event);
          	clear();
          	setState(ExecutorState.UNLOADED);
        	}
          break;
        case ERROR:
        	if (_state != ExecutorState.UNLOADED) {
        		EventsManager.invokeEvent(new ModuleEvent(
          			ExecutorEvent.EXECUTE,
          			ConsoleManager.getInstance().getModuleUuid(),
          			ExecutorEvent.ERROR.name(),    			
          			event._args
            ));
        		clear();
        	}
          break;
        case ABORT:
        	if (_state != ExecutorState.UNLOADED) {
        		_module.handleEvent(event);
        		clear();
        	}
        	break;
      }
    } catch (Exception error) {
    	LOGGER.error("Main module thread: ", error);
    	EventsManager.invokeEvent(new ModuleEvent(
    			ExecutorEvent.ERROR,
    			_module.getUuid(),
    			ExecutorEvent.ERROR.name(),    			
    			new Object[] { "Main module thread: ", error }
      ));
    }
  }
  
  @Override
  public boolean interrupt()
  {
  	if (_state != ExecutorState.UNLOADED) {
    	queueEvent(new ModuleEvent(ExecutorEvent.UNLOAD, _module.getUuid(), ExecutorEvent.UNLOAD.name(), null));
      return true;
    }
    return false;
  }
  
  private void clear() {
  	synchronized (_queueLock) {
      _eventQueue.clear();
    }
  }
  
  /**
   * Handle plugin events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(ModuleEvent event) {
  	if (canAccept(event)) {
  		queueEvent(event);
  	}
  }

  protected boolean canAccept(ModuleEvent event) {
  	return _module.canAccept(event);
  }
  
  /**
   * Queue an event if the executor state is in ACCEPT_EVENT_EXECUTOR_STATE.
   * @param event an module event;
   */
  private void queueEvent(ModuleEvent event) {
  	if (ACCEPT_EVENT_EXECUTOR_STATE.contains(_state)) {
      synchronized (_queueLock) {
        if (_eventQueue.size() < QUEUE_LIMIT) {
        	_eventQueue.offer(event);
        }
      }
  	}
  }
}
