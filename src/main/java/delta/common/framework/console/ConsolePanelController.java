package delta.common.framework.console;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.eleet.dragonconsole.DragonConsole;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;

/**
 * Controller for a panel that displays a console.
 * @author MaxThlon
 */
public class ConsolePanelController extends AbstractPanelController
{
  /**
   * Constructor.
   * @param parent .
   */
  public ConsolePanelController(WindowController parent)
  {
    super(parent);
    JPanel panel=build(parent);
    setPanel(panel);
  }

  private JPanel build(WindowController parent)
  {
    JPanel pluginPanel=buildPluginPanel();

    return pluginPanel;
  }

  private JPanel buildPluginPanel()
  {
    JPanel panel = GuiFactory.buildPanel(new BorderLayout());
    DragonConsole dragonConsole = new DragonConsole(false, false);
    ConsoleManager.getInstance().getCommandProcessor().install(dragonConsole);
    panel.add(dragonConsole);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Inherited
    super.dispose();
  }
}
