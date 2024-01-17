package delta.common.framework.lua.command;

import java.nio.file.Path;

import javax.annotation.Nullable;

import delta.common.framework.module.command.ModuleCommand;

/**
 * @author MaxThlon
 */
public class LuaModuleCommandPath implements ModuleCommand {
	private @Nullable Path[] _paths;
	
	/**
	 * @param paths .
	 */
	public LuaModuleCommandPath(Path... paths) {
		_paths = paths;
	}
	
	/**
	 * @return paths.
	 */
	public @Nullable Path[] getPaths() {
		return _paths;
	}
}
