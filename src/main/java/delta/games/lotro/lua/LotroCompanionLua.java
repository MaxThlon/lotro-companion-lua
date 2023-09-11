package delta.games.lotro.lua;

import static org.squiddev.cobalt.ValueFactory.valueOf;

import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.function.LibFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lua.turbine.ui.UI;
import delta.games.lotro.lua.turbine.ui.tree.TableTreeViewCellRenderer;

/**
 * LotroCompanionLua library for lua scripts.
 * @author DAM
 */
public abstract class LotroCompanionLua {
  private static int columnCount = 0;
  private static JInternalFrame jInternalFrame = null;
  private static JTree jTree = null;
  
  public LotroCompanionLua() {}
  
  public static void add(LuaState state, LuaTable env) throws LuaError {
    LibFunction.setGlobalLibrary(state, env, "LotroCompanion", RegisteredFunction.bind(new RegisteredFunction[]{
        RegisteredFunction.of("Activate", LotroCompanionLua::Activate),
        RegisteredFunction.of("AddTranslation", LotroCompanionLua::AddTranslation),
        RegisteredFunction.of("TranslationPopulate", LotroCompanionLua::TranslationPopulate)
    }));
  }

  public static LuaValue Activate(LuaState state, LuaValue value) throws LuaError {
    LuaTable languages = value.checkTable();
    columnCount = languages.count();
    jInternalFrame = new JInternalFrame("Translation");
    jInternalFrame.setOpaque(false);
    jInternalFrame.setPreferredSize(new Dimension(400,700));
    
    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root");
    jTree = new JTree (new DefaultTreeModel(rootNode));
    jTree.setCellRenderer(new TableTreeViewCellRenderer(columnCount));
    //tree.setCellEditor(new LuaTreeCellEditor());
    jTree.setEditable(true);
    jTree.setRootVisible(false);
    jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    jTree.setShowsRootHandles(true);
    JScrollPane jScrollPane = GuiFactory.buildScrollPane(jTree);
    jInternalFrame.add(jScrollPane);

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
                                        LuaValue translateValue) throws LuaError {
    DefaultTreeModel treeModel = (DefaultTreeModel)jTree.getModel();
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
    jTree.expandPath(new TreePath(rootNode.getPath()));
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
