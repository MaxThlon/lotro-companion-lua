package delta.games.lotro.lua.turbine.ui.tree;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

@SuppressWarnings("serial")
public class LuaTreeViewTreeCellRenderer extends DefaultTreeCellRenderer /* implements TreeCellRenderer */ {
  private int height;
  
  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
                                                boolean expanded, boolean leaf, int row, boolean hasFocus) {
    Component component = this;
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
    
    if (node.getUserObject() instanceof JPanel jPanel) {
      component = jPanel;
      height = component.getHeight();
    } else {
      JLabel l = (JLabel)super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
      l.setFont(tree.getFont().deriveFont(leaf ? 16f : 48f));
      //height = tree.getRowHeight();
      height = leaf ? 20 : 60;
    }
    return component;
  }
  
  @Override
  public Dimension getPreferredSize() {
    Dimension d = super.getPreferredSize();
    d.height = height;
    return d;
  }
}
