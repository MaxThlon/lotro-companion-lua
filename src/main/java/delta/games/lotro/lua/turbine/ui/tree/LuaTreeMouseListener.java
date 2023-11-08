package delta.games.lotro.lua.turbine.ui.tree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.value.LuaValue;

/**
 * LuaTreeMouseListener class
 * @author MaxThlon
 */
public class LuaTreeMouseListener implements MouseListener {
  private JTree _tree;

  public LuaTreeMouseListener(JTree tree) {
    _tree = tree;
  }

  @Override
  public void mousePressed(MouseEvent mouseEvent) {
    TreePath treePath = _tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
    if (treePath != null) {
      Object lastPathComponent = treePath.getLastPathComponent();
      if (lastPathComponent != null) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)lastPathComponent;

        LuaValue luaNode=LuaObject.findLuaObjectFromObject(node);
        LuaValue luaNodeCallBackFunc = luaNode.get("MouseDown");
        if (luaNodeCallBackFunc != null) {
          LuaTools.invokeEvent(
          		luaNode.state(),
          		luaNodeCallBackFunc,
              new LuaValue[]{ luaNode });
        }
      }
    }
  }

  @Override
  public void mouseClicked(MouseEvent e)
  {
    // ToDo Auto-generated method stub
    
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
    // ToDo Auto-generated method stub
    
  }

  @Override
  public void mouseEntered(MouseEvent e)
  {
    // ToDo Auto-generated method stub
    
  }

  @Override
  public void mouseExited(MouseEvent e)
  {
    // ToDo Auto-generated method stub
    
  }
}
