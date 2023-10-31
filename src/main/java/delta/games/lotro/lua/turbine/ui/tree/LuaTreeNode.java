package delta.games.lotro.lua.turbine.ui.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.turbine.ui.LuaControl;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.lua51.Lua51Consts;

/**
 * LuaTreeNode library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaTreeNode {

  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = LuaObject.callInherit(lua, -3, "Turbine", "UI", "Control");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", LuaTreeNode::constructor);
  	LuaTools.setFunction(lua, -1, -3, "GetParentNode", LuaTreeNode::getParentNode);
    LuaTools.setFunction(lua, -1, -3, "GetChildNodes", LuaTreeNode::getChildNodes);
    LuaTools.setFunction(lua, -1, -3, "IsSelected", LuaTreeNode::isSelected);
    LuaTools.setFunction(lua, -1, -3, "IsExpanded", LuaTreeNode::isExpanded);
    LuaTools.setFunction(lua, -1, -3, "SetExpanded", LuaTreeNode::setExpanded);
    LuaTools.setFunction(lua, -1, -3, "Expand", LuaTreeNode::expand);
    LuaTools.setFunction(lua, -1, -3, "ExpandAll", LuaTreeNode::expandAll);
    LuaTools.setFunction(lua, -1, -3, "Collapse", LuaTreeNode::collapse);
    LuaTools.setFunction(lua, -1, -3, "CollapseAll", LuaTreeNode::collapseAll);

    lua.setField(-2, "TreeNode");
    return error;
  }

  private static int constructor(Lua lua) {
    AbstractPanelController panelController = new AbstractPanelController();
    panelController.setPanel(GuiFactory.buildPanel(null));
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(panelController);
    LuaControl.controlInheritedConstructor(lua, 1, treeNode);
    return 1;
  }

  private static int getParentNode(Lua lua) {
    LuaObject.findLuaObjectFromObject(LuaObject.objectSelf(lua, 1, DefaultMutableTreeNode.class).getParent()).push();
    return 1;
  }
  
  private static int getChildNodes(Lua lua) {
    LuaTreeNodeList.newLuaTreeNodeList(
        lua,
        Lua51Consts.LUA_ENVIRONINDEX,
        null,
        LuaObject.objectSelf(lua, 1, DefaultMutableTreeNode.class)
    );
    return 1;
  }
  
  private static int isSelected(Lua lua) {
    return 1;
  }
  
  private static int isExpanded(Lua lua) {
    return 1;
  }
  
  private static int setExpanded(Lua lua) {
    return 1;
  }
  
  private static int expand(Lua lua) {
    return 1;
  }
  
  private static int expandAll(Lua lua) {
    return 1;
  }
  
  private static int collapse(Lua lua) {
    return 1;
  }
  
  private static int collapseAll(Lua lua) {
    return 1;
  }
}
