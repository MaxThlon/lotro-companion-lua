package delta.games.lotro.lua.turbine.ui.listbox;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LuaListCellRenderer extends DefaultListCellRenderer {

  @Override
  public Component getListCellRendererComponent(JList<?> list,
                                                Object value,
                                                int index,
                                                boolean isSelected,
                                                boolean cellHasFocus) {
    Component component = this;
    
    if (list.getModel().getElementAt(index) instanceof JPanel jPanel) {
      component = jPanel;
    } else {
      component = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      //l.setFont(tree.getFont().deriveFont(leaf ? 16f : 48f));
      //height = tree.getRowHeight();
      //height = leaf ? 20 : 60;
    }
    return component;
  }
}
