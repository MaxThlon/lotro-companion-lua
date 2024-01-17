package delta.common.framework.i18n.event;

import delta.common.framework.translation.LocalizedEntity;
import delta.games.lotro.utils.events.Event;

/**
 * @author MaxThlon
 */
public class LocalizedEntityUpdatedEvent extends Event {
  private LocalizedEntity _localizedEntity;

  /**
   * @param localizedEntity .
   */
  public LocalizedEntityUpdatedEvent(LocalizedEntity localizedEntity) {
    _localizedEntity = localizedEntity;
  }

  /**
   * @return multilocalesTranslator.
   */
  public LocalizedEntity getLocalizedEntity() {
    return _localizedEntity;
  }
}
