package delta.games.lotro.lua;

import static org.squiddev.cobalt.ValueFactory.valueOf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.UnwindThrowable;

import delta.games.lotro.lua.turbine.ui.UI;
import delta.games.lotro.lua.utils.URLToolsLua;


/**
 * Main test LuaJ.
 * @author DAM
 */
public class MainTestLua
{
  private static Logger LOGGER = Logger.getLogger(MainTestLua.class);
  
  private static void scriptSimpleTest() throws MalformedURLException, FileNotFoundException, IOException, LuaError, UnwindThrowable
  {
    /*MockedStatic<URLToolsLuaJ> urlTools = mockStatic(URLToolsLuaJ.class);

    urlTools.when(() -> URLToolsLuaJ.getFromClassPath(
        anyString()
    )).thenAnswer(invocation -> LuaValue.valueOf("file:src/main/resources/" + invocation.getArgument(0)));
*/
    InputStream script = new FileInputStream(URLToolsLua.getFromClassPath("test/simple.lua"));
    
    LuaJRunner luaJRunner = new LuaJRunner(script, valueOf("simple.lua"));
    try {
      luaJRunner.handleEvent(null, null);
    } catch (Exception exception){
      LOGGER.error("Exception Error: ", exception);
    }
  }

  private static void scriptTranslateTest() throws MalformedURLException, FileNotFoundException, IOException, LuaError, UnwindThrowable
  {
    InputStream script = new FileInputStream(URLToolsLua.getFromClassPath("test/translate-test.lua"));
    
    LuaJRunner luaJRunner = new LuaJRunner(script, valueOf("translate-test.lua"));
    try {
      luaJRunner.handleEvent(null, null);
    } catch (Exception exception){
      LOGGER.error("Exception Error: ", exception);
    }
  }

  private static void scriptUiTest() throws MalformedURLException, FileNotFoundException, IOException, LuaError, UnwindThrowable
  {
    InputStream script = new FileInputStream(URLToolsLua.getFromClassPath("test/ui-test.lua"));
    
    LuaJRunner luaJRunner = new LuaJRunner(script, valueOf("ui-test.lua"));
    try {
      luaJRunner.handleEvent(null, null);
    } catch (Exception exception){
      LOGGER.error("Exception Error: ", exception);
    }
    
    
  }

  /**
   * Method method for this game.
   * @param args Not used.
   * @throws UnwindThrowable 
   * @throws LuaError 
   */
  public static void main(String[] args) throws MalformedURLException, FileNotFoundException, IOException, LuaError, UnwindThrowable
  {
    //scriptSimpleTest();
    
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        UI.buildUI();
        try {
          

          scriptTranslateTest();
          //scriptUiTest();
        } catch (IOException | LuaError | UnwindThrowable e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });
  }
}
