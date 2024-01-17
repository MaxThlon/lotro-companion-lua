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
   */
	public static void add(Lua lua, int envIndex) {
		Turbine.pushfenv(
    		lua,
    		envIndex,
    		"UI.Swing",
    		"UI", "Swing"
    );

  	LuaTools.pushClass(lua, "Turbine", "Object");
    LuaTools.setFunction(lua, -1, -2, "SetName", LuaComponent::setName);
    lua.setField(-2, "Component");
    
  	lua.pop(1); /* pop env */
	}
	
	private static int setName(Lua lua) {
		Component component = LuaControl.findComponentFromLuaObject(lua, 1);
		String value = lua.toString(2);

		SwingUtilities.invokeLater(() -> component.setName(value));
		return 1;
	}
}
