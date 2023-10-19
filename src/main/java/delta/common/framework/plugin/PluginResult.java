package delta.common.framework.plugin;

import javax.annotation.Nullable;

import delta.common.framework.jobs.JobResult;

/**
 * The result of executing an action on a plugin.
 */
public final class PluginResult {
  /**
   * A successful complete execution.
   */
  public static final PluginResult OK = new PluginResult(false, false, null);

  /**
   * A successful paused execution.
   */
  public static final PluginResult PAUSE = new PluginResult(false, true, null);

  /**
   * An execution which timed out.
   */
  public static final PluginResult TIMEOUT = new PluginResult(true, false, TimeoutState.ABORT_MESSAGE);
  
  /**
   * An error with no user-friendly error message
   */
  public static final PluginResult GENERIC_ERROR = new PluginResult(true, false, null);

  private final boolean _error;
  private final boolean _pause;
  private final @Nullable String _message;
  private final @Nullable JobResult<?> _jobResult;

  private PluginResult(boolean error, boolean pause, @Nullable String message) {
    _error = error;
    _pause = pause;
    _message = message;
    _jobResult = null;
  }

  private PluginResult(boolean error, boolean pause, @Nullable String message, @Nullable JobResult<?> jobResult) {
    _error = error;
    _pause = pause;
    _message = message;
    _jobResult=jobResult;
  }

  public static PluginResult error(String error) {
    return new PluginResult(true, false, error);
  }

  public static PluginResult error(Exception error) {
    return new PluginResult(true, false, error.getMessage());
  }
  
  public static PluginResult data(@Nullable Object data) {
    JobResult<Object> jobResult=new JobResult<Object>();
    jobResult.setResult(data);
    return new PluginResult(true, false, null, jobResult);
  }

  public boolean isError() {
    return _error;
  }

  public boolean isPause() {
    return _pause;
  }

  @Nullable
  public String getMessage() {
     return _message;
  }
  
  public boolean isData() {
    return _jobResult!=null;
  }
  
  @Nullable
  public JobResult<?> getData() {
    return _jobResult;
  }
}

