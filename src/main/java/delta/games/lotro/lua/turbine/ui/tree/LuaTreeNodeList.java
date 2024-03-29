package delta.games.lotro.lua.turbine.ui.tree;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.lang3.tuple.ImmutablePair;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * LuaTreeNodeList library for lua scripts.
 * @author MaxThlon
 */
public final class LuaTreeNodeList {
  /**
   * Initialize lua TreeNodeList package
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
  	LuaTools.pushClass(lua, "Turbine", "Object");
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", LuaTreeNodeList::constructor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetCount", LuaTreeNodeList::getCount);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Add", LuaTreeNodeList::addNode);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Get", LuaTreeNodeList::getNode);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Contains", LuaTreeNodeList::contains);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IndexOf", LuaTreeNodeList::indexOf);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Remove", LuaTreeNodeList::remove);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "RemoveAt", LuaTreeNodeList::removeAt);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Clear", LuaTreeNodeList::clear);

    lua.setField(-2, "TreeNodeList");
  }

  /**
   * @param lua .
   * @param envIndex .
   * @param jtree .
   * @param rootNode .
   * @return int.
   */
  public static int newLuaTreeNodeList(Lua lua, int envIndex, JTree jtree, DefaultMutableTreeNode rootNode) {
  	LuaTools.newClassInstance(lua, envIndex, (relativeEnvIndex) -> {
  		LuaTools.pushValue(lua, relativeEnvIndex.intValue(), "Turbine", "UI", "TreeNodeList");
  		//lua.push(jtree, Conversion.NONE);
  		//lua.push(rootNode, Conversion.NONE);
  	});
    return 1;
  }

  /**
   * @param lua .
   * @param index .
   * @return a JTree.
   */
  public static JTree jTreeSelf(Lua lua, int index) {
    return (JTree)LuaObject.objectSelf(lua, index, ImmutablePair.class).left;
  }

  /**
   * @param lua .
   * @param index .
   * @return a DefaultMutableTreeNode.
   */
  public static DefaultMutableTreeNode rootNodeSelf(Lua lua, int index) {
    return (DefaultMutableTreeNode)LuaObject.objectSelf(lua, index, ImmutablePair.class).right;
  }
  
  private static int constructor(Lua lua) {
    LuaObject.ObjectInheritedConstructor(
        lua,
        1,
        new ImmutablePair<JTree, DefaultMutableTreeNode>(
            (JTree)lua.toJavaObject(2),
            (DefaultMutableTreeNode)lua.toJavaObject(3)
        ),
        null,
        null
    );
    return 1;
  }
  
  private static int getCount(Lua lua) {
    DefaultMutableTreeNode rootNode = rootNodeSelf(lua, 1);
    lua.push(rootNode.getChildCount());
    return 1;
  }
  
  private static int addNode(Lua lua) {
    JTree jTree = jTreeSelf(lua, 1);
    DefaultMutableTreeNode rootNode = rootNodeSelf(lua, 1);
    DefaultMutableTreeNode node = LuaObject.objectSelf(lua, 2, DefaultMutableTreeNode.class);

    if (jTree != null) {
      DefaultTreeModel treeModel = (DefaultTreeModel)jTree.getModel();
      SwingUtilities.invokeLater(() -> {
      	treeModel.insertNodeInto(
          node,
          rootNode,
          rootNode.getChildCount()
        );
        
        jTree.expandPath(new TreePath(rootNode.getPath()));
      });
    } else {
    	SwingUtilities.invokeLater(() ->
        rootNode.insert(
            node,
            rootNode.getChildCount()
        )
      );
    }
    return 0;
  }
  
  @SuppressWarnings("cast")
  private static int getNode(Lua lua) {
    LuaObject.findLuaObjectFromObject(
    		(DefaultMutableTreeNode)rootNodeSelf(lua, 1).getChildAt((int)lua.toNumber(2))
    );
    return 1;
  }
  
  private static int contains(Lua lua) {
  	lua.push(false);
    return 1;
  }
  
  private static int indexOf(Lua lua) {
  	lua.push(1);
    return 1;
  }
  
  private static int remove(Lua lua) {
    return 0;
  }
  
  private static int removeAt(Lua lua) {
    return 0;
  }
  
  private static int clear(Lua lua) {
    return 0;
  }
}
