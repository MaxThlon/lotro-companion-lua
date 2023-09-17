package delta.games.lotro.lua;

import static org.squiddev.cobalt.ValueFactory.valueOf;

import java.awt.Dimension;

import javax.swing.JInternalFrame;
import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.function.LibFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.games.lotro.gui.utils.translation.TranslationPanelController;
import delta.games.lotro.lua.turbine.ui.UI;

/**
 * LuaLotroCompanion library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaLotroCompanion {
  
  // UI
  private static JInternalFrame jInternalFrame = null;
  private static TranslationPanelController _translationPanelController;

  public LuaLotroCompanion() {}
  

  
  public static void add(LuaState state, LuaTable env) throws LuaError {
    LibFunction.setGlobalLibrary(state, env, "LotroCompanion", RegisteredFunction.bind(new RegisteredFunction[]{
        RegisteredFunction.of("Activate", LuaLotroCompanion::Activate),
        RegisteredFunction.of("AddTranslation", LuaLotroCompanion::AddTranslation),
        RegisteredFunction.of("TranslationPopulate", LuaLotroCompanion::TranslationPopulate)
    }));
  }

  public static LuaValue Activate(LuaState state, LuaValue value) {
    //LuaTable languages = value.checkTable();
    //columnCount = languages.count();
    jInternalFrame = new JInternalFrame("Translation");
    jInternalFrame.setOpaque(false);
    jInternalFrame.setPreferredSize(new Dimension(400,700));
    
    _translationPanelController = new TranslationPanelController();
    
    jInternalFrame.add(_translationPanelController.getPanel());

    UI.jDesktopPane.add(jInternalFrame);
    jInternalFrame.pack();
    jInternalFrame.setVisible(true);
    jInternalFrame.toFront();
    return Constants.NIL;
  }

  /*public DefaultMutableTreeNode searchNode(String translateId) {
    DefaultTreeModel treeModel = (DefaultTreeModel)jTree.getModel();
    DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)treeModel.getRoot();
    
    DefaultMutableTreeNode node = null;
    Enumeration<TreeNode> e = rootNode.breadthFirstEnumeration();
    while (e.hasMoreElements()) {
      node = (DefaultMutableTreeNode)e.nextElement();
      if (nodeStr.equals(node.getUserObject().toString())) {
        return node;
      }
    }
    return null;
  }*/

  public static LuaValue AddTranslation(LuaState state,
                                        LuaValue language,
                                        LuaValue translateId,
                                        LuaValue translateValue) {
    /*DefaultTreeModel treeModel = (DefaultTreeModel)jTree.getModel();
    DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)treeModel.getRoot();
    
    int languageId = language.checkInteger();
    if (languageId > 268435456) languageId -= 268435456;
    

    LuaValue[] translations = new LuaValue[columnCount];
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(
        translations
    );
    treeModel.insertNodeInto(
        treeNode,
        rootNode,
        rootNode.getChildCount()
    );
    jTree.expandPath(new TreePath(rootNode.getPath()));*/
    return Constants.NIL;
  }
  
  public static LuaValue TranslationPopulate(LuaState state, LuaValue luaTranslation) throws LuaError {
    String[] translations = new String[] {"Ani", "Sam", "Joe"};
    String _english = translations[0];

    int i = 0;
    LuaTable _luaTranslation = luaTranslation.checkTable();
    for (String translation: translations) {
      _luaTranslation.rawset(_english, valueOf((i++ == 0)?"":translation));
    }

    return Constants.NIL;
  }
}
