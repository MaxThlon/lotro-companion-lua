package delta.games.lotro.lua.turbine.ui.tree;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.ws.Holder;

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
public final class LuaTreeNode {
  /**
   * Initialize lua TreeNode package
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
  	LuaTools.pushClass(lua, "Turbine", "UI", "Control");
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", LuaTreeNode::constructor);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetParentNode", LuaTreeNode::getParentNode);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetChildNodes", LuaTreeNode::getChildNodes);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsSelected", LuaTreeNode::isSelected);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsExpanded", LuaTreeNode::isExpanded);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetExpanded", LuaTreeNode::setExpanded);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Expand", LuaTreeNode::expand);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "ExpandAll", LuaTreeNode::expandAll);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Collapse", LuaTreeNode::collapse);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "CollapseAll", LuaTreeNode::collapseAll);

    lua.setField(-2, "TreeNode");
  }

  private static int constructor(Lua lua) {
    AbstractPanelController panelController = new AbstractPanelController();
    Holder<JPanel> jPanel = new Holder<JPanel>();
    LuaTools.invokeAndWait(lua, () -> jPanel.value = GuiFactory.buildPanel(null));
    panelController.setPanel(jPanel.value);
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
