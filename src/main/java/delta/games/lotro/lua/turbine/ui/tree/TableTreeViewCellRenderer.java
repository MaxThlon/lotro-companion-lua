package delta.games.lotro.lua.turbine.ui.tree;

import java.awt.Component;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaValue;

@SuppressWarnings("serial")
public class TableTreeViewCellRenderer extends DefaultTreeCellRenderer {

  private int columnCount;
  private JTable table;

  public TableTreeViewCellRenderer(int columnCount) {
    this.columnCount = columnCount;
    this.table = new JTable();
    JScrollPane scrollPane = new JScrollPane(this.table);
    add(scrollPane);
  }

  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
                                                boolean expanded, boolean leaf, int row, boolean hasFocus) {
    Component component = this;
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
    if (node.getUserObject() instanceof List list) {
      //final String v = (String) ((DefaultMutableTreeNode) value).getUserObject();
      component = table;
      table.setModel(new DefaultTableModel() {
        @Override
        public int getRowCount() {
          return list.size();
        }
        @Override
        public int getColumnCount() {
          return columnCount;
        }
        @Override
        public Object getValueAt(int row, int column) {
          String value = "";
          try {
            if (list.get(row) instanceof LuaValue[] translations)
              value = translations[column].optString("");
          } catch (LuaError e) {
            e.printStackTrace();
          }
          
          return value;
        }
      });
      table.setPreferredScrollableViewportSize(table.getPreferredSize());
    } else {
      JLabel l = (JLabel)super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
      l.setFont(tree.getFont().deriveFont(leaf ? 16f : 48f));
      //height = tree.getRowHeight();
      //height = leaf ? 20 : 60;
    }
    return component;
  }
}
