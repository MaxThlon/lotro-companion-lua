package delta.games.lotro.lua.utils;

import java.util.concurrent.TimeUnit;

public class TimeoutState {

  /**
   * The total time a task is allowed to run before aborting in nanoseconds.
   */
  static final long TIMEOUT = TimeUnit.MILLISECONDS.toNanos(7000);

  /**
   * The time the task is allowed to run after each abort in nanoseconds.
   */
  static final long ABORT_TIMEOUT = TimeUnit.MILLISECONDS.toNanos(1500);

  /**
   * The error message to display when we trigger an abort.
   */
  public static final String ABORT_MESSAGE = "Too long without yielding";

  private boolean paused;
  private boolean softAbort;
  private volatile boolean hardAbort;

  /**
   * Whether we should pause execution of this machine.
   * <p>
   * This is determined by whether we've consumed our time slice, and if there are other computers waiting to perform
   * work.
   *
   * @return Whether we should pause execution.
   */
  public boolean isPaused() {
      return paused;
  }
  
  /**
   * If the machine should be passively aborted.
   *
   * @return {@code true} if we should throw a timeout error.
   */
  public boolean isSoftAborted() {
      return softAbort;
  }

  /**
   * Determine if the machine should be forcibly aborted.
   *
   * @return {@code true} if the machine should be forcibly shut down.
   */
  public boolean isHardAborted() {
      return hardAbort;
  }
}
