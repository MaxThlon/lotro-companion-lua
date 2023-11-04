package delta.common.framework.module;

import java.util.UUID;

/**
 * @author MaxThlon
 */
public interface Module {
	UUID getUuid();
	String getName();
	
	/**
   * can module accept this event.
   * @param event .
	 * @return true if can accept event.
   */
	boolean canAccept(ModuleEvent event);

	default ModuleEvent preOffer(ModuleEvent event) {
		return event;
	}
	
  /**
   * module method to handle events.
   * @param event .
   */
	void handleEvent(ModuleEvent event);
}
