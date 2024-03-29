package delta.games.lotro.lua.turbine.ui;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.xml.ws.Holder;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;

/**
 * LuaListBox library for lua scripts.
 * 
 * @author MaxThlon
 */
final class LuaListBox {
  /**
   * Initialize lua LuaListBox package
   * 
   * @param lua      .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
    LuaTools.pushClass(lua, "Turbine", "UI", "ScrollableControl");
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", LuaListBox::constructor);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "AddItem", LuaListBox::addItem);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "InsertItem", LuaListBox::insertItem);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "RemoveItem", LuaListBox::removeItem);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "RemoveItemAt", LuaListBox::removeItemAt);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetItem", LuaListBox::getItem);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetItem", LuaListBox::setItem);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetItemAt", LuaListBox::getItemAt);

    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "ContainsItem", LuaListBox::containsItem);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IndexOfItem", LuaListBox::indexOfItem);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetItemCount", LuaListBox::getItemCount);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "ClearItems", LuaListBox::clearItems);

    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetSelectedItem",
        LuaListBox::getSelectedItem);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetSelectedItem",
        LuaListBox::setSelectedItem);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetSelectedIndex",
        LuaListBox::getSelectedIndex);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetSelectedIndex",
        LuaListBox::setSelectedIndex);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetOrientation", LuaListBox::getOrientation);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetOrientation", LuaListBox::setOrientation);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetMaxItemsPerLine",
        LuaListBox::getMaxItemsPerLine);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetMaxItemsPerLine",
        LuaListBox::setMaxItemsPerLine);

    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetReverseFill", LuaListBox::getReverseFill);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetReverseFill", LuaListBox::setReverseFill);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Sort", LuaListBox::sort);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "EnsureVisible", LuaListBox::ensureVisible);

    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetMaxRows", LuaListBox::getMaxRows);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetMaxRows", LuaListBox::setMaxRows);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetMaxColumns", LuaListBox::getMaxColumns);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetMaxColumns", LuaListBox::setMaxColumns);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetFlippedLayout",
        LuaListBox::getFlippedLayout);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetFlippedLayout",
        LuaListBox::setFlippedLayout);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetIgnoreCellAlignment",
        LuaListBox::getIgnoreCellAlignment);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetIgnoreCellAlignment",
        LuaListBox::setIgnoreCellAlignment);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetWrapIndentAmount",
        LuaListBox::getWrapIndentAmount);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetWrapIndentAmount",
        LuaListBox::setWrapIndentAmount);

    lua.setField(-2, "ListBox");
  }

  @SuppressWarnings("unchecked")
  public static JList<JComponent> jListSelf(Lua lua, int index) {
    return (JList<JComponent>) LuaObject.objectSelf(lua, index, JScrollPane.class).getViewport().getView();
  }

  public static DefaultListModel<JComponent> jListModelSelf(Lua lua, int index) {
    return ((DefaultListModel<JComponent>) jListSelf(lua, index).getModel());
  }

  private static int constructor(Lua lua) {
    DefaultListModel<JComponent> model = new DefaultListModel<JComponent>();
    Holder<JScrollPane> jScrollPane = new Holder<JScrollPane>();

    LuaTools.invokeAndWait(lua, () -> {
      JList<JComponent> jList = GuiFactory.buildList();
      jList.setModel(model);
      jScrollPane.value = GuiFactory.buildScrollPane(jList);
    });
    LuaControl.controlInheritedConstructor(lua, 1, jScrollPane.value);

    return 1;
  }

  private static int addItem(Lua lua) {
    DefaultListModel<JComponent> model = jListModelSelf(lua, 1);
    JComponent element = LuaControl.findJComponentFromLuaObject(lua, 2);

    SwingUtilities.invokeLater(() -> model.addElement(element));
    return 0;
  }

  private static int insertItem(Lua lua) {
    DefaultListModel<JComponent> model = jListModelSelf(lua, 1);
    int index = (int) lua.toNumber(2);
    JComponent element = LuaControl.findJComponentFromLuaObject(lua, 3);

    SwingUtilities.invokeLater(() -> model.add(index, element));
    return 0;
  }

  private static int removeItem(Lua lua) {
    DefaultListModel<JComponent> model = jListModelSelf(lua, 1);
    JComponent element = LuaControl.findJComponentFromLuaObject(lua, 2);

    SwingUtilities.invokeLater(() -> model.removeElement(element));
    return 0;
  }

  private static int removeItemAt(Lua lua) {
    DefaultListModel<JComponent> model = jListModelSelf(lua, 1);
    int index = (int) lua.toNumber(2);

    SwingUtilities.invokeLater(() -> model.remove(index));
    return 0;
  }

  private static int getItem(Lua lua) {
    DefaultListModel<JComponent> model = jListModelSelf(lua, 1);
    int index = (int) lua.toNumber(2);

    LuaTools.invokeAndWait(lua, () -> LuaObject.findLuaObjectFromObject(model.get(index)).push());
    return 1;
  }

  private static int setItem(Lua lua) {
    DefaultListModel<JComponent> model = jListModelSelf(lua, 1);
    int index = (int) lua.toNumber(2);
    JComponent element = LuaControl.findJComponentFromLuaObject(lua, 3);

    SwingUtilities.invokeLater(() -> model.set(index, element));
    return 0;
  }

  private static int getItemAt(Lua lua) {
    lua.pushNil();
    return 1;
  }

  private static int containsItem(Lua lua) {
    DefaultListModel<JComponent> model = jListModelSelf(lua, 1);
    JComponent element = LuaControl.findJComponentFromLuaObject(lua, 2);

    LuaTools.invokeAndWait(lua, () -> lua.push(model.contains(element)));
    return 1;
  }

  private static int indexOfItem(Lua lua) {
    DefaultListModel<JComponent> model = jListModelSelf(lua, 1);
    JComponent element = LuaControl.findJComponentFromLuaObject(lua, 2);

    LuaTools.invokeAndWait(lua, () -> lua.push(model.indexOf(element)));
    return 1;
  }

  private static int getItemCount(Lua lua) {
    DefaultListModel<JComponent> model = jListModelSelf(lua, 1);

    LuaTools.invokeAndWait(lua, () -> lua.push(model.getSize()));
    return 1;
  }

  private static int clearItems(Lua lua) {
    DefaultListModel<JComponent> model = jListModelSelf(lua, 1);

    SwingUtilities.invokeLater(() -> model.clear());
    return 0;
  }

  private static int getSelectedItem(Lua lua) {
    JList<JComponent> jList = jListSelf(lua, 1);

    LuaTools.invokeAndWait(lua,
        () -> lua.push(LuaObject.findLuaObjectFromObject(jList.getSelectedValue()), Conversion.NONE));
    return 1;
  }

  private static int setSelectedItem(Lua lua) {
    JList<JComponent> jList = jListSelf(lua, 1);
    Object anObject = LuaControl.findJComponentFromLuaObject(lua, 2);

    SwingUtilities.invokeLater(() -> jList.setSelectedValue(anObject, true));
    return 0;
  }

  private static int getSelectedIndex(Lua lua) {
    JList<JComponent> jList = jListSelf(lua, 1);

    LuaTools.invokeAndWait(lua, () -> lua.push(jList.getSelectedIndex()));
    return 1;
  }

  private static int setSelectedIndex(Lua lua) {
    JList<JComponent> jList = jListSelf(lua, 1);
    int index = (int) lua.toNumber(2);

    SwingUtilities.invokeLater(() -> jList.setSelectedIndex(index));
    return 0;
  }

  private static int getOrientation(Lua lua) {
    lua.pushNil();
    return 1;
  }

  private static int setOrientation(Lua lua) {
    return 0;
  }

  private static int getMaxItemsPerLine(Lua lua) {
    lua.pushNil();
    return 1;
  }

  private static int setMaxItemsPerLine(Lua lua) {
    return 0;
  }

  private static int getReverseFill(Lua lua) {
    lua.pushNil();
    return 1;
  }

  private static int setReverseFill(Lua lua) {
    return 0;
  }

  private static int sort(Lua lua) {
    return 0;
  }

  private static int ensureVisible(Lua lua) {
    return 0;
  }

  private static int getMaxRows(Lua lua) {
    lua.pushNil();
    return 1;
  }

  private static int setMaxRows(Lua lua) {
    return 0;
  }

  private static int getMaxColumns(Lua lua) {
    lua.pushNil();
    return 1;
  }

  private static int setMaxColumns(Lua lua) {
    return 0;
  }

  private static int getFlippedLayout(Lua lua) {
    lua.pushNil();
    return 1;
  }

  private static int setFlippedLayout(Lua lua) {
    return 0;
  }

  private static int getIgnoreCellAlignment(Lua lua) {
    lua.pushNil();
    return 1;
  }

  private static int setIgnoreCellAlignment(Lua lua) {
    return 0;
  }

  private static int getWrapIndentAmount(Lua lua) {
    lua.pushNil();
    return 1;
  }

  private static int setWrapIndentAmount(Lua lua) {
    return 0;
  }
}
