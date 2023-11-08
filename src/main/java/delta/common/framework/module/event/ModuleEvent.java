package delta.common.framework.module.event;

import delta.common.framework.module.Module;
import delta.common.framework.module.ModuleExecutor;
import delta.games.lotro.utils.events.Event;

/**
 * @author MaxThlon
 */
public class ModuleEvent extends Event {
	private ModuleExecutor.MEvent _type;
	private Module _module;
	
	public ModuleEvent(ModuleExecutor.MEvent type, Module module) {
		_type = type;
		_module = module;
	}

  public ModuleExecutor.MEvent getType() {
  	return _type;
  }
  
  public Module getModule() {
  	return _module;
  }
}
