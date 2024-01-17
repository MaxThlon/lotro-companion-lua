package delta.games.lotro.lua.turbine.ui;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.xml.ws.Holder;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * LuaLabel library for lua scripts.
 * @author MaxThlon
 */
final class LuaLabel {
  /**
   * Initialize lua LuaLabel package
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
  	LuaTools.pushClass(lua, "Turbine", "UI", "ScrollableControl");
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", LuaLabel::constructor);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsMultiline", LuaLabel::isMultiline);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetMultiline", LuaLabel::setMultiline);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsMarkupEnabled", LuaLabel::isMarkupEnabled);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetMarkupEnabled", LuaLabel::setMarkupEnabled);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetFont", LuaLabel::getFont);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetFont", LuaLabel::setFont);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetFontStyle", LuaLabel::getFontStyle);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetFontStyle", LuaLabel::setFontStyle);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetTextAlignment", LuaLabel::getTextAlignment);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetTextAlignment", LuaLabel::setTextAlignment);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetForeColor", LuaLabel::getForeColor);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetForeColor", LuaLabel::setForeColor);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetOutlineColor", LuaLabel::getOutlineColor);
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetOutlineColor", LuaLabel::setOutlineColor);
    
  	LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetTextLength", LuaLabel::getTextLength);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetText", LuaLabel::getText);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetText", LuaLabel::setText);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "AppendText", LuaLabel::appendText);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "InsertText", LuaLabel::insertText);
    
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsSelectable", LuaLabel::isSelectable);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetSelectable", LuaLabel::setSelectable);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SelectAll", LuaLabel::selectAll);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "DeselectAll", LuaLabel::deselectAll);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetSelection", LuaLabel::setSelection);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetSelectedText", LuaLabel::getSelectedText);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetSelectedText", LuaLabel::setSelectedText);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetSelectionLength", LuaLabel::getSelectionLength);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetSelectionLength", LuaLabel::setSelectionLength);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetSelectionStart", LuaLabel::getSelectionStart);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "SetSelectionStart", LuaLabel::setSelectionStart);

    lua.setField(LuaTools.relativizeIndex(envIndex, -1), "Label");
  }

  private static int constructor(Lua lua) {
  	Holder<JLabel> label = new Holder<JLabel>();
    //MultilineLabel label = new MultilineLabel();

    LuaTools.invokeAndWait(lua, () -> label.value = GuiFactory.buildLabel(""));
    LuaControl.controlInheritedConstructor(lua, 1, label.value);

    return 1;
  }
  
  private static int isMultiline(Lua lua) {
    return 1;
  }
  
  private static int setMultiline(Lua lua) {
    return 0;
  }

  private static int isMarkupEnabled(Lua lua) {
    return 1;
  }
  
  private static int setMarkupEnabled(Lua lua) {
    return 0;
  }
  
  private static int getFont(Lua lua) {  
    return 1;
  }

  private static int setFont(Lua lua) {
    //MultilineLabel2 multilineLabel = self.metatag(lua, valueOf("objectSelf")).checkUserdata(MultilineLabel2.class);
    //multilineLabel.getDefaultStyle().getFont().    
    return 0;
  }
  
  private static int getFontStyle(Lua lua) {
    return 1;
  }
  
  private static int setFontStyle(Lua lua) { 
    return 0;
  }
  
  private static int getTextAlignment(Lua lua) {
    return 1;
  }
  
  private static int setTextAlignment(Lua lua) {
    return 0;
  }
  
  private static int getForeColor(Lua lua) {
    return 1;
  }
  
  private static int setForeColor(Lua lua) {
    return 0;
  }
  
  private static int getOutlineColor(Lua lua) {
    return 1;
  }
  
  private static int setOutlineColor(Lua lua) {
    return 0;
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
      return 0;
    } else if (JButton.class.isAssignableFrom(jComponent.getClass())) {
    	SwingUtilities.invokeLater(() -> ((JButton)jComponent).setText(value));
      return 0;
    }

    lua.push("Type error in LuaLabel:SetText");
    return -1;
  }
  
  private static int appendText(Lua lua) {
    return 0;
  }
  
  private static int insertText(Lua lua) {
    return 0;
  }  
  
  private static int isSelectable(Lua lua) {
    return 1;
  }
  
  private static int setSelectable(Lua lua) {
    return 0;
  }
  
  private static int selectAll(Lua lua) {
    return 0;
  }
  
  private static int deselectAll(Lua lua) {
    return 0;
  }
  
  private static int setSelection(Lua lua) {
    return 0;
  }
  
  private static int getSelectedText(Lua lua) {
    return 1;
  }
  
  private static int setSelectedText(Lua lua) {
    return 0;
  }
  
  private static int getSelectionLength(Lua lua) {
    return 1;
  }
  
  private static int setSelectionLength(Lua lua) {
    return 0;
  }

  private static int getSelectionStart(Lua lua) {
    return 1;
  }
  
  private static int setSelectionStart(Lua lua) {
    return 0;
  }
}
