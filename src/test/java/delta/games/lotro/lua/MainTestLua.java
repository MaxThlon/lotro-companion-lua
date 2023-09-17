package delta.games.lotro.lua;

import static org.squiddev.cobalt.ValueFactory.valueOf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.UnwindThrowable;

import delta.games.lotro.lua.turbine.ui.UI;
import delta.games.lotro.lua.utils.URLToolsLua;


/**
 * Main test Lua.
 * @author MaxThlon
 */
public class MainTestLua
{
  private static Logger LOGGER = Logger.getLogger(MainTestLua.class);
  
  public static void scriptSimpleTest() throws FileNotFoundException, IOException, LuaError
  {
    /*MockedStatic<URLToolsLuaJ> urlTools = mockStatic(URLToolsLuaJ.class);

    urlTools.when(() -> URLToolsLuaJ.getFromClassPath(
        anyString()
    )).thenAnswer(invocation -> LuaValue.valueOf("file:src/main/resources/" + invocation.getArgument(0)));
*/
    InputStream script = new FileInputStream(URLToolsLua.getFromClassPath("test/simple.lua"));
    
    LuaRunner luaJRunner = new LuaRunner(script, valueOf("simple.lua"));
    try {
      luaJRunner.handleEvent(null, null);
    } catch (Exception exception){
      LOGGER.error("Exception Error: ", exception);
    }
  }

  public static void scriptTranslateTest() throws FileNotFoundException, IOException, LuaError
  {
    InputStream script = new FileInputStream(URLToolsLua.getFromClassPath("test/translate-test.lua"));
    
    LuaRunner luaJRunner = new LuaRunner(script, valueOf("translate-test.lua"));
    try {
      luaJRunner.handleEvent(null, null);
    } catch (Exception exception){
      LOGGER.error("Exception Error: ", exception);
    }
  }

  public static void scriptUiTest() throws FileNotFoundException, IOException, LuaError
  {
    InputStream script = new FileInputStream(URLToolsLua.getFromClassPath("test/ui-test.lua"));
    
    LuaRunner luaJRunner = new LuaRunner(script, valueOf("ui-test.lua"));
    try {
      luaJRunner.handleEvent(null, null);
    } catch (Exception exception){
      LOGGER.error("Exception Error: ", exception);
    }
    
    
  }

  /**
   * main for UI tests.
   * @param args Not used.
   * @throws UnwindThrowable 
   * @throws LuaError 
   */
  public static void main(String[] args) throws LuaError, UnwindThrowable
  {
    //scriptSimpleTest();
    
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        UI.buildUI();
        try {
          //scriptTranslateTest();
          scriptUiTest();
        } catch (IOException | LuaError e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });
  }
}
