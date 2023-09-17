package delta.games.lotro.lua.turbine.ui;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.common.ui.swing.GuiFactory;

/**
 * LuaListBox library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaListBox {

  public static void add(LuaState state,
                         LuaTable uiMetatable,
                         LuaFunction luaClass,
                         LuaValue luaScrollableControlClass) throws LuaError, UnwindThrowable {

    LuaTable luaListBoxClass = luaClass.call(state, luaScrollableControlClass).checkTable();
    RegisteredFunction.bind(luaListBoxClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaListBox::Constructor),
        /*
        AddItem   Adds an item to the list box.
        ClearItems  Clears all the items from the listbox.
        ContainsItem  Test is the list box contains an item.
        EnsureVisible
        GetItem   Gets the item at the specified index.
        GetItemCount
        GetMaxItemsPerLine
        GetOrientation
        GetReverseFill
        GetSelectedIndex  .
        GetSelectedItem
        IndexOfItem   Gets the index of an item in the list box.
        InsertItem
        RemoveItem  Removes an item from the list box.
        RemoveItemAt
        SetItem
        SetMaxItemsPerLine
        SetOrientation
        SetReverseFill
        SetSelectedIndex  .
        SetSelectedItem*/
    });
    
    uiMetatable.rawset("ListBox", luaListBoxClass);
  }

  public static LuaValue Constructor(LuaState state, LuaValue self) throws LuaError {
    
    DefaultListModel<JPanel> model = new DefaultListModel<>();
    JList<JPanel> jList = new JList<JPanel>(model);
    JScrollPane jScrollPane = GuiFactory.buildScrollPane(jList);
    LuaControl.ControlInheritedConstructor(state, self, jScrollPane);
    
    return Constants.NIL;
  }
}
