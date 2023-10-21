package delta.games.lotro.lua.turbine.ui;

import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaNumber;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.OperationHelper;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeCellEditor;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeMouseListener;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeNodeList;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeViewTreeCellRenderer;
import delta.games.lotro.lua.utils.LuaTools;

/**
 * LuaTreeView library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaTreeView {

  public static void add(LuaState state,
                         LuaTable uiEnv,
                         LuaValue luaScrollableControlClass) throws LuaError, UnwindThrowable {

    LuaTable luaTreeViewClass = OperationHelper.call(state, Turbine._luaClass, luaScrollableControlClass).checkTable();
    RegisteredFunction.bind(luaTreeViewClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaTreeView::constructor),
        
        RegisteredFunction.of("GetIndentationWidth", LuaTreeView::getIndentationWidth),
        RegisteredFunction.of("SetIndentationWidth", LuaTreeView::setIndentationWidth),
        
        RegisteredFunction.of("ExpandAll", LuaTreeView::expandAll),
        RegisteredFunction.of("CollapseAll", LuaTreeView::collapseAll),
        
        RegisteredFunction.of("GetNodes", LuaTreeView::getNodes),
        RegisteredFunction.of("GetSelectedNode", LuaTreeView::getSelectedNode),
        RegisteredFunction.of("SetSelectedNode", LuaTreeView::setSelectedNode),
        RegisteredFunction.of("GetItemAt", LuaTreeView::getItemAt),
        
        RegisteredFunction.of("GetFilter", LuaTreeView::getFilter),
        RegisteredFunction.of("SetFilter", LuaTreeView::setFilter),
        RegisteredFunction.of("GetSortMethod", LuaTreeView::getSortMethod),
        RegisteredFunction.of("SetSortMethod", LuaTreeView::setSortMethod),
        
        RegisteredFunction.of("Refresh", LuaTreeView::refresh)
    });
    
    uiEnv.rawset("TreeView", luaTreeViewClass);
  }

  public static JTree jTreeSelf(LuaState state, LuaValue self) throws LuaError {
    return (JTree)LuaTools.objectSelf(state, self, JScrollPane.class).getViewport().getView();
  }

  public static LuaValue constructor(LuaState state, LuaValue self) throws LuaError {
    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root");
    JTree tree = new JTree (new DefaultTreeModel(rootNode));
    tree.setCellRenderer(new LuaTreeViewTreeCellRenderer());
    tree.setCellEditor(new LuaTreeCellEditor());
    tree.addMouseListener(new LuaTreeMouseListener(tree, state));
    tree.setEditable(true);
    tree.setRootVisible(false);
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.setShowsRootHandles(true);

    JScrollPane jScrollPane = GuiFactory.buildScrollPane(tree);
    LuaScrollableControl.scrollableControlInheritedConstructor(state, self, jScrollPane);

    return Constants.NIL;
  }
  
  public static LuaNumber getIndentationWidth(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaValue setIndentationWidth(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue expandAll(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue collapseAll(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getNodes(LuaState state, LuaValue self) throws LuaError, UnwindThrowable {
    JTree jTree = LuaTreeView.jTreeSelf(state, self);
    return LuaTreeNodeList.newLuaTreeNodeList(
        state,
        jTree,
        (DefaultMutableTreeNode)jTree.getModel().getRoot()
    );
  }
  
  public static LuaValue getSelectedNode(LuaState state, LuaValue self) throws LuaError { 
    return LuaTools.findLuaObjectFromObject(LuaTreeView.jTreeSelf(state, self).getLastSelectedPathComponent());
  }
  
  public static LuaValue setSelectedNode(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue getItemAt(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue getFilter(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue setFilter(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue getSortMethod(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue setSortMethod(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue refresh(LuaState state, LuaValue self) {
    return Constants.NIL;
  }  
}
