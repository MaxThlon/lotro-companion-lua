package delta.games.lotro.lua.turbine.ui.listbox;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * LuaListCellRenderer library for lua scripts.
 * @author MaxThlon
 */
public class LuaListCellRenderer extends DefaultListCellRenderer {

  @Override
  public Component getListCellRendererComponent(JList<?> list,
                                                Object value,
                                                int index,
                                                boolean isSelected,
                                                boolean cellHasFocus) {
    Component component = this;
    Object object=list.getModel().getElementAt(index);
    JPanel jPanel=(object instanceof JPanel)?(JPanel)object:null;
    if (jPanel != null) {
      component = jPanel;
    } else {
      component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      //l.setFont(tree.getFont().deriveFont(leaf ? 16f : 48f));
      //height = tree.getRowHeight();
      //height = leaf ? 20 : 60;
    }
    return component;
  }
}
