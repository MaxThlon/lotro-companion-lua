package delta.games.lotro.lua;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.UnwindThrowable;

import com.eleet.dragonconsole.CommandProcessor;

import delta.common.utils.url.URLTools;
import delta.games.lotro.lua.turbine.ui.UI;
import delta.games.lotro.lua.utils.URLToolsLua;


/**
 * Main test Lua.
 * @author MaxThlon
 */
public class MainTestLua
{
  private static Logger LOGGER = Logger.getLogger(MainTestLua.class);
  
  public static void scriptSimpleTest(CommandProcessor commandProcessor) throws FileNotFoundException, IOException
  {
    InputStream script = new FileInputStream(URLToolsLua.getFromClassPath("test/simple.lua"));
    
    LuaRunner luaRunner = new LuaRunner(commandProcessor);
    try {
      luaRunner.initPackageLib(Paths.get(URLTools.getFromClassPath("test", LuaRunner.class).toURI()));
      luaRunner.bootstrapLotro("simple.lua", script);
      luaRunner.handleEvent(null);
    } catch (Exception exception){
      LOGGER.error("Exception Error: ", exception);
    }
  }

  public static void scriptTranslateTest(CommandProcessor commandProcessor) throws FileNotFoundException, IOException
  {
    InputStream script = new FileInputStream(URLToolsLua.getFromClassPath("test/translate-test.lua"));
    
    LuaRunner luaRunner = new LuaRunner(commandProcessor);
    try {
      luaRunner.initPackageLib(Paths.get(URLTools.getFromClassPath("test", LuaRunner.class).toURI()));
      luaRunner.bootstrapLotro("translate-test.lua", script);
      luaRunner.handleEvent(null);
    } catch (Exception exception){
      LOGGER.error("Exception Error: ", exception);
    }
  }

  public static void scriptUiTest(CommandProcessor commandProcessor) throws FileNotFoundException, IOException
  {
    InputStream script = new FileInputStream(URLToolsLua.getFromClassPath("test/ui-test.lua"));
    
    LuaRunner luaRunner = new LuaRunner(commandProcessor);
    try {
      luaRunner.initPackageLib(Paths.get(URLTools.getFromClassPath("test", LuaRunner.class).toURI()));
      luaRunner.bootstrapLotro("ui-test.lua", script);
      luaRunner.handleEvent(null);
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
        CommandProcessor commandProcessor=new CommandProcessor();
        commandProcessor.install(UI.dragonConsole);
        try {
          //scriptTranslateTest();
          scriptUiTest(commandProcessor);
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });
  }
}
