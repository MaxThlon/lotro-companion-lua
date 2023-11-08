package delta.games.lotro.lua.ui.swing;

import java.awt.Component;

import javax.swing.SwingUtilities;

import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.turbine.ui.LuaControl;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public final class LuaComponent {
  /**
   * Initialize lua Component package
   * @param lua .
   * @param envIndex .
   * @param errfunc .
   * @return Lua.LuaError.
   */
	public static Lua.LuaError add(Lua lua, int envIndex, int errfunc) {
		Lua.LuaError error;
		
		Turbine.pushfenv(
    		lua,
    		envIndex,
    		"UI.Swing",
    		"UI", "Swing"
    );

  	if ((error = LuaTools.pushClass(
  			lua,
  			LuaTools.relativizeIndex(errfunc, -1),
  			"Turbine", "Object"
  	)) != Lua.LuaError.OK) return error;
    LuaTools.setFunction(lua, -1, -2, "SetName", LuaComponent::setName);
    lua.setField(-2, "Component");
    
  	lua.pop(1); /* pop env */
  	return error;
	}
	
	private static int setName(Lua lua) {
		Component component = LuaControl.findComponentFromLuaObject(lua, 1);
		String value = lua.toString(2);

		SwingUtilities.invokeLater(() -> component.setName(value));
		return 1;
	}
}
