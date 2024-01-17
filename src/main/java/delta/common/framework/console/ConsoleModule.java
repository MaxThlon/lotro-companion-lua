package delta.common.framework.console;

import java.util.UUID;

import delta.common.framework.module.Module;

/**
 * ConsoleModule.
 * 
 * @author MaxThlon
 */
public abstract class ConsoleModule implements Module {
  /**
   *
   */
  public enum Command {
    /**
     * 
     */
    PRINT,
    /**
     * 
     */
    CLEAR
  }

  private UUID _uuid;

  /**
   * @param uuid
   */
  public ConsoleModule(UUID uuid) {
    _uuid = uuid;
  }

  @Override
  public UUID getUuid() {
    return _uuid;
  }

  @Override
  public String getName() {
    return "ConsoleModule";
  }

  /*@Override
  public ModuleExecutorCommand preOffer(ModuleExecutorCommand command) {
    return (command.getModuleUuid() == _uuid)?command:
      new ModuleExecutorCommand(
        ModuleExecutor.Command.EXECUTE,
        _uuid,
        new Object[] { event },
        null
    );
  }  */
}
