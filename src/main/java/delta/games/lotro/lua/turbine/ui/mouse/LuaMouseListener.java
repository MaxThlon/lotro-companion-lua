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

import delta.games.lotro.lua.turbine.ui.LuaControl;

/**
 * LuaMouseListener library for lua scripts.
 * @author MaxThlon
 */
public abstract class LuaMouseListener implements MouseListener {
  
  public LuaValue luaMouseDown = null,
                  luaMouseClick = null,
                  luaMouseUp = null,
                  luaMouseEnter = null,
                  luaMouseLeave = null;
  
  public static LuaMouseListener luaIndexMetaFunc(LuaState state, LuaValue self) throws LuaError {
    Component component = LuaControl.findJComponentFromLuaObject(state, self);

    return (LuaMouseListener)Arrays.stream(component.getMouseListeners()).findFirst().orElseGet(() -> {
      LuaMouseListener luaMouseListener = new LuaMouseListener() {
        @Override
        public void mousePressed(MouseEvent  mouseEvent) {
          try {
            if (this.luaMouseDown != null) OperationHelper.call(state, this.luaMouseDown, self, tableOf());
          } catch (LuaError | UnwindThrowable error) {
            error.printStackTrace();
          }
        }
  
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
          try {
            if (this.luaMouseClick != null) OperationHelper.call(state, this.luaMouseClick, self, tableOf());
          } catch (LuaError | UnwindThrowable error) {
            error.printStackTrace();
          }
        }
  
        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
          try {
            if (this.luaMouseUp != null) OperationHelper.call(state, this.luaMouseUp, self, tableOf());
          } catch (LuaError | UnwindThrowable error) {
            error.printStackTrace();
          }
        }
  
        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
          try {
            if (this.luaMouseEnter != null) OperationHelper.call(state, this.luaMouseEnter, self, tableOf());
          } catch (LuaError | UnwindThrowable error) {
            error.printStackTrace();
          }
        }
  
        @Override
        public void mouseExited(MouseEvent mouseEvent) {
          try {
            if (this.luaMouseLeave != null) OperationHelper.call(state, this.luaMouseLeave, self, tableOf());
          } catch (LuaError | UnwindThrowable error) {
            error.printStackTrace();
          }
        }
      };
      component.addMouseListener(luaMouseListener);
      return luaMouseListener;
    });
  }
}
