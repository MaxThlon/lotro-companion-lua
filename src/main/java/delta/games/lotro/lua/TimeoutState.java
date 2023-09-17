package delta.games.lotro.lua;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.concurrent.GuardedBy;

/**
 * TimeoutState for LuaRunner.
 * 
 * @author MaxThlon
 */
public class TimeoutState {

  /**
   * The total time a runner is allowed to run before aborting in nanoseconds.
   */
  static final long TIMEOUT = TimeUnit.MILLISECONDS.toNanos(7000);

  /**
   * The time the runner is allowed to run after each abort in nanoseconds.
   */
  static final long ABORT_TIMEOUT = TimeUnit.MILLISECONDS.toNanos(1500);

  /**
   * The error message to display when we trigger an abort.
   */
  public static final String ABORT_MESSAGE = "Too long without yielding";

  @GuardedBy("this")
  private final List<Runnable> listeners = new ArrayList<>(0);

  private boolean paused;
  private boolean softAbort;
  private volatile boolean hardAbort;

  /**
   * When the cumulative time would have started had the whole event been processed in one go.
   */
  private long cumulativeStart;

  /**
   * How much cumulative time has elapsed. This is effectively {@code cumulativeStart - currentStart}.
   */
  private long cumulativeElapsed;

  /**
   * When this execution round started.
   */
  private long currentStart;

  /**
   * When this execution round should look potentially be paused.
   */
  private long currentDeadline;

  public TimeoutState() {
  }
  
  long nanoCumulative() {
    return System.nanoTime() - cumulativeStart;
  }
  
  long nanoCurrent() {
      return System.nanoTime() - currentStart;
  }

  /**
   * Recompute the {@link #isSoftAborted()} and {@link #isPaused()} flags.
   */
  public synchronized void refresh() {
      // Important: The weird arithmetic here is important, as nanoTime may return negative values, and so we
      // need to handle overflow.
      long now = System.nanoTime();
      boolean changed = false;
      if (!paused && (paused = currentDeadline - now <= 0)) { // now >= currentDeadline
          changed = true;
      }
      if (!softAbort && (softAbort = now - cumulativeStart - TIMEOUT >= 0)) { // now - cumulativeStart >= TIMEOUT
          changed = true;
      }

      if (changed) updateListeners();
  }
  
  /**
   * Whether we should pause execution of this runner.
   * <p>
   * This is determined by whether we've consumed our time slice, and if there are other runner waiting to perform
   * work.
   *
   * @return Whether we should pause execution.
   */
  public boolean isPaused() {
      return paused;
  }
  
  /**
   * If the runner should be passively aborted.
   *
   * @return {@code true} if we should throw a timeout error.
   */
  public boolean isSoftAborted() {
      return softAbort;
  }

  /**
   * Determine if the runner should be forcibly aborted.
   *
   * @return {@code true} if the runner should be forcibly shut down.
   */
  public boolean isHardAborted() {
      return hardAbort;
  }
  
  /**
   * If the runner should be forcibly aborted.
   */
  void hardAbort() {
    softAbort = hardAbort = true;
    synchronized (this) {
        updateListeners();
    }
  }
  
  /**
   * Start the current and cumulative timers again.
   */
  void startTimer() {
    long now = System.nanoTime();
    currentStart = now;
    currentDeadline = now + TIMEOUT;
    // Compute the "nominal start time".
    cumulativeStart = now - cumulativeElapsed;
  }
  
  /**
   * Pauses the cumulative time, to be resumed by {@link #startTimer()}.
   *
   * @see #nanoCumulative()
   */
  synchronized void pauseTimer() {
      // We set the cumulative time to difference between current time and "nominal start time".
      cumulativeElapsed = System.nanoTime() - cumulativeStart;
      paused = false;
      updateListeners();
  }

  /**
   * Resets the cumulative time and resets the abort flags.
   */
  synchronized void stopTimer() {
      cumulativeElapsed = 0;
      paused = softAbort = hardAbort = false;
      updateListeners();
  }

  @GuardedBy("this")
  private void updateListeners() {
      for (Runnable listener : listeners) listener.run();
  }

  public synchronized void addListener(Runnable listener) {
      Objects.requireNonNull(listener, "listener cannot be null");
      listeners.add(listener);
      listener.run();
  }

  public synchronized void removeListener(Runnable listener) {
      Objects.requireNonNull(listener, "listener cannot be null");
      listeners.remove(listener);
  }
}
