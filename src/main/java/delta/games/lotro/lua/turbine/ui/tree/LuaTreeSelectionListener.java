package delta.games.lotro.lua.turbine.ui.tree;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import delta.games.lotro.lua.turbine.object.LuaObject;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.value.LuaValue;

/**
 * LuaTreeSelectionListener library for lua scripts.
 * @author MaxThlon
 */
public class LuaTreeSelectionListener implements TreeSelectionListener  {
  private JTree _jTree;
  private LuaValue _luaTreeview;
  public LuaValue _luaSelectedNodeChanged;

  public LuaTreeSelectionListener(JTree jTree, LuaValue luaTreeview, LuaValue luaSelectedNodeChanged) {
    _jTree = jTree;
    _luaTreeview = luaTreeview;
    _luaSelectedNodeChanged = luaSelectedNodeChanged;
  }

  @Override
  public void valueChanged(TreeSelectionEvent event) {
    if (_luaSelectedNodeChanged.type() != Lua.LuaType.NIL) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)_jTree.getLastSelectedPathComponent();
      if (node != null) {
        _luaSelectedNodeChanged.call(
            _luaTreeview,
            LuaObject.findLuaObjectFromObject(node)
        );
      }
    }
  }
}
