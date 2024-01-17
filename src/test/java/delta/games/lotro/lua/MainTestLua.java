package delta.games.lotro.lua;

import static org.assertj.swing.finder.WindowFinder.findFrame;

import java.nio.file.Paths;
import java.util.HashMap;
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
import delta.common.framework.lua.LuaModule;
import delta.common.framework.lua.command.LuaModuleCommandPath;
import delta.common.framework.lua.command.LuaModuleThreadCommand;
import delta.common.framework.module.ModuleManager;
import delta.common.framework.module.command.ModuleCommand;
import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.JFrame;
import delta.games.lotro.client.plugin.Plugin;
import delta.games.lotro.client.plugin.Plugin.Information;
import delta.games.lotro.lua.command.LotroLMCNewScriptState;
import delta.games.lotro.lua.command.LuaMTCRequireScriptFile;
import delta.games.lotro.lua.utils.URLToolsLua;

/**
 * Main test Lua.
 * @author MaxThlon
 */
public class MainTestLua extends AssertJSwingJUnitTestCase
{
  private static Logger LOGGER = Logger.getLogger(MainTestLua.class);
  
  /**
   * 
   */
  public static JFrame _frame = null;
  /**
   * 
   */
  public static JDesktopPane _jDesktopPane = null;
  /**
   * 
   */
  public static DragonConsole _dragonConsole = null;
  
  /**
   * @param packageName
   * @param apartmentName
   * @return a plugin.
   */
  public static Plugin buildPlugin(String packageName, String apartmentName) {
  	return new Plugin(
  			new Information("", "", "", "", ""),
  			packageName,
  			new HashMap<String, String>() {{ put("apartmentName", apartmentName); }}
  	);
  }

  /**
   * 
   */
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
  
  /**
   * @param commandProcessor
   */
  public static void scriptSimpleTest(CommandProcessor commandProcessor) {
    LuaModule luaModule = new LotroLuaModule(UUID.randomUUID());
    try {
    	/*error = luaModule.bootstrapLotro(
          LuaTools.loadBuffer("simple.lua", MainTestLua.class),
          "simple"
      );
      if (error != Lua.LuaError.OK) LuaTools.throwLuaError(luaModule.getLua());*/
      luaModule.execute(null);
    } catch (Exception exception){
      LOGGER.error("Exception Error: ", exception);
    }
  }

  /**
   * @param commandProcessor
   */
  public static void scriptTranslateTest(CommandProcessor commandProcessor) {
    LuaModule luaModule = new LotroLuaModule(UUID.randomUUID());
    try {
    	/*error = luaModule.bootstrapLotro(
          LuaTools.loadBuffer("translate-test", MainTestLua.class),
          "translate-test"
      );*/
      luaModule.execute(null);
    } catch (Exception exception){
      LOGGER.error("Exception Error: ", exception);
    }
  }

  /**
   * 
   */
  @Test
  public void scriptUiTest() {
  	UUID moduleUuid = UUID.randomUUID();
    LuaModule luaModule = new LotroLuaModule(moduleUuid);
    ModuleManager.getInstance().addModule(luaModule);
    luaModule.load(new ModuleCommand[] {
    		new LuaModuleCommandPath(
    				URLToolsLua.getFromClassPath("", MainTestLua.class),
    				Paths.get("target", "classes", "delta", "games", "lotro", "lua")
    		)
    		//new LuaModuleCommandCaracterFile(characterFile)
    });

    //Plugin plugin = buildPlugin("Test.ui-test", "Test.ui-test");
    UUID threadUuid = UUID.randomUUID();
    luaModule.execute(new LotroLMCNewScriptState(threadUuid, ""));
    
    /*luaModule.handleEvent(new  ModuleEvent(
    		ModuleExecutor.ExecutorEvent.EXECUTE,
    		moduleUuid,
    		"debug",
    		null
    ));
    luaModule.handleEvent(new  ModuleEvent(
    		ModuleExecutor.ExecutorEvent.EXECUTE,
    		moduleUuid,
    		ModuleExecutor.ExecutorEvent.EXECUTE.name(),
    		null
    ));*/
    FrameFixture debugWindow_Frame = findFrame("debugWindow").withTimeout(100009999).using(robot());
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
    		GuiFactory.init();
      	ConsoleManager.getInstance().activateByModuleLogger();
      	
        try {
        	UUID moduleUuid = UUID.randomUUID();
          LuaModule luaModule = new LotroLuaModule(moduleUuid);
          ModuleManager.getInstance().addModule(luaModule);
          luaModule.load(new ModuleCommand[] {
          		new LuaModuleCommandPath(
          				URLToolsLua.getFromClassPath("", MainTestLua.class).resolve("Test"),
          				Paths.get("target", "classes", "delta", "games", "lotro", "lua"),
          				Paths.get("target", "classes", "delta", "games", "lotro", "lua", "translate")
          		)
          		//new LuaModuleCommandCaracterFile(characterFile)
          });

          //Plugin plugin = buildPlugin("Test.ui-test", "Test.ui-test");
          UUID threadUuid = UUID.randomUUID();
          //luaModule.execute(new LotroLMCNewScriptState(threadUuid, ""));
          luaModule.execute(new LuaModuleThreadCommand(LuaModule.Command.NEW_THREAD, threadUuid));
          
          luaModule.execute(new LuaMTCRequireScriptFile(threadUuid, "translate-test"));
        } catch (Exception e) {
          LOGGER.error(e);
        }
      }
    });
  }
}
