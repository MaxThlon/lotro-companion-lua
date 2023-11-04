package delta.games.lotro.lua.turbine.ui.mouse;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.value.LuaValue;

/**
 * LuaMouseListener library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaMouseListener implements MouseListener {
  
  public LuaValue _luaMouseDown = null,
                  _luaMouseClick = null,
                  _luaMouseUp = null,
                  _luaMouseEnter = null,
                  _luaMouseLeave = null;
  
  public static LuaMouseListener luaIndexMetaFunc(Lua lua, Component component, LuaValue self) {
    
    return Arrays.stream(component.getMouseListeners())
                 .filter(LuaMouseListener.class::isInstance)
                 .map(LuaMouseListener.class::cast)
                 .findFirst().orElseGet(() -> {
      LuaMouseListener luaMouseListener = new LuaMouseListener() {
        @Override
        public void mousePressed(MouseEvent  mouseEvent) {
          if (_luaMouseDown != null) {
            LuaTools.invokeEvent(lua, "MouseDown", new Object[]{_luaMouseDown, self});
          }
        }
  
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
          if (_luaMouseClick != null) {
            LuaTools.invokeEvent(lua, "MouseClick", new Object[]{_luaMouseClick, self});
          }
        }
  
        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
          if (_luaMouseUp != null) {
            LuaTools.invokeEvent(lua, "MouseUp", new Object[]{_luaMouseUp, self});
          }
        }
  
        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
          if (_luaMouseEnter != null) {
            LuaTools.invokeEvent(lua, "MouseEnter", new Object[]{_luaMouseEnter, self});
          }
        }
  
        @Override
        public void mouseExited(MouseEvent mouseEvent) {
          if (_luaMouseLeave != null) {
            LuaTools.invokeEvent(lua, "MouseLeave", new Object[]{_luaMouseLeave, self});
          }
        }
      };
      component.addMouseListener(luaMouseListener);
      return luaMouseListener;
    });
  }
}
