package delta.games.lotro.lua.turbine.ui;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * LuaLabel library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaLabel {

  public static Lua.LuaError add(Lua lua) {
  	Lua.LuaError error;
  	error = LuaObject.callInherit(lua, -3, "Turbine", "UI", "ScrollableControl");
  	if (error != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", LuaLabel::constructor);
  	LuaTools.setFunction(lua, -1, -3, "IsMultiline", LuaLabel::isMultiline);
  	LuaTools.setFunction(lua, -1, -3, "SetMultiline", LuaLabel::setMultiline);
  	LuaTools.setFunction(lua, -1, -3, "IsMarkupEnabled", LuaLabel::isMarkupEnabled);
  	LuaTools.setFunction(lua, -1, -3, "GetFont", LuaLabel::getFont);
  	LuaTools.setFunction(lua, -1, -3, "SetFont", LuaLabel::setFont);
  	LuaTools.setFunction(lua, -1, -3, "GetFontStyle", LuaLabel::getFontStyle);
  	LuaTools.setFunction(lua, -1, -3, "SetFontStyle", LuaLabel::setFontStyle);
  	LuaTools.setFunction(lua, -1, -3, "GetTextAlignment", LuaLabel::getTextAlignment);
  	LuaTools.setFunction(lua, -1, -3, "SetTextAlignment", LuaLabel::setTextAlignment);
  	LuaTools.setFunction(lua, -1, -3, "GetForeColor", LuaLabel::getForeColor);
  	LuaTools.setFunction(lua, -1, -3, "SetForeColor", LuaLabel::setForeColor);
  	LuaTools.setFunction(lua, -1, -3, "GetOutlineColor", LuaLabel::getOutlineColor);
  	LuaTools.setFunction(lua, -1, -3, "SetOutlineColor", LuaLabel::setOutlineColor);
    
  	LuaTools.setFunction(lua, -1, -3, "GetTextLength", LuaLabel::getTextLength);
    LuaTools.setFunction(lua, -1, -3, "GetText", LuaLabel::getText);
    LuaTools.setFunction(lua, -1, -3, "SetText", LuaLabel::setText);
    LuaTools.setFunction(lua, -1, -3, "AppendText", LuaLabel::appendText);
    LuaTools.setFunction(lua, -1, -3, "InsertText", LuaLabel::insertText);
    
    LuaTools.setFunction(lua, -1, -3, "IsSelectable", LuaLabel::isSelectable);
    LuaTools.setFunction(lua, -1, -3, "SetSelectable", LuaLabel::setSelectable);
    LuaTools.setFunction(lua, -1, -3, "SelectAll", LuaLabel::selectAll);
    LuaTools.setFunction(lua, -1, -3, "DeselectAll", LuaLabel::deselectAll);
    LuaTools.setFunction(lua, -1, -3, "SetSelection", LuaLabel::setSelection);
    LuaTools.setFunction(lua, -1, -3, "GetSelectedText", LuaLabel::getSelectedText);
    LuaTools.setFunction(lua, -1, -3, "SetSelectedText", LuaLabel::setSelectedText);
    LuaTools.setFunction(lua, -1, -3, "GetSelectionLength", LuaLabel::getSelectionLength);
    LuaTools.setFunction(lua, -1, -3, "SetSelectionLength", LuaLabel::setSelectionLength);
    LuaTools.setFunction(lua, -1, -3, "GetSelectionStart", LuaLabel::getSelectionStart);
    LuaTools.setFunction(lua, -1, -3, "SetSelectionStart", LuaLabel::setSelectionStart);

    lua.setField(-2, "Label");
    return error;
  }

  private static int constructor(Lua lua) {
    //MultilineLabel label = new MultilineLabel();
    JLabel label = new JLabel();
    LuaControl.controlInheritedConstructor(lua, 1, label);

    return 1;
  }
  
  private static int isMultiline(Lua lua) {
    return 1;
  }
  
  private static int setMultiline(Lua lua) {
    return 1;
  }

  private static int isMarkupEnabled(Lua lua) {
    return 1;
  }
  
  
  private static int getFont(Lua lua) {  
    return 1;
  }

  private static int setFont(Lua lua) {
    //MultilineLabel2 multilineLabel = self.metatag(lua, valueOf("objectSelf")).checkUserdata(MultilineLabel2.class);
    //multilineLabel.getDefaultStyle().getFont().    
    return 1;
  }
  
  private static int getFontStyle(Lua lua) {
    return 1;
  }
  
  private static int setFontStyle(Lua lua) { 
    return 1;
  }
  
  private static int getTextAlignment(Lua lua) {
    return 1;
  }
  
  private static int setTextAlignment(Lua lua) {
    return 1;
  }
  
  private static int getForeColor(Lua lua) {
    return 1;
  }
  
  private static int setForeColor(Lua lua) {
    return 1;
  }
  
  private static int getOutlineColor(Lua lua) {
    return 1;
  }
  
  private static int setOutlineColor(Lua lua) {
    return 1;
  }

  
  private static int getTextLength(Lua lua) {
    return 1;
  }
  
  private static int getText(Lua lua) {
    JComponent jComponent = LuaObject.objectSelf(lua, 1, JComponent.class);
    if (JLabel.class.isAssignableFrom(jComponent.getClass())) {
    	return LuaTools.invokeAndWait(lua, () -> lua.push(((JLabel)jComponent).getText()));
    } else if (JTextArea.class.isAssignableFrom(jComponent.getClass())) {
    	return LuaTools.invokeAndWait(lua, () -> lua.push(((JTextArea)jComponent).getText()));
    } else if (JButton.class.isAssignableFrom(jComponent.getClass())) {
    	return LuaTools.invokeAndWait(lua, () -> lua.push(((JButton)jComponent).getText()));
    }
    lua.push("Type error in LuaLabel:GetText");
    return -1;
  }
  
  private static int setText(Lua lua) {
    JComponent jComponent = LuaObject.objectSelf(lua, 1, JComponent.class);
    String value = lua.toString(2);

    if (JLabel.class.isAssignableFrom(jComponent.getClass())) {    	
    	SwingUtilities.invokeLater(() -> ((JLabel)jComponent).setText(value));
      return 1;
    } else if (JButton.class.isAssignableFrom(jComponent.getClass())) {
    	SwingUtilities.invokeLater(() -> ((JButton)jComponent).setText(value));
      return 1;
    }

    lua.push("Type error in LuaLabel:SetText");
    return -1;
  }
  
  private static int appendText(Lua lua) {
    return 1;
  }
  
  private static int insertText(Lua lua) {
    return 1;
  }  
  
  private static int isSelectable(Lua lua) {
    return 1;
  }
  
  private static int setSelectable(Lua lua) {
    return 1;
  }
  
  private static int selectAll(Lua lua) {
    return 1;
  }
  
  private static int deselectAll(Lua lua) {
    return 1;
  }
  
  private static int setSelection(Lua lua) {
    return 1;
  }
  
  private static int getSelectedText(Lua lua) {
    return 1;
  }
  
  private static int setSelectedText(Lua lua) {
    return 1;
  }
  
  private static int getSelectionLength(Lua lua) {
    return 1;
  }
  
  private static int setSelectionLength(Lua lua) {
    return 1;
  }

  private static int getSelectionStart(Lua lua) {
    return 1;
  }
  
  private static int setSelectionStart(Lua lua) {
    return 1;
  }
}
