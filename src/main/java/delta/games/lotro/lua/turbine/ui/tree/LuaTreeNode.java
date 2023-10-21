package delta.games.lotro.lua.turbine.ui.tree;

import static org.squiddev.cobalt.ValueFactory.tableOf;
import static org.squiddev.cobalt.ValueFactory.userdataOf;
import static org.squiddev.cobalt.ValueFactory.valueOf;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.turbine.ui.LuaControl;
import delta.games.lotro.lua.utils.LuaTools;

/**
 * LuaTreeNode library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaTreeNode {

  public static void add(LuaState state,
                         LuaTable uiEnv,
                         LuaValue luaControlClass) throws LuaError, UnwindThrowable {

    LuaTable luaTreeNodeClass = Turbine._luaClass.call(state, luaControlClass).checkTable();
    RegisteredFunction.bind(luaTreeNodeClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaTreeNode::constructor),
        
        RegisteredFunction.of("GetParentNode", LuaTreeNode::getParentNode),
        RegisteredFunction.of("GetChildNodes", LuaTreeNode::getChildNodes),
        RegisteredFunction.of("IsSelected", LuaTreeNode::isSelected),
        
        RegisteredFunction.of("IsExpanded", LuaTreeNode::isExpanded),
        RegisteredFunction.of("SetExpanded", LuaTreeNode::setExpanded),
        RegisteredFunction.of("Expand", LuaTreeNode::expand),
        RegisteredFunction.of("ExpandAll", LuaTreeNode::expandAll),
        RegisteredFunction.of("Collapse", LuaTreeNode::collapse),
        RegisteredFunction.of("CollapseAll", LuaTreeNode::CollapseAll)   
    });
    
    uiEnv.rawset("TreeNode", luaTreeNodeClass);
  }
  
  public static void setObjectSelf(LuaState state, LuaValue self, DefaultTreeModel treeModel, DefaultMutableTreeNode treeNode) throws LuaError {
    LuaTable __objectSelf = self.metatag(state, valueOf("__objectSelf")).optTable(null);
    if (__objectSelf == null) {
      __objectSelf = tableOf();
      self.getMetatable(state).rawset("__objectSelf", __objectSelf);
    }

    __objectSelf.rawset(self, 
        tableOf(
            valueOf("treeModel"), userdataOf(treeModel),
            valueOf("treeNode"), userdataOf(treeNode)
        )
    );
  }

  public static LuaValue constructor(LuaState state, LuaValue self) throws LuaError {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(new JPanel());
    LuaControl.controlInheritedConstructor(state, self, treeNode);
    
    return Constants.NIL;
  }

  public static LuaValue getParentNode(LuaState state, LuaValue self) throws LuaError {
    return LuaTools.findLuaObjectFromObject(LuaTools.objectSelf(state, self, DefaultMutableTreeNode.class).getParent()) ;
  }
  
  public static LuaValue getChildNodes(LuaState state, LuaValue self) throws LuaError, UnwindThrowable {
    return LuaTreeNodeList.newLuaTreeNodeList(
        state,
        null,
        LuaTools.objectSelf(state, self, DefaultMutableTreeNode.class)
    );
  }
  
  public static LuaValue isSelected(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue isExpanded(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue setExpanded(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue expand(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue expandAll(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue collapse(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue CollapseAll(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
}
