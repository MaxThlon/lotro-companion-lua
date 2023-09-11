package delta.games.lotro.lua.turbine.ui.tree;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.OperationHelper;
import org.squiddev.cobalt.UnwindThrowable;

import delta.games.lotro.lua.turbine.ui.LuaControl;

public class LuaTreeSelectionListener implements TreeSelectionListener  {
  private JTree jTree;
  private LuaState state;
  private LuaTable luaTreeview;
  public LuaValue luaSelectedNodeChanged;

  public LuaTreeSelectionListener(JTree jTree, LuaState state, LuaTable luaTreeview, LuaValue luaSelectedNodeChanged) {
    this.jTree = jTree;
    this.state = state;
    this.luaTreeview = luaTreeview;
    this.luaSelectedNodeChanged = luaSelectedNodeChanged;
  }

  @Override
  public void valueChanged(TreeSelectionEvent event) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)jTree.getLastSelectedPathComponent();

    try {
      if (this.luaSelectedNodeChanged != Constants.NIL)
          OperationHelper.call(
              state,
              this.luaSelectedNodeChanged,
              luaTreeview, 
              (node == null)?Constants.NIL : LuaControl.findluaObjectFromObject(node),
              Constants.NIL
          );
    } catch (LuaError | UnwindThrowable error) {
      error.printStackTrace();
    }
  }
}
