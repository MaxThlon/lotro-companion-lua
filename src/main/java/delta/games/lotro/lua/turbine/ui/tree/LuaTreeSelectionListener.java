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

/**
 * LuaTreeSelectionListener library for lua scripts.
 * @author MaxThlon
 */
public class LuaTreeSelectionListener implements TreeSelectionListener  {
  private JTree _jTree;
  private LuaState _state;
  private LuaTable _luaTreeview;
  public LuaValue _luaSelectedNodeChanged;

  public LuaTreeSelectionListener(JTree jTree, LuaState state, LuaTable luaTreeview, LuaValue luaSelectedNodeChanged) {
    this._jTree = jTree;
    this._state = state;
    this._luaTreeview = luaTreeview;
    this._luaSelectedNodeChanged = luaSelectedNodeChanged;
  }

  @Override
  public void valueChanged(TreeSelectionEvent event) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)_jTree.getLastSelectedPathComponent();

    try {
      if (this._luaSelectedNodeChanged != Constants.NIL)
          OperationHelper.call(
              _state,
              this._luaSelectedNodeChanged,
              _luaTreeview, 
              (node == null)?Constants.NIL : LuaControl.findluaObjectFromObject(node),
              Constants.NIL
          );
    } catch (LuaError | UnwindThrowable error) {
      error.printStackTrace();
    }
  }
}
