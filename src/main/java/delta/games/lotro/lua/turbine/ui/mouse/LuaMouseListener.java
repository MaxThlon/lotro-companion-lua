package delta.games.lotro.lua.turbine.ui.mouse;

import static org.squiddev.cobalt.ValueFactory.tableOf;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.OperationHelper;
import org.squiddev.cobalt.UnwindThrowable;

import delta.common.framework.plugin.PluginManager;
import delta.games.lotro.lua.turbine.Apartment;
import delta.games.lotro.lua.turbine.ui.LuaControl;

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
  
  public static LuaMouseListener luaIndexMetaFunc(LuaState state, LuaValue self) throws LuaError {
    Component component = LuaControl.findComponentFromLuaObject(state, self);
    return Arrays.stream(component.getMouseListeners())
                 .filter(LuaMouseListener.class::isInstance)
                 .map(LuaMouseListener.class::cast)
                 .findFirst().orElseGet(() -> {
      LuaMouseListener luaMouseListener = new LuaMouseListener() {
        @Override
        public void mousePressed(MouseEvent  mouseEvent) {
          try {
            if (_luaMouseDown != null) {
              PluginManager.getInstance().event("MouseDown", new Object[]{Apartment.findApartment(state), _luaMouseDown, self, tableOf()});
            }
          } catch (LuaError error) {
            error.printStackTrace();
          }
        }
  
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
          try {
            if (_luaMouseClick != null) {
              PluginManager.getInstance().event("MouseClick", new Object[]{Apartment.findApartment(state), _luaMouseClick, self, tableOf()});
            }
          } catch (LuaError error) {
            error.printStackTrace();
          }
        }
  
        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
          try {
            if (_luaMouseUp != null) {
              PluginManager.getInstance().event("MouseUp", new Object[]{Apartment.findApartment(state), _luaMouseUp, self, tableOf()});
            }
          } catch (LuaError error) {
            error.printStackTrace();
          }
        }
  
        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
          try {
            if (_luaMouseEnter != null) {
              PluginManager.getInstance().event("MouseEnter", new Object[]{Apartment.findApartment(state), _luaMouseEnter, self, tableOf()});
            }
          } catch (LuaError error) {
            error.printStackTrace();
          }
        }
  
        @Override
        public void mouseExited(MouseEvent mouseEvent) {
          try {
            if (_luaMouseLeave != null) {
              PluginManager.getInstance().event("MouseLeave", new Object[]{Apartment.findApartment(state), _luaMouseLeave, self, tableOf()});
            }
          } catch (LuaError error) {
            error.printStackTrace();
          }
        }
      };
      component.addMouseListener(luaMouseListener);
      return luaMouseListener;
    });
  }
}
