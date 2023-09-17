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
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.turbine.ui.LuaControl;
import delta.games.lotro.lua.utils.LuaTools;

/**
 * LuaTreeNodeList library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaTreeNodeList {

  public static void add(LuaState state,
                         LuaTable uiMetatable,
                         LuaFunction luaClass,
                         LuaValue luaObjectClass) throws LuaError, UnwindThrowable {

    LuaTable luaTreeNodeListClass = luaClass.call(state, luaObjectClass).checkTable();
    RegisteredFunction.bind(luaTreeNodeListClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaTreeNodeList::Constructor),
        
        RegisteredFunction.of("GetCount", LuaTreeNodeList::GetCount),        
        RegisteredFunction.of("Add", LuaTreeNodeList::Add),
        RegisteredFunction.of("Get", LuaTreeNodeList::Get),
        RegisteredFunction.of("Contains", LuaTreeNodeList::Contains),
        RegisteredFunction.of("IndexOf", LuaTreeNodeList::IndexOf),

        RegisteredFunction.of("Remove", LuaTreeNodeList::Remove),
        RegisteredFunction.of("RemoveAt", LuaTreeNodeList::RemoveAt),
        RegisteredFunction.of("Clear", LuaTreeNodeList::Clear),
    });
    
    uiMetatable.rawset("TreeNodeList", luaTreeNodeListClass);
  }

  public static LuaTable newLuaTreeNodeList(LuaState state, JTree jtree, DefaultMutableTreeNode rootNode) throws LuaError, UnwindThrowable {
    LuaTable globals = state.getCurrentThread().getfenv();
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
    return (JTree)Turbine.objectSelf(state, self, ImmutablePair.class).left;
  }

  public static DefaultMutableTreeNode rootNodeSelf(LuaState state, LuaValue self) throws LuaError {
    return (DefaultMutableTreeNode)Turbine.objectSelf(state, self, ImmutablePair.class).right;
  }
  
  public static LuaValue Constructor(LuaState state, LuaValue self, LuaValue jTtree, LuaValue treeNode) throws LuaError {
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
  
  public static LuaNumber GetCount(LuaState state, LuaValue self) throws LuaError {
    DefaultMutableTreeNode rootNode = rootNodeSelf(state, self);

    return valueOf(rootNode.getChildCount());
  }
  
  public static LuaValue Add(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    JTree jTree = jTreeSelf(state, self);
    DefaultMutableTreeNode rootNode = rootNodeSelf(state, self);

    if (jTree != null) {
      DefaultTreeModel treeModel = (DefaultTreeModel)jTree.getModel();
      treeModel.insertNodeInto(
          Turbine.objectSelf(state, value, DefaultMutableTreeNode.class),
          rootNode,
          rootNode.getChildCount()
      );
      jTree.expandPath(new TreePath(rootNode.getPath()));
    } else {
      rootNode.insert(
          Turbine.objectSelf(state, value, DefaultMutableTreeNode.class),
          rootNode.getChildCount()
      );
    }
    return Constants.NIL;
  }
  
  @SuppressWarnings("cast")
  public static LuaValue Get(LuaState state, LuaValue self, LuaValue index) throws LuaError {
    return LuaControl.findluaObjectFromObject((DefaultMutableTreeNode)rootNodeSelf(state, self).getChildAt(index.checkInteger()));
  }
  
  public static LuaValue Contains(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue IndexOf(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue Remove(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue RemoveAt(LuaState state, LuaValue self, LuaValue index) {
    return Constants.NIL;
  }
  
  public static LuaValue Clear(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
}
