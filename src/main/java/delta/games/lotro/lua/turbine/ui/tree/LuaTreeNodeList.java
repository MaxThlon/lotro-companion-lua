package delta.games.lotro.lua.turbine.ui.tree;

import static org.squiddev.cobalt.ValueFactory.userdataOf;
import static org.squiddev.cobalt.ValueFactory.valueOf;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaNumber;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.OperationHelper;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.utils.LuaTools;

/**
 * LuaTreeNodeList library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaTreeNodeList {

  public static void add(LuaState state,
                         LuaTable uiEnv) throws LuaError, UnwindThrowable {

    LuaTable luaTreeNodeListClass = Turbine._luaClass.call(state, Turbine._luaObjectClass).checkTable();
    RegisteredFunction.bind(luaTreeNodeListClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaTreeNodeList::constructor),
        
        RegisteredFunction.of("GetCount", LuaTreeNodeList::getCount),        
        RegisteredFunction.of("Add", LuaTreeNodeList::addNode),
        RegisteredFunction.of("Get", LuaTreeNodeList::getNode),
        RegisteredFunction.of("Contains", LuaTreeNodeList::contains),
        RegisteredFunction.of("IndexOf", LuaTreeNodeList::indexOf),

        RegisteredFunction.of("Remove", LuaTreeNodeList::remove),
        RegisteredFunction.of("RemoveAt", LuaTreeNodeList::removeAt),
        RegisteredFunction.of("Clear", LuaTreeNodeList::clear),
    });
    
    uiEnv.rawset("TreeNodeList", luaTreeNodeListClass);
  }

  public static LuaTable newLuaTreeNodeList(LuaState state, JTree jtree, DefaultMutableTreeNode rootNode) throws LuaError, UnwindThrowable {
    LuaTable globals = state.getMainThread().getfenv();
    LuaTable luaTreeNodeListClass = globals.rawget("Turbine").checkTable().rawget("UI").checkTable().rawget("TreeNodeList").checkTable();
    
    LuaTable luaTreeNodeList = OperationHelper.call(
        state,
        luaTreeNodeListClass,
        (jtree!=null)?userdataOf(jtree):Constants.NIL,
        userdataOf(rootNode)
    ).checkTable();
    return luaTreeNodeList;
  }

  public static JTree jTreeSelf(LuaState state, LuaValue self) throws LuaError {
    return (JTree)LuaTools.objectSelf(state, self, ImmutablePair.class).left;
  }

  public static DefaultMutableTreeNode rootNodeSelf(LuaState state, LuaValue self) throws LuaError {
    return (DefaultMutableTreeNode)LuaTools.objectSelf(state, self, ImmutablePair.class).right;
  }
  
  public static LuaValue constructor(LuaState state, LuaValue self, LuaValue jTtree, LuaValue treeNode) throws LuaError {
    Turbine.ObjectInheritedConstructor(
        state,
        self,
        new ImmutablePair<JTree, DefaultMutableTreeNode>(
            LuaTools.optUserdata(jTtree, JTree.class, null),
            LuaTools.optUserdata(treeNode, DefaultMutableTreeNode.class, null)
        ),
        null,
        null
    );
    return Constants.NIL;
  }
  
  public static LuaNumber getCount(LuaState state, LuaValue self) throws LuaError {
    DefaultMutableTreeNode rootNode = rootNodeSelf(state, self);

    return valueOf(rootNode.getChildCount());
  }
  
  public static LuaValue addNode(LuaState state, LuaValue self, LuaValue luaNode) throws LuaError {
    JTree jTree = jTreeSelf(state, self);
    DefaultMutableTreeNode rootNode = rootNodeSelf(state, self);
    DefaultMutableTreeNode node = LuaTools.objectSelf(state, luaNode, DefaultMutableTreeNode.class);

    if (jTree != null) {
      DefaultTreeModel treeModel = (DefaultTreeModel)jTree.getModel();
      treeModel.insertNodeInto(
          node,
          rootNode,
          rootNode.getChildCount()
      );
      
      jTree.expandPath(new TreePath(rootNode.getPath()));
    } else {
      rootNode.insert(
          node,
          rootNode.getChildCount()
      );
    }
    return Constants.NIL;
  }
  
  @SuppressWarnings("cast")
  public static LuaValue getNode(LuaState state, LuaValue self, LuaValue index) throws LuaError {
    return LuaTools.findLuaObjectFromObject((DefaultMutableTreeNode)rootNodeSelf(state, self).getChildAt(index.checkInteger()));
  }
  
  public static LuaValue contains(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue indexOf(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue remove(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue removeAt(LuaState state, LuaValue self, LuaValue index) {
    return Constants.NIL;
  }
  
  public static LuaValue clear(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
}
