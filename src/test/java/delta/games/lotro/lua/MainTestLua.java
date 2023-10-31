package delta.games.lotro.lua;

import java.awt.Dimension;
import java.nio.file.Paths;
import java.util.UUID;

import javax.swing.JDesktopPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import com.eleet.dragonconsole.CommandProcessor;
import com.eleet.dragonconsole.DragonConsole;

import delta.common.framework.module.ModuleEvent;
import delta.common.framework.module.ModuleExecutor;
import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.JFrame;
import delta.games.lotro.lua.utils.LuaTools;
import delta.games.lotro.lua.utils.URLToolsLua;


/**
 * Main test Lua.
 * @author MaxThlon
 */
public class MainTestLua
{
  private static Logger LOGGER = Logger.getLogger(MainTestLua.class);
  
  public static JFrame _frame = null;
  public static JDesktopPane _jDesktopPane = null;
  public static DragonConsole _dragonConsole = null;
  
  public static void buildUI() {
    /*try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Throwable e) {
        e.printStackTrace();
    }*/
    //FlatLotroLaf.setup();
    GuiFactory.init();
    
    _frame = GuiFactory.buildFrame();
    _frame.setTitle("Lotro companion lua");
    _frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    _frame.setPreferredSize(new Dimension(1024, 768));
    _jDesktopPane = new JDesktopPane();
    _frame.setContentPane(_jDesktopPane);
    _frame.pack();
    _frame.setVisible(true);
  }
  
  public static void scriptSimpleTest(CommandProcessor commandProcessor) {
    LuaModule luaRunner = new LuaModule(
    		UUID.randomUUID(),
    		null,
    		URLToolsLua.getFromClassPath("", MainTestLua.class)
    );
    try {
    	/*error = luaRunner.bootstrapLotro(
          LuaTools.loadBuffer("simple.lua", MainTestLua.class),
          "simple"
      );
      if (error != Lua.LuaError.OK) LuaTools.throwLuaError(luaRunner.getLua());*/
      luaRunner.handleEvent(null);
    } catch (Exception exception){
      LOGGER.error("Exception Error: ", exception);
    }
  }

  public static void scriptTranslateTest(CommandProcessor commandProcessor) {
    LuaModule luaRunner = new LuaModule(
    		UUID.randomUUID(),
    		null,
    		URLToolsLua.getFromClassPath("", MainTestLua.class)
    );
    try {
    	/*error = luaRunner.bootstrapLotro(
          LuaTools.loadBuffer("translate-test", MainTestLua.class),
          "translate-test"
      );*/
      luaRunner.handleEvent(null);
    } catch (Exception exception){
      LOGGER.error("Exception Error: ", exception);
    }
  }

  public static void scriptUiTest(CommandProcessor commandProcessor) {
  	UUID moduleUuid = UUID.randomUUID();
    LuaModule luaRunner = new LuaModule(
    		moduleUuid,
    		null,
    		URLToolsLua.getFromClassPath("", MainTestLua.class),
    		Paths.get("target", "classes", "delta", "games", "lotro", "lua")
    );
    luaRunner.handleEvent(new  ModuleEvent(
    		ModuleExecutor.ExecutorEvent.LOAD,
    		moduleUuid,
    		ModuleExecutor.ExecutorEvent.LOAD.name(),
    		new Object[]{ 
    				LuaModule.LuaBootstrap.Lotro,
    				LuaTools.loadBuffer(Paths.get("Test", "ui-test.lua"), MainTestLua.class),
    				"Test.ui-test"
    		}
    ));
    luaRunner.handleEvent(new  ModuleEvent(
    		ModuleExecutor.ExecutorEvent.EXECUTE,
    		moduleUuid,
    		ModuleExecutor.ExecutorEvent.EXECUTE.name(),
    		null
    ));
  }

  /**
   * main for UI tests.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    //scriptSimpleTest(null);

    SwingUtilities.invokeLater(new Runnable() {
    	@Override
      public void run() {
        buildUI();
        CommandProcessor commandProcessor=new CommandProcessor();
        commandProcessor.install(_dragonConsole);
        //try {
          //scriptTranslateTest();
          scriptUiTest(commandProcessor);
        //} catch (IOException e) {
          // TODO Auto-generated catch block
          //LOGGER.erro(e);
        //}
      }
    });
  }
}
