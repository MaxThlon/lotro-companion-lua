package delta.games.lotro.lua.turbine.ui.tree;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;

/**
 * LuaTreeCellEditor library for lua scripts.
 * @author MaxThlon
 */
public class LuaTreeCellEditor extends AbstractCellEditor implements TreeCellEditor {

  private JPanel jPanel=null;

  /**
   * Constructor.
   */
  public LuaTreeCellEditor() {
  }

  @Override
  public Object getCellEditorValue() {
    return jPanel;
  }

  @Override
  public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected,
                                              boolean expanded, boolean leaf, int row) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
    Component component = null;

    if (node.getUserObject() instanceof JPanel) {
      component = (JPanel)node.getUserObject();
    }

    return component;
  }

}