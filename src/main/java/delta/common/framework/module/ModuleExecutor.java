package delta.common.framework.module;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import org.apache.log4j.Logger;

import delta.common.framework.module.command.ModuleCommand;
import delta.common.framework.module.command.ModuleExecutorCommand;
import delta.common.framework.module.event.ModuleEvent;
import delta.common.framework.module.event.ModuleExecutorEvent;
import delta.games.lotro.utils.events.EventsManager;

/**
 * ModuleExecutor class.
 * @author MaxThlon
 */
public class ModuleExecutor implements Runnable
{
  /**
   *
   */
  public enum Command {
    /**
     * LOAD
     */
    LOAD,
    /**
     * EXECUTE
     */
    EXECUTE,
    /**
     * UNLOAD
     */
    UNLOAD,
    /**
     * 
     */
    SHUTDOWN
  }
  /**
   *
   */
  public enum State {
  	/**
  	 * 
  	 */
  	UNLOADED,
  	/**
  	 * 
  	 */
  	LOADING,
  	/**
  	 * 
  	 */
  	PENNDING,
    /**
     * 
     */
    RUNNING,
    /**
     * 
     */
    UNLOADING,
    /**
     * 
     */
    TERMINATED
  }
  /**
   *
   */
  public enum MEvent {
  	/**
  	 * 
  	 */
  	STARTED,
  	/**
  	 * 
  	 */
  	TERMINATED
  }
  
  static final List<State> ACCEPT_COMMAND_STATE = Collections.unmodifiableList(Arrays.asList(
  		State.UNLOADED,
  		State.LOADING,
  		State.PENNDING,
  		State.RUNNING
  ));
  static final List<State> ACCEPT_UNLOAD_STATE = Collections.unmodifiableList(Arrays.asList(
  		State.LOADING,
  		State.PENNDING,
  		State.RUNNING
  ));

  private static Logger LOGGER = Logger.getLogger(ModuleExecutor.class);
  private static final int QUEUE_LIMIT = 256;

  private Module _module;
  private State _state;

  /**
   * A lock used for any changes to {@link #_commandQueue}. This will be
   * used on the main thread, so locks should be kept as brief as possible.
   */
  private final Object _queueLock;
  /**
   * The queue of {@link ModuleExecutorCommand} which should be executed when this executor is running.
   */
  private final Queue<ModuleExecutorCommand> _commandQueue;
  private final Object _idleLock = new Object();

  /**
   * Constructor.
   * @param module .
   */
  public ModuleExecutor(Module module) {
    _module = module;
    _state = State.UNLOADED;
    //_isStateLock = new ReentrantLock();
    _queueLock = new Object();
    _commandQueue = new ArrayDeque<>(4);
  }
  
  /**
   * @return handled module.
   */
  public Module getModule()
  {
    return _module;
  }
  
  private void setState(State state) {
    _state = state;
  }

  @Override
  public void run() {
  	EventsManager.invokeEvent(new ModuleExecutorEvent(MEvent.STARTED, this));
  	EventsManager.invokeEvent(new ModuleEvent(MEvent.STARTED, _module));
  	ModuleExecutorCommand command;

    while ((!Thread.interrupted()) && (_state != State.TERMINATED)) try {
    	synchronized (_queueLock) {
      	command = _commandQueue.poll();
      }
    	
    	if (command == null) synchronized (_idleLock) {
    		_idleLock.wait();
    	} else switch (command.getExecutorEvent()) {
        case LOAD:
          if (_state == State.UNLOADED) {
          	setState(State.LOADING);
          	_module.load(command.getHandlers());
          	setState(State.PENNDING);
          }
          break;
        case EXECUTE:
        	if (_state == State.PENNDING) {
          	setState(State.RUNNING);
          	for (ModuleCommand handler:command.getHandlers()) {
          		_module.execute(handler);
          	}
          	setState(State.PENNDING);
        	}
        	break;
        case UNLOAD:
        	if (ACCEPT_UNLOAD_STATE.contains(_state)) {
        		setState(State.UNLOADING);
            _module.unLoad();
          	setState(State.UNLOADED);
        	}
          break;
        case SHUTDOWN:
        	setState(State.TERMINATED);
        	clear();
        	_module.unLoad();
        	break;
      }
    } catch (Exception error) {
    	LOGGER.error("Main module thread: ", error);
    	/*ModuleManager.getInstance().offer(new ModuleExecutorCommand(
    			ExecutorEvent.ERROR,
    			_module.getUuid(),
    			new Object[] { "Main module thread: ", error },
    			null
      ));*/
    }
    clear();
    if (ACCEPT_UNLOAD_STATE.contains(_state)) {
    	_module.unLoad();
    }
    EventsManager.invokeEvent(new ModuleEvent(MEvent.TERMINATED, _module));
    EventsManager.invokeEvent(new ModuleExecutorEvent(MEvent.TERMINATED, this));
  	_module = null;
  }
  
  private void clear() {
  	synchronized (_queueLock) {
      _commandQueue.clear();
    }
  }
  
  /**
   * resume paused executor.
   */
  public void resume() {
    synchronized (_idleLock) {
    	_idleLock.notifyAll(); // Unblocks thread
    }
  }

  /**
   * If the module can accept this command
   * @param command 
   * @return true if can accept this command.
   /
  private boolean canAccept(ModuleExecutorCommand command) {
  	return ACCEPT_COMMAND_STATE.contains(_state) && _module.canAccept(command);
  }*/

  /**
   * Called when an {@link ModuleExecutorCommand } event occurred.
   * Queue an command if the executor state is in ACCEPT_COMMAND_EXECUTOR_STATE.
   * @param command command data.
   */
  public void offer(ModuleExecutorCommand command) {
  	boolean queued = false;
  	//if (canAccept(command)) {
  		synchronized (_queueLock) {
        if (_commandQueue.size() < QUEUE_LIMIT) {
        	_commandQueue.offer(_module.preOffer(command));
        	queued = true;
        }
      }
  		
  		if (queued) resume();
  	//}
  }
}
