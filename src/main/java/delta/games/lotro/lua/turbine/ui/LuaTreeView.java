package delta.games.lotro.lua.turbine.ui;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.ws.Holder;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeCellEditor;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeMouseListener;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeNodeList;
import delta.games.lotro.lua.turbine.ui.tree.LuaTreeViewTreeCellRenderer;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.lua51.Lua51Consts;

/**
 * LuaTreeView library for lua scripts.
 * @author MaxThlon
 */
final class LuaTreeView {

  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = LuaObject.callInherit(lua, -3, "Turbine", "UI", "ScrollableControl");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", LuaTreeView::constructor);
  	LuaTools.setFunction(lua, -1, -3, "GetIndentationWidth", LuaTreeView::getIndentationWidth);
    LuaTools.setFunction(lua, -1, -3, "SetIndentationWidth", LuaTreeView::setIndentationWidth);

    LuaTools.setFunction(lua, -1, -3, "ExpandAll", LuaTreeView::expandAll);
    LuaTools.setFunction(lua, -1, -3, "CollapseAll", LuaTreeView::collapseAll);

    LuaTools.setFunction(lua, -1, -3, "GetNodes", LuaTreeView::getNodes);
    LuaTools.setFunction(lua, -1, -3, "GetSelectedNode", LuaTreeView::getSelectedNode);
    LuaTools.setFunction(lua, -1, -3, "SetSelectedNode", LuaTreeView::setSelectedNode);
    LuaTools.setFunction(lua, -1, -3, "GetItemAt", LuaTreeView::getItemAt);

    LuaTools.setFunction(lua, -1, -3, "GetFilter", LuaTreeView::getFilter);
    LuaTools.setFunction(lua, -1, -3, "SetFilter", LuaTreeView::setFilter);

    LuaTools.setFunction(lua, -1, -3, "GetSortMethod", LuaTreeView::getSortMethod);
    LuaTools.setFunction(lua, -1, -3, "SetSortMethod", LuaTreeView::setSortMethod);

    LuaTools.setFunction(lua, -1, -3, "Refresh", LuaTreeView::refresh);

    lua.setField(-2, "TreeView");
    return error;
  }

  public static JTree jTreeSelf(Lua lua, int index) {
    return (JTree)LuaObject.objectSelf(lua, index, JScrollPane.class).getViewport().getView();
  }

  private static int constructor(Lua lua) {
    Holder<JScrollPane> jScrollPane = new Holder<JScrollPane>();
    
    LuaTools.invokeAndWait(lua, () -> {
    	JTree jTree = GuiFactory.buildTree(null);
    	jTree.setCellRenderer(new LuaTreeViewTreeCellRenderer());
    	jTree.setCellEditor(new LuaTreeCellEditor());
    	jTree.addMouseListener(new LuaTreeMouseListener(jTree));
    	jTree.setEditable(true);
    	jTree.setRootVisible(false);
    	jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    	jTree.setShowsRootHandles(true);
      jScrollPane.value = GuiFactory.buildScrollPane(jTree);
    });
    LuaScrollableControl.scrollableControlInheritedConstructor(lua, 1, jScrollPane.value);

    return 1;
  }
  
  private static int getIndentationWidth(Lua lua) {
    return 1;
  }
  
  private static int setIndentationWidth(Lua lua) {
    return 1;
  }
  
  private static int expandAll(Lua lua) {
    return 1;
  }
  
  private static int collapseAll(Lua lua) {
    return 1;
  }
  
  private static int getNodes(Lua lua) {
    JTree jTree = LuaTreeView.jTreeSelf(lua, 1);
    return LuaTreeNodeList.newLuaTreeNodeList(
        lua,
        Lua51Consts.LUA_ENVIRONINDEX,
        jTree,
        (DefaultMutableTreeNode)jTree.getModel().getRoot()
    );
  }
  
  private static int getSelectedNode(Lua lua) {
  	LuaTools.invokeAndWait(lua, () ->
    	LuaObject.findLuaObjectFromObject(LuaTreeView.jTreeSelf(lua, 1).getLastSelectedPathComponent())
    );
    return 1;
  }
  
  private static int setSelectedNode(Lua lua) {
    return 1;
  }
  
  private static int getItemAt(Lua lua) {
    return 1;
  }
  
  private static int getFilter(Lua lua) {
    return 1;
  }
  
  private static int setFilter(Lua lua) {
    return 1;
  }
  
  private static int getSortMethod(Lua lua) {
    return 1;
  }
  
  private static int setSortMethod(Lua lua) {
    return 1;
  }
  
  private static int refresh(Lua lua) {
    return 1;
  }  
}
