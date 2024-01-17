package delta.common.framework.lua;

import java.util.UUID;

import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class LuaThreadState {
	protected UUID _threadUuid;
	protected Lua _thread;
	protected Lua.LuaError _error;

	/**
	 * @param threadUuid .
	 * @param thread .
	 */
	public LuaThreadState(UUID threadUuid, Lua thread) {
		_threadUuid = threadUuid;
		_thread = thread;
		_error = Lua.LuaError.OK;
	}
	
	/**
	 * @return thread uuid.
	 */
	public UUID getTheadUuid() {
		return _threadUuid;
	}

	/**
	 * @return lua thread.
	 */
	public Lua getThead() {
		return _thread;
	}
	
	/**
	 * @return lua error (state).
	 */
	public Lua.LuaError getError() {
		return _error;
	}

	/**
	 * @param error .
	 */
	public void setError(Lua.LuaError error) {
		_error = error;
	}
}
