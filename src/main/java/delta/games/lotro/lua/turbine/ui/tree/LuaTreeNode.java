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
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.turbine.ui.LuaControl;

/**
 * LuaTreeNode library for lua scripts.
 * @author DAM
 */
public abstract class LuaTreeNode {

  public static void add(LuaState state,
                         LuaTable uiMetatable,
                         LuaFunction luaClass,
                         LuaValue luaControlClass) throws LuaError, UnwindThrowable {

    LuaTable luaTreeNodeClass = luaClass.call(state, luaControlClass).checkTable();
    RegisteredFunction.bind(luaTreeNodeClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaTreeNode::Constructor),
        
        RegisteredFunction.of("GetParentNode", LuaTreeNode::GetParentNode),
        RegisteredFunction.of("GetChildNodes", LuaTreeNode::GetChildNodes),
        RegisteredFunction.of("IsSelected", LuaTreeNode::IsSelected),
        
        RegisteredFunction.of("IsExpanded", LuaTreeNode::IsExpanded),
        RegisteredFunction.of("SetExpanded", LuaTreeNode::SetExpanded),
        RegisteredFunction.of("Expand", LuaTreeNode::Expand),
        RegisteredFunction.of("ExpandAll", LuaTreeNode::ExpandAll),
        RegisteredFunction.of("Collapse", LuaTreeNode::Collapse),
        RegisteredFunction.of("CollapseAll", LuaTreeNode::CollapseAll)   
    });
    
    uiMetatable.rawset("TreeNode", luaTreeNodeClass);
  }
  
  public static void setObjectSelf(LuaState state, LuaValue self, DefaultTreeModel treeModel, DefaultMutableTreeNode treeNode) throws LuaError, UnwindThrowable {
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

  public static LuaValue Constructor(LuaState state, LuaValue self) throws LuaError, UnwindThrowable {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(new JPanel());
    LuaControl.ControlInheritedConstructor(state, self, treeNode);
    
    return Constants.NIL;
  }
  
  public static LuaValue GetParentNode(LuaState state, LuaValue self) throws LuaError, UnwindThrowable {
    return Constants.NIL;
  }
  
  public static LuaValue GetChildNodes(LuaState state, LuaValue self) throws LuaError, UnwindThrowable {
    return LuaTreeNodeList.newLuaTreeNodeList(
        state,
        null,
        Turbine.objectSelf(state, self, DefaultMutableTreeNode.class)
    );
  }
  
  public static LuaValue IsSelected(LuaState state, LuaValue self) throws LuaError, UnwindThrowable {
    return Constants.NIL;
  }
  
  public static LuaValue IsExpanded(LuaState state, LuaValue self) throws LuaError, UnwindThrowable {
    return Constants.NIL;
  }
  
  public static LuaValue SetExpanded(LuaState state, LuaValue self, LuaValue value) throws LuaError, UnwindThrowable {
    return Constants.NIL;
  }
  
  public static LuaValue Expand(LuaState state, LuaValue self) throws LuaError, UnwindThrowable {
    return Constants.NIL;
  }
  
  public static LuaValue ExpandAll(LuaState state, LuaValue self) throws LuaError, UnwindThrowable {
    return Constants.NIL;
  }
  
  public static LuaValue Collapse(LuaState state, LuaValue self) throws LuaError, UnwindThrowable {
    return Constants.NIL;
  }
  
  public static LuaValue CollapseAll(LuaState state, LuaValue self) throws LuaError, UnwindThrowable {
    return Constants.NIL;
  }
}
