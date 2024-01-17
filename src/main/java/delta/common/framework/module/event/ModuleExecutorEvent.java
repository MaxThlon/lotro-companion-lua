package delta.common.framework.module.event;

import delta.common.framework.module.ModuleExecutor;
import delta.games.lotro.utils.events.Event;

/**
 * @author MaxThlon
 */
public class ModuleExecutorEvent extends Event {
	private ModuleExecutor.MEvent _type;
	private ModuleExecutor _moduleExecutor;
	
	/**
	 * @param type .
	 * @param moduleExecutor .
	 */
	public ModuleExecutorEvent(ModuleExecutor.MEvent type, ModuleExecutor moduleExecutor) {
		_type = type;
		_moduleExecutor = moduleExecutor;
	}

  /**
   * @return type.
   */
  public ModuleExecutor.MEvent getType() {
  	return _type;
  }
  
  /**
   * @return ModuleExecutor.
   */
  public ModuleExecutor getModuleExecutor() {
  	return _moduleExecutor;
  }
}
