package delta.common.framework.lua.command;

import java.util.UUID;

import javax.annotation.Nullable;

/**
 * @author MaxThlon
 */
public class LuaMTCCommandMethod extends LuaMTCCommand {
	private Object _object;

	/**
	 * @param threadUuid .
	 * @param object .
	 * @param method .
	 * @param args .
	 */
	public LuaMTCCommandMethod(UUID threadUuid,
														 Object object,
														 String method,
														 @Nullable String... args) {
		super(threadUuid, method, args);
		_object = object;
	}

	/**
	 * @return object.
	 */
	public Object getObject() {
		return _object;
	}
}
