package delta.common.framework.console;

import delta.common.framework.module.ModuleEvent;

/**
 * @author MaxThlon
 */
public interface ConsoleModuleImpl {
	void load();
	void execute(ModuleEvent event);
	void unLoad();
}
