package delta.games.lotro.lua.turbine.ui.tree;

import static org.squiddev.cobalt.ValueFactory.tableOf;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.function.LuaFunction;

import delta.common.framework.plugin.PluginManager;
import delta.games.lotro.lua.turbine.Apartment;
import delta.games.lotro.lua.utils.LuaTools;

/**
 * @author MaxThlon
 */
public class LuaTreeMouseListener implements MouseListener {
  private static Logger LOGGER = Logger.getLogger(LuaTreeMouseListener.class);
  
  private JTree _tree;
  private LuaState _state;

  public LuaTreeMouseListener(JTree tree, LuaState state) {
    _tree = tree;
    _state = state;
  }

  @Override
  public void mousePressed(MouseEvent mouseEvent) {
    TreePath treePath = _tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
    if (treePath != null) {
      Object lastPathComponent = treePath.getLastPathComponent();
      if (lastPathComponent != null) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)lastPathComponent;
        try
        {
          LuaTable luaNode=LuaTools.findLuaObjectFromObject(node).checkTable();
          LuaFunction luaNodeCallBackFunc = luaNode.rawget("MouseDown").optFunction(null);
          if (luaNodeCallBackFunc != null) {
            PluginManager.getInstance().event(
                "MouseDown",
                new Object[]{Apartment.findApartment(_state), luaNodeCallBackFunc, luaNode, tableOf()});
          }
        }
        catch (LuaError e)
        {
          LOGGER.error(e);
          
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
