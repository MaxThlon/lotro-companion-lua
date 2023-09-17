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

/**
 * TableTreeViewCellRenderer library for lua scripts.
 * @author MaxThlon
 */
public class TableTreeViewCellRenderer extends DefaultTreeCellRenderer {

  private int _columnCount;
  private JTable table;

  public TableTreeViewCellRenderer(int columnCount) {
    this._columnCount = columnCount;
    this.table = new JTable();
    JScrollPane scrollPane = new JScrollPane(this.table);
    add(scrollPane);
  }

  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean xselected,
                                                boolean expanded, boolean leaf, int row, boolean xhasFocus) {
    Component component = this;
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
    if (node.getUserObject() instanceof List) {
      List<?> list=(List<?>)node.getUserObject();
      //final String v = (String) ((DefaultMutableTreeNode) value).getUserObject();
      component = table;
      table.setModel(new DefaultTableModel() {
        @Override
        public int getRowCount() {
          return list.size();
        }
        @Override
        public int getColumnCount() {
          return _columnCount;
        }
        @Override
        public Object getValueAt(int atRow, int atColumn) {
          String atValue = "";
          try {
            Object object=list.get(atRow);
            if (object instanceof LuaValue[]) {
              LuaValue[] translations=(LuaValue[])object;
              atValue = translations[atColumn].optString("");
            }
          } catch (LuaError e) {
            e.printStackTrace();
          }
          
          return atValue;
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
