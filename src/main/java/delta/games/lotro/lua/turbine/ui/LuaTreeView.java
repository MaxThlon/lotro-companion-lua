package delta.games.lotro.lua.turbine.ui;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
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
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeNodeList;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeViewTreeCellRenderer;

/**
 * LuaTreeView library for lua scripts.
 * @author DAM
 */
public abstract class LuaTreeView {

  public static void add(LuaState state,
                         LuaTable uiMetatable,
                         LuaFunction luaClass,
                         LuaValue luaScrollableControlClass) throws LuaError, UnwindThrowable {

    LuaTable luaTreeViewClass = OperationHelper.call(state, luaClass, luaScrollableControlClass).checkTable();
    RegisteredFunction.bind(luaTreeViewClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaTreeView::Constructor),
        
        RegisteredFunction.of("GetIndentationWidth", LuaTreeView::GetIndentationWidth),
        RegisteredFunction.of("SetIndentationWidth", LuaTreeView::SetIndentationWidth),
        
        RegisteredFunction.of("ExpandAll", LuaTreeView::ExpandAll),
        RegisteredFunction.of("CollapseAll", LuaTreeView::CollapseAll),
        
        RegisteredFunction.of("GetNodes", LuaTreeView::GetNodes),
        RegisteredFunction.of("GetSelectedNode", LuaTreeView::GetSelectedNode),
        RegisteredFunction.of("SetSelectedNode", LuaTreeView::SetSelectedNode),
        RegisteredFunction.of("GetItemAt", LuaTreeView::GetItemAt),
        
        RegisteredFunction.of("GetFilter", LuaTreeView::GetFilter),
        RegisteredFunction.of("SetFilter", LuaTreeView::SetFilter),
        RegisteredFunction.of("GetSortMethod", LuaTreeView::GetSortMethod),
        RegisteredFunction.of("SetSortMethod", LuaTreeView::SetSortMethod),
        
        RegisteredFunction.of("Refresh", LuaTreeView::Refresh)
    });
    
    uiMetatable.rawset("TreeView", luaTreeViewClass);
  }

  public static JTree jTreeSelf(LuaState state, LuaValue self) throws LuaError {
    return (JTree)Turbine.objectSelf(state, self, JScrollPane.class).getViewport().getView();
  }

  public static LuaValue Constructor(LuaState state, LuaValue self) throws LuaError {
    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root");
    JTree tree = new JTree (new DefaultTreeModel(rootNode));
    tree.setCellRenderer(new LuaTreeViewTreeCellRenderer());
    tree.setCellEditor(new LuaTreeCellEditor());
    tree.setEditable(true);
    tree.setRootVisible(false);
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.setShowsRootHandles(true);

    JScrollPane jScrollPane = GuiFactory.buildScrollPane(tree);
    LuaScrollableControl.scrollableControlInheritedConstructor(state, self, jScrollPane);

    return Constants.NIL;
  }
  
  public static LuaNumber GetIndentationWidth(LuaState state, LuaValue self) throws LuaError {
    return Constants.ZERO;
  }
  
  public static LuaValue SetIndentationWidth(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    return Constants.NIL;
  }
  
  public static LuaValue ExpandAll(LuaState state, LuaValue self) throws LuaError {
    return Constants.NIL;
  }
  
  public static LuaValue CollapseAll(LuaState state, LuaValue self) throws LuaError {
    return Constants.NIL;
  }
  
  public static LuaValue GetNodes(LuaState state, LuaValue self) throws LuaError, UnwindThrowable {
    JTree jTree = LuaTreeView.jTreeSelf(state, self);
    return LuaTreeNodeList.newLuaTreeNodeList(
        state,
        jTree,
        (DefaultMutableTreeNode)jTree.getModel().getRoot()
    );
  }
  
  public static LuaValue GetSelectedNode(LuaState state, LuaValue self) throws LuaError { 
    return LuaControl.findluaObjectFromObject(LuaTreeView.jTreeSelf(state, self).getLastSelectedPathComponent());
  }
  
  public static LuaValue SetSelectedNode(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    return Constants.NIL;
  }
  
  public static LuaValue GetItemAt(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    return Constants.NIL;
  }
  
  public static LuaValue GetFilter(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    return Constants.NIL;
  }
  
  public static LuaValue SetFilter(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    return Constants.NIL;
  }
  
  public static LuaValue GetSortMethod(LuaState state, LuaValue self) throws LuaError {
    return Constants.NIL;
  }
  
  public static LuaValue SetSortMethod(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    return Constants.NIL;
  }
  
  public static LuaValue Refresh(LuaState state, LuaValue self) throws LuaError {
    return Constants.NIL;
  }  
}
