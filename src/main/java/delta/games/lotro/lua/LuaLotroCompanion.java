package delta.games.lotro.lua;

import java.awt.Dimension;

import javax.swing.JInternalFrame;

import party.iroiro.luajava.Lua;
import party.iroiro.luajava.value.LuaValue;

/**
 * LuaLotroCompanion library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaLotroCompanion {
  
  // UI
  private static JInternalFrame jInternalFrame = null;
  //private static TranslationPanelController _translationPanelController;

  public LuaLotroCompanion() {}
  
  public static void add(Lua lua, LuaValue env) {
    /*LibFunction.setGlobalLibrary(state, env, "LotroCompanion", RegisteredFunction.bind(new RegisteredFunction[]{
        RegisteredFunction.of("Activate", LuaLotroCompanion::Activate),
        RegisteredFunction.of("AddTranslation", LuaLotroCompanion::AddTranslation),
        RegisteredFunction.of("TranslationPopulate", LuaLotroCompanion::TranslationPopulate)
    }));*/
  }

  public static int Activate(Lua lua, LuaValue value) {
    //LuaValue languages = value.checkTable();
    //columnCount = languages.count();
    jInternalFrame = new JInternalFrame("Translation");
    jInternalFrame.setOpaque(false);
    jInternalFrame.setPreferredSize(new Dimension(400,700));
    
    /*_translationPanelController = new TranslationPanelController();
    
    jInternalFrame.add(_translationPanelController.getPanel());*/

    //UI.jDesktopPane.add(jInternalFrame);
    jInternalFrame.pack();
    jInternalFrame.setVisible(true);
    jInternalFrame.toFront();
    return 1;
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

  public static int AddTranslation(Lua lua,
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
    return 1;
  }
  
  public static int TranslationPopulate(Lua lua, LuaValue luaTranslation) {
    /*String[] translations = new String[] {"Ani", "Sam", "Joe"};
    String _english = translations[0];

    int i = 0;
    LuaValue _luaTranslation = luaTranslation.checkTable();
    for (String translation: translations) {
      _luaTranslation.set(_english, valueOf((i++ == 0)?"":translation));
    }*/

    return 1;
  }
}
