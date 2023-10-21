package delta.games.lotro.lua.turbine.ui;

import static org.squiddev.cobalt.ValueFactory.valueOf;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaNumber;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.utils.LuaTools;

/**
 * LuaListBox library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaListBox {

  public static void add(LuaState state,
                         LuaTable uiEnv,
                         LuaValue luaScrollableControlClass) throws LuaError, UnwindThrowable {

    LuaTable luaListBoxClass = Turbine._luaClass.call(state, luaScrollableControlClass).checkTable();
    RegisteredFunction.bind(luaListBoxClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaListBox::Constructor),
        RegisteredFunction.of("AddItem", LuaListBox::addItem),
        RegisteredFunction.of("InsertItem", LuaListBox::insertItem),
        RegisteredFunction.of("RemoveItem", LuaListBox::removeItem),
        RegisteredFunction.of("RemoveItemAt", LuaListBox::removeItemAt),
        RegisteredFunction.of("GetItem", LuaListBox::getItem),
        RegisteredFunction.of("SetItem", LuaListBox::setItem),
        RegisteredFunction.of("ContainsItem", LuaListBox::containsItem),
        RegisteredFunction.of("IndexOfItem", LuaListBox::indexOfItem),
        RegisteredFunction.of("GetItemCount", LuaListBox::getItemCount),
        RegisteredFunction.of("ClearItems", LuaListBox::clearItems),
        
        
        RegisteredFunction.of("GetSelectedItem", LuaListBox::getSelectedItem),
        RegisteredFunction.of("SetSelectedItem", LuaListBox::setSelectedItem),
        RegisteredFunction.of("GetSelectedIndex", LuaListBox::getSelectedIndex),
        RegisteredFunction.of("SetSelectedIndex", LuaListBox::setSelectedIndex),
        RegisteredFunction.of("GetOrientation", LuaListBox::getOrientation),
        RegisteredFunction.of("SetOrientation", LuaListBox::setOrientation),
        RegisteredFunction.of("GetMaxItemsPerLine", LuaListBox::getMaxItemsPerLine),
        RegisteredFunction.of("SetMaxItemsPerLine", LuaListBox::setMaxItemsPerLine),

        RegisteredFunction.of("GetReverseFill", LuaListBox::getReverseFill),
        RegisteredFunction.of("SetReverseFill", LuaListBox::setReverseFill),
        RegisteredFunction.of("Sort", LuaListBox::sort),
        RegisteredFunction.of("EnsureVisible", LuaListBox::ensureVisible),
    });
    
    uiEnv.rawset("ListBox", luaListBoxClass);
  }
  
  @SuppressWarnings("unchecked")
  public static JList<JComponent> jListSelf(LuaState state, LuaValue self) throws LuaError {
    return (JList<JComponent>)LuaTools.objectSelf(state, self, JScrollPane.class).getViewport().getView();
  }

  public static DefaultListModel<JComponent> jListModelSelf(LuaState state, LuaValue self) throws LuaError {
    return ((DefaultListModel<JComponent>)jListSelf(state, self).getModel());
  }

  public static LuaValue Constructor(LuaState state, LuaValue self) throws LuaError {
    DefaultListModel<JComponent> model = new DefaultListModel<JComponent>();
    JList<JComponent> jList = GuiFactory.buildList();
    jList.setModel(model);
    JScrollPane jScrollPane = GuiFactory.buildScrollPane(jList);

    LuaControl.controlInheritedConstructor(state, self, jScrollPane);
    
    return Constants.NIL;
  }
  
  public static LuaValue addItem(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    jListModelSelf(state, self).addElement(LuaControl.findJComponentFromLuaObject(state,value));
    return Constants.NIL;
  }
  
  public static LuaValue insertItem(LuaState state, LuaValue self, LuaValue index, LuaValue value) throws LuaError {
    jListModelSelf(state, self).add(
        index.checkInteger(),
        LuaControl.findJComponentFromLuaObject(state,value)
    );
    return Constants.NIL;
  }
  
  public static LuaValue removeItem(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    jListModelSelf(state, self).removeElement(LuaControl.findJComponentFromLuaObject(state,value));
    return Constants.NIL;
  }
  
  public static LuaValue removeItemAt(LuaState state, LuaValue self, LuaValue index) throws LuaError {
    jListModelSelf(state, self).remove(index.checkInteger());
    return Constants.NIL;
  }
  
  public static LuaValue getItem(LuaState state, LuaValue self, LuaValue index) throws LuaError {
    return LuaTools.findLuaObjectFromObject(jListModelSelf(state, self).get(index.checkInteger()));
  }
  
  public static LuaValue setItem(LuaState state, LuaValue self, LuaValue index, LuaValue value) throws LuaError {
    jListModelSelf(state, self).set(
        index.checkInteger(),
        LuaControl.findJComponentFromLuaObject(state,value)
    );
    return Constants.NIL;
  }
  
  public static LuaValue containsItem(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    jListModelSelf(state, self).contains(LuaControl.findJComponentFromLuaObject(state,value));
    return Constants.NIL;
  }
  
  public static LuaValue indexOfItem(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    jListModelSelf(state, self).indexOf(LuaControl.findJComponentFromLuaObject(state,value));
    return Constants.NIL;
  }
  
  public static LuaNumber getItemCount(LuaState state, LuaValue self, LuaValue value) {
    return Constants.ZERO;
  }
  
  public static LuaValue clearItems(LuaState state, LuaValue self) throws LuaError {
    jListModelSelf(state, self).clear();
    return Constants.NIL;
  }
  
  public static LuaValue getSelectedItem(LuaState state, LuaValue self) throws LuaError {
    LuaTools.findLuaObjectFromObject(jListSelf(state, self).getSelectedValue());
    return Constants.NIL;
  }
  
  public static LuaValue setSelectedItem(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    jListSelf(state, self).setSelectedValue(LuaControl.findJComponentFromLuaObject(state,value), true);
    return Constants.NIL;
  }
  
  public static LuaNumber getSelectedIndex(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    return valueOf(jListSelf(state, self).getSelectedIndex());
  }
  
  public static LuaValue setSelectedIndex(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    jListSelf(state, self).setSelectedIndex(value.checkInteger());
    return Constants.NIL;
  }
  
  public static LuaValue getOrientation(LuaState state, LuaValue self) throws LuaError {
    return Constants.NIL;
  }
  
  public static LuaValue setOrientation(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    return Constants.NIL;
  }
  
  public static LuaNumber getMaxItemsPerLine(LuaState state, LuaValue self) throws LuaError {
    return Constants.ZERO;
  }
  
  public static LuaValue setMaxItemsPerLine(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    return Constants.NIL;
  }
  
  public static LuaValue getReverseFill(LuaState state, LuaValue self) throws LuaError {
    return Constants.NIL;
  }
  
  public static LuaValue setReverseFill(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    return Constants.NIL;
  }
  
  public static LuaValue sort(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    return Constants.NIL;
  }
  
  public static LuaValue ensureVisible(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    return Constants.NIL;
  }
}
