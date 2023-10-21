package delta.games.lotro.lua.turbine.ui;

import static org.squiddev.cobalt.ValueFactory.valueOf;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.ErrorFactory;
import org.squiddev.cobalt.LuaBoolean;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaNumber;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaString;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.OperationHelper;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.common.ui.swing.labels.MultilineLabel;
import delta.games.lotro.lua.utils.LuaTools;

/**
 * LuaLabel library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaLabel {

  public static LuaTable add(LuaState state,
                             LuaTable uiEnv,
                             LuaFunction luaClass,
                             LuaValue luaScrollableControlClass) throws LuaError, UnwindThrowable {

    LuaTable luaLabelClass = OperationHelper.call(state, luaClass, luaScrollableControlClass).checkTable();

    RegisteredFunction.bind(luaLabelClass, new RegisteredFunction[]{
        RegisteredFunction.of("Constructor", LuaLabel::Constructor),
        RegisteredFunction.of("IsMultiline", LuaLabel::IsMultiline),
        RegisteredFunction.of("SetMultiline", LuaLabel::SetMultiline),
        RegisteredFunction.of("IsMarkupEnabled", LuaLabel::IsMarkupEnabled),
        
        RegisteredFunction.of("GetFont", LuaLabel::GetFont),
        RegisteredFunction.of("SetFont", LuaLabel::SetFont),
        RegisteredFunction.of("GetFontStyle", LuaLabel::GetFontStyle),
        RegisteredFunction.of("SetFontStyle", LuaLabel::SetFontStyle),
        RegisteredFunction.of("GetTextAlignment", LuaLabel::GetTextAlignment),
        RegisteredFunction.of("SetTextAlignment", LuaLabel::SetTextAlignment),
        RegisteredFunction.of("GetForeColor", LuaLabel::GetForeColor),
        RegisteredFunction.of("SetForeColor", LuaLabel::SetForeColor),
        RegisteredFunction.of("GetOutlineColor", LuaLabel::GetOutlineColor),
        RegisteredFunction.of("SetOutlineColor", LuaLabel::SetOutlineColor),
        
        RegisteredFunction.of("GetTextLength", LuaLabel::GetTextLength),
        RegisteredFunction.of("GetText", LuaLabel::GetText),
        RegisteredFunction.of("SetText", LuaLabel::SetText),
        RegisteredFunction.of("AppendText", LuaLabel::AppendText),
        RegisteredFunction.of("InsertText", LuaLabel::InsertText),


        
        RegisteredFunction.of("IsSelectable", LuaLabel::IsSelectable),
        RegisteredFunction.of("SetSelectable", LuaLabel::SetSelectable),
        RegisteredFunction.of("SelectAll", LuaLabel::SelectAll),
        RegisteredFunction.of("DeselectAll", LuaLabel::DeselectAll),
        RegisteredFunction.of("SetSelection", LuaLabel::SetSelection),
        RegisteredFunction.of("GetSelectedText", LuaLabel::GetSelectedText),
        RegisteredFunction.of("SetSelectedText", LuaLabel::SetSelectedText),
        RegisteredFunction.of("GetSelectionLength", LuaLabel::GetSelectionLength),
        RegisteredFunction.of("SetSelectionLength", LuaLabel::SetSelectionLength),
        RegisteredFunction.of("SetSelectionStart", LuaLabel::SetSelectionStart),
        RegisteredFunction.of("GetSelectionStart", LuaLabel::GetSelectionStart)
    });
    
    uiEnv.rawset("Label", luaLabelClass);
    
    return luaLabelClass;
  }

  public static LuaValue Constructor(LuaState state, LuaValue self) throws LuaError {
    //MultilineLabel label = new MultilineLabel();
    JLabel label = new JLabel();
    LuaControl.controlInheritedConstructor(state, self, label);

    return Constants.NIL;
  }
  
  public static LuaBoolean IsMultiline(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
  
  public static LuaValue SetMultiline(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }

  public static LuaBoolean IsMarkupEnabled(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
  
  
  public static LuaValue GetFont(LuaState state, LuaValue self) {  
    return Constants.NIL;
  }

  public static LuaValue SetFont(LuaState state, LuaValue self, LuaValue value) {
    //MultilineLabel2 multilineLabel = self.metatag(state, valueOf("objectSelf")).checkUserdata(MultilineLabel2.class);
    //multilineLabel.getDefaultStyle().getFont().    
    return Constants.NIL;
  }
  
  public static LuaValue GetFontStyle(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue SetFontStyle(LuaState state, LuaValue self, LuaValue value) { 
    return Constants.NIL;
  }
  
  public static LuaValue GetTextAlignment(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue SetTextAlignment(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue GetForeColor(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue SetForeColor(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue GetOutlineColor(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue SetOutlineColor(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }

  
  public static LuaNumber GetTextLength(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaString GetText(LuaState state, LuaValue self) throws LuaError {
    JComponent jComponent = LuaTools.objectSelf(state, self, JComponent.class);
    if (JLabel.class.isAssignableFrom(jComponent.getClass())) {
      return valueOf(((JLabel)jComponent).getText());
    } else if (JTextArea.class.isAssignableFrom(jComponent.getClass())) {
      return valueOf(((JTextArea)jComponent).getText());
    } else if (JButton.class.isAssignableFrom(jComponent.getClass())) {
      return valueOf(((JButton)jComponent).getText());
    } else throw ErrorFactory.typeError(self, MultilineLabel.class.getName());
  }
  
  public static LuaValue SetText(LuaState state, LuaValue self, LuaValue value) throws LuaError {
    JComponent jComponent = LuaTools.objectSelf(state, self, JComponent.class);
    if (JLabel.class.isAssignableFrom(jComponent.getClass())) {
      ((JLabel)jComponent).setText(value.checkString());
    } else if (JButton.class.isAssignableFrom(jComponent.getClass())) {
      ((JButton)jComponent).setText(value.checkString());
    } else throw ErrorFactory.typeError(self, MultilineLabel.class.getName());

    return Constants.NIL;
  }
  
  public static LuaValue AppendText(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue InsertText(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }  
  
  public static LuaValue IsSelectable(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue SetSelectable(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue SelectAll(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue DeselectAll(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue SetSelection(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaString GetSelectedText(LuaState state, LuaValue self) {
    return Constants.EMPTYSTRING;
  }
  
  public static LuaValue SetSelectedText(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaNumber GetSelectionLength(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaValue SetSelectionLength(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue SetSelectionStart(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
  
  public static LuaValue GetSelectionStart(LuaState state, LuaValue self, LuaValue value) {
    return Constants.NIL;
  }
}
