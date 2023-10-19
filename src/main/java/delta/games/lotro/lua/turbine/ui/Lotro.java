package delta.games.lotro.lua.turbine.ui;

import static org.squiddev.cobalt.ValueFactory.tableOf;
import static org.squiddev.cobalt.ValueFactory.valueOf;

import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.UnwindThrowable;

/**
 * Lotro library for lua scripts.
 * @author MaxThlon
 */
public abstract class Lotro {

  public static void add(LuaState state) throws LuaError, UnwindThrowable {
    LuaTable globals = state.getMainThread().getfenv();
    LuaTable font = tableOf(
        valueOf("Verdana12"), valueOf("Verdana12"),
        valueOf("BookAntiqua12"), valueOf("BookAntiqua12"),
        valueOf("BookAntiqua14"), valueOf("BookAntiqua14"),
        valueOf("BookAntiqua16"), valueOf("BookAntiqua16"),
        valueOf("BookAntiqua18"), valueOf("BookAntiqua18"),
        valueOf("BookAntiqua20"), valueOf("BookAntiqua20"),
        valueOf("BookAntiqua22"), valueOf("BookAntiqua22"),
        valueOf("BookAntiqua24"), valueOf("BookAntiqua24"),
        
        valueOf("BookAntiquaBold12"), valueOf("BookAntiquaBold12"),
        valueOf("BookAntiquaBold14"), valueOf("BookAntiquaBold14"),
        valueOf("BookAntiquaBold18"), valueOf("BookAntiquaBold18"),
        valueOf("BookAntiquaBold19"), valueOf("BookAntiquaBold19"),
        valueOf("BookAntiquaBold22"), valueOf("BookAntiquaBold22"),
        valueOf("BookAntiquaBold24"), valueOf("BookAntiquaBold24"),
        valueOf("BookAntiquaBold26"), valueOf("BookAntiquaBold26"),
        valueOf("BookAntiquaBold28"), valueOf("BookAntiquaBold28"),
        valueOf("BookAntiquaBold32"), valueOf("BookAntiquaBold32"),
        valueOf("BookAntiquaBold36"), valueOf("BookAntiquaBold36"),
        
        valueOf("FixedSys15"), valueOf("FixedSys15"),
        
        valueOf("LucidaConsole12"), valueOf("LucidaConsole12"),
        
        valueOf("TrajanPro13"), valueOf("TrajanPro13"),
        valueOf("TrajanPro14"), valueOf("TrajanPro14"),
        valueOf("TrajanPro15"), valueOf("TrajanPro15"),
        valueOf("TrajanPro16"), valueOf("TrajanPro16"),
        valueOf("TrajanPro18"), valueOf("TrajanPro18"),
        valueOf("TrajanPro19"), valueOf("TrajanPro19"),
        valueOf("TrajanPro20"), valueOf("TrajanPro20"),
        valueOf("TrajanPro21"), valueOf("TrajanPro21"),
        valueOf("TrajanPro23"), valueOf("TrajanPro23"),
        valueOf("TrajanPro24"), valueOf("TrajanPro24"),
        valueOf("TrajanPro25"), valueOf("TrajanPro25"),
        valueOf("TrajanPro26"), valueOf("TrajanPro26"),
        valueOf("TrajanPro28"), valueOf("TrajanPro28"),
        
        valueOf("TrajanProBold16"), valueOf("TrajanProBold16"),
        valueOf("TrajanProBold22"), valueOf("TrajanProBold22"),
        valueOf("TrajanProBold24"), valueOf("TrajanProBold24"),
        valueOf("TrajanProBold25"), valueOf("TrajanProBold25"),
        valueOf("TrajanProBold30"), valueOf("TrajanProBold30"),
        valueOf("TrajanProBold36"), valueOf("TrajanProBold36"),
        
        valueOf("Undefined"), valueOf("Undefined"),
        
        valueOf("Verdana10"), valueOf("Verdana10"),
        valueOf("Verdana12"), valueOf("Verdana12"),
        valueOf("Verdana14"), valueOf("Verdana14"),
        valueOf("Verdana16"), valueOf("Verdana16"),
        valueOf("Verdana18"), valueOf("Verdana18"),
        valueOf("Verdana20"), valueOf("Verdana20"),
        valueOf("Verdana22"), valueOf("Verdana22"),
        valueOf("Verdana23"), valueOf("Verdana23"),
        valueOf("VerdanaBold16"), valueOf("VerdanaBold16")
    );
    
    LuaTable uiMetatable = globals.rawget("Turbine").checkTable().rawget("UI").checkTable();
    LuaTable luaControlClass = uiMetatable.rawget("Control").checkTable();
    LuaTable uiLotroMetatable = tableOf(
        valueOf("Font"), font,
        valueOf("TextBox"), uiMetatable.rawget("TextBox"),
        valueOf("Button"), uiMetatable.rawget("Button"),
        valueOf("ScrollBar"), uiMetatable.rawget("ScrollBar")
    );
    
    LuaWindow.add(state, uiLotroMetatable, luaControlClass);
    
    uiMetatable.rawset("Lotro", uiLotroMetatable);
  }
}