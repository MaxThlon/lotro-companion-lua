package delta.games.lotro.lua;

import static org.assertj.swing.finder.WindowFinder.findFrame;

import java.nio.file.Paths;
import java.util.UUID;

import javax.swing.JDesktopPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import com.eleet.dragonconsole.CommandProcessor;
import com.eleet.dragonconsole.DragonConsole;

import delta.common.framework.console.ConsoleManager;
import delta.common.framework.module.ModuleEvent;
import delta.common.framework.module.ModuleExecutor;
import delta.common.framework.module.ModuleManager;
import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.JFrame;
import delta.games.lotro.client.plugin.Plugin;
import delta.games.lotro.client.plugin.Plugin.Configuration;
import delta.games.lotro.client.plugin.Plugin.Information;
import delta.games.lotro.lua.utils.URLToolsLua;

/**
 * Main test Lua.
 * @author MaxThlon
 */
public class MainTestLua extends AssertJSwingJUnitTestCase
{
  private static Logger LOGGER = Logger.getLogger(MainTestLua.class);
  
  public static JFrame _frame = null;
  public static JDesktopPane _jDesktopPane = null;
  public static DragonConsole _dragonConsole = null;
  
  public static Plugin buildPlugin(String packageName, String apartmentName) {
  	return new Plugin(
  			new Information("", "", "", "", ""),
  			packageName,
  			new Configuration(apartmentName)
  	);
  }

  public static void buildUI() {
    /*try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Throwable e) {
        e.printStackTrace();
    }*/
    //FlatLotroLaf.setup();
    /*
    
    _frame = GuiFactory.buildFrame();
    _frame.setTitle("Lotro companion lua");
    _frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    _frame.setPreferredSize(new Dimension(1024, 768));
    _jDesktopPane = new JDesktopPane();
    _frame.setContentPane(_jDesktopPane);
    _frame.pack();
    _frame.setVisible(true);*/
  }
  
  //private DefaultWindowController _defaultWindowController;
  //private FrameFixture _window;

  @Override
  protected void onSetUp() {
  	GuiFactory.init();
  	ConsoleManager.getInstance().activateByModuleLogger();
  	/*_defaultWindowController=new DefaultWindowController() {
      @Override
      protected JFrame build() {
        JFrame frame=super.build();
        frame.getContentPane().setLayout(null);
        return frame;
      }
      @Override
      protected JComponent buildContents() {
        return null;
      }
    };
    
    JFrame frame = GuiActionRunner.execute(() -> _defaultWindowController.getFrame());
    // IMPORTANT: note the call to 'robot()'
    // we must use the Robot from AssertJSwingJUnitTestCase
    _window = new FrameFixture(robot(), (java.awt.Frame)frame);
    _window.show();*/
  }
  
  public static void scriptSimpleTest(CommandProcessor commandProcessor) {
    LuaModule luaRunner = new LuaModule(
    		UUID.randomUUID(),
    		null,
    		new LuaLotro(),
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
    		new LuaLotro(),
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

  @Test
  public void scriptUiTest() {
  	UUID moduleUuid = UUID.randomUUID();
    LuaModule luaRunner = new LuaModule(
    		moduleUuid,
    		null,
    		new LuaLotro(),
    		URLToolsLua.getFromClassPath("", MainTestLua.class),
    		Paths.get("target", "classes", "delta", "games", "lotro", "lua")
    );
    ModuleManager.getInstance().addModule(luaRunner);
    luaRunner.handleEvent(new ModuleEvent(
    		ModuleExecutor.ExecutorEvent.LOAD,
    		moduleUuid,
    		ModuleExecutor.ExecutorEvent.LOAD.name(),
    		new Object[]{ LuaModule.LuaBootstrap.Lotro }
    ));

    luaRunner.handleEvent(new ModuleEvent(
    		ModuleExecutor.ExecutorEvent.EXECUTE,
    		moduleUuid,
    		"load",
    		new Object[]{ buildPlugin("Test.ui-test", "Test.ui-test") }
    ));
    
    /*luaRunner.handleEvent(new  ModuleEvent(
    		ModuleExecutor.ExecutorEvent.EXECUTE,
    		moduleUuid,
    		"debug",
    		null
    ));
    luaRunner.handleEvent(new  ModuleEvent(
    		ModuleExecutor.ExecutorEvent.EXECUTE,
    		moduleUuid,
    		ModuleExecutor.ExecutorEvent.EXECUTE.name(),
    		null
    ));*/
    FrameFixture debugWindow_Frame = findFrame("debugWindow").withTimeout(10000).using(robot());
    debugWindow_Frame.requireTitle("Debug Console");
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
          //scriptUiTest(commandProcessor);
        //} catch (IOException e) {
          // TODO Auto-generated catch block
          //LOGGER.erro(e);
        //}
      }
    });
  }
}
