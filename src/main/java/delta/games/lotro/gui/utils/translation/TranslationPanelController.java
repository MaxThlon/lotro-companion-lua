package delta.games.lotro.gui.utils.translation;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.games.lotro.lua.turbine.ui.tree.TableTreeViewCellRenderer;

/**
 * Controller for a panel that displays translations.
 * @author MaxThlon
 */
public class TranslationPanelController extends AbstractPanelController
{
  // Translator
  /*private static Translator initTranslator()
  {
    String translatorName=TranslationPanelController.class.getPackage().getName()+".translationPanel";
    TranslatorsManager translatorsMgr=TranslatorsManager.getInstance();
    Translator translator=translatorsMgr.getTranslatorByName(translatorName,true,false);
    return translator;
  }
  private static final Translator TRANSLATOR=initTranslator();*/

  // Controllers
  // Data
  private static int columnCount = 0;

  // UI
  private JPanel _panel;
  private JTree jTree;
  
  // Child windows manager

  /**
   * Constructor.
   */
  public TranslationPanelController()
  {
    _panel = buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Update values.
   */
  public void update()
  {
  }

  /**
   * Set translations to display.
   */
  public void setTranslations()
  {
  }

  private JPanel buildPanel()
  {
    //JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    JPanel translationPanel=buildTranslationPanel();

    return translationPanel;
  }

  private JPanel buildTranslationPanel()
  {
    JPanel panel = GuiFactory.buildPanel(new BorderLayout());

    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root");
    jTree = new JTree (new DefaultTreeModel(rootNode));
    jTree.setCellRenderer(new TableTreeViewCellRenderer(columnCount));
    //tree.setCellEditor(new LuaTreeCellEditor());
    jTree.setEditable(true);
    jTree.setRootVisible(false);
    jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    jTree.setShowsRootHandles(true);
    
    JScrollPane jScrollPane = GuiFactory.buildScrollPane(jTree);    
    panel.add(jScrollPane);

    TitledBorder border=GuiFactory.buildTitledBorder("Main");
    panel.setBorder(border);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
