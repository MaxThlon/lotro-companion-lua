package delta.common.framework.plugin;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Nullable;

import org.apache.log4j.Logger;

import com.eleet.dragonconsole.CommandProcessor;

import delta.common.framework.jobs.JobImpl;
import delta.common.framework.jobs.JobState;
import delta.common.framework.jobs.JobSupport;
/**
 * @author MaxThlon
 */
public class PluginExecutor implements JobImpl
{
  private enum StateCommand {
    TURN_ON,
    SHUTDOWN,
    ABORT,
    ERROR,
  }
  
  private static final Logger LOGGER=Logger.getLogger(PluginExecutor.class);
  private static final int QUEUE_LIMIT = 256;
  private PluginManager _pluginManager;
  private PluginImpl _pluginImpl;
  
  /**
   * Whether the plugin is currently on. This is set to false when a shutdown starts, or when turning on completes
   * (but just before the plugin is started).
   *
   * @see #isOnLock
   */
  private volatile boolean isOn = false;

  /**
   * The lock to acquire when you need to modify the "on state" of a plugin.
   * <p>
   * We hold this lock when running any command, and attempt to hold it when updating APIs. This ensures you don't
   * update APIs while also starting/stopping them.
   *
   * @see #isOn
   * @see #turnOn()
   * @see #shutdown()
   */
  private final ReentrantLock isOnLock = new ReentrantLock();
  /**
   * A lock used for any changes to {@link #_eventQueue}, {@link #_command}. This will be
   * used on the main thread, so locks should be kept as brief as possible.
   */
  private final Object queueLock = new Object();
  /**
   * The command that should execute on the plugin thread.
   */
  private volatile @Nullable StateCommand _command;
  /**
   * The queue of events which should be executed when this plugin is on.
   */
  private final Queue<PluginEvent> _eventQueue = new ArrayDeque<>(4);
  /**
   * Whether we interrupted an event and so should resume it instead of executing another task.
   *
   * @see #doIt(JobSupport support)
   * @see #resumePlugin(PluginEvent event)
   */
  private boolean _interruptedEvent = false;
  /**
   * Whether this executor has been closed, and will no longer accept any incoming commands or events.
   *
   * @see #queueStop(boolean, boolean)
   */
  private boolean _closed;

  /**
   * Constructor.
   * @param pluginManager .
   */
  public PluginExecutor(PluginManager pluginManager) {
    _pluginManager=pluginManager;
    _pluginImpl=null;
  }

  private void turnOn() throws InterruptedException {
    isOnLock.lockInterruptibly();
    try {
        _interruptedEvent = false;
        synchronized (queueLock) {
          _eventQueue.clear();
        }

        // Init plugin
        _pluginImpl=_pluginManager.createLuaRunner();
        if (_pluginImpl == null) {
            shutdown();
            return;
        }

        // Initialisation has finished, so let's mark ourselves as on.
        isOn = true;
    } catch (Exception e) {
      LOGGER.error("Message: " + e.getMessage());
    } finally {
        isOnLock.unlock();
    }

    // Now actually start the computer, now that everything is set up.
    resumePlugin(null);
  }
  
  private void shutdown() throws InterruptedException {
    isOnLock.lockInterruptibly();
    try {
      isOn = false;
      _interruptedEvent = false;
      synchronized (queueLock) {
        _eventQueue.clear();
      }

      // Shutdown plugin
      if (_pluginImpl != null) {
        _pluginImpl.close();
        _pluginImpl = null;
      }
    } catch (Exception e) {
      LOGGER.error("Message: " + e.getMessage());
    } finally {
        isOnLock.unlock();
    }
  }

  
  
  @Override
  public String getLabel()
  {
    // ToDo Auto-generated method stub
    return null;
  }
  
  /**
   * The main job function, called by {@link delta.common.framework.jobs.Job}.
   * <p>
   * This either executes a {@link StateCommand} or attempts to run an event
   *
   * @see #_command
   * @see #_eventQueue
   */
  @Override
  public void doIt(JobSupport support) {
    synchronized (queueLock) {
      _command=StateCommand.TURN_ON;
    }
    
    try {
      while (support.getState() == JobState.RUNNING) {
        _interruptedEvent = false;
        /*if (_interruptedEvent && !_closed) {
          _interruptedEvent = false;
          if (_pluginImpl != null) {
            resumePlugin(null);
            return;
          }
        }*/

        StateCommand command;
        PluginEvent event = null;
        synchronized (queueLock) {
          command = _command;
          _command = null;
  
          // If we've no command, pull something from the event queue instead.
          if (command == null) {
            if (!isOn) {
              // We're not on and had no command, but we had work queued. This should never happen, so clear
              // the event queue just in case.
              _eventQueue.clear();
              return;
            }

            event = _eventQueue.poll();
          }
        }
        
        if (command != null) {
          switch (command) {
            case TURN_ON -> {
              if (!isOn) turnOn();
              break;
            }
            case SHUTDOWN -> {
              if (isOn) shutdown();
              break;
            }
            case ABORT -> {
              if (isOn) {
                displayFailure("Error running plugin", TimeoutState.ABORT_MESSAGE);
                shutdown();
              }
              break;
            }
            case ERROR -> {
              if (isOn) {
                displayFailure("Error running plugin", "An internal error occurred, see logs.");
                shutdown();
              }
              break;
            }
          }
        } else if (event != null) {
          resumePlugin(event);
        }
      }
    } catch (Exception e) {
      LOGGER.error(e);
    }
  }
  
  /**
   * Queue an event if the computer is on.
   * @param event The event's name
   * @param args The event's arguments
   */
  void queueEvent(String event, @Nullable Object[] args) {
      // Events should be skipped if we're not on.
      if (!isOn) return;

      synchronized (queueLock) {
          // And if we've got some command in the pipeline, then don't queue events - they'll
          // probably be disposed of anyway.
          // We also limit the number of events which can be queued.
          if (_closed || _command != null || _eventQueue.size() >= QUEUE_LIMIT) return;

          _eventQueue.offer(new PluginEvent(event, args));
      }
  }

  @Override
  public boolean interrupt()
  {
    if (!_closed) {
      synchronized (queueLock) {
        _command = StateCommand.ABORT;
      }
    }
    return true;
  }

  private void resumePlugin(@Nullable PluginEvent event) throws InterruptedException {
    PluginResult result=_pluginImpl.handleEvent(event);
    _interruptedEvent=result.isPause();
    if (!result.isError()) return;

    displayFailure("Error running plugin", result.getMessage());
    shutdown();
  }
  
  private void displayFailure(String message, @Nullable String extra) {
    CommandProcessor terminal=_pluginImpl.getCommandProcessor();

    // Display our primary error message
    terminal.outputError(message);

    if (extra != null) {
        terminal.outputError(extra);
    }
  }
}
