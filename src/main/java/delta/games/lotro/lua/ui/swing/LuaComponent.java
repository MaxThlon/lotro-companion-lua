package delta.games.lotro.lua.ui.swing;

import java.awt.Component;

import javax.swing.SwingUtilities;

import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.turbine.ui.LuaControl;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class LuaComponent {
	public static Lua.LuaError add(Lua lua, int globalsIndex) {
		Lua.LuaError error;
		
		Turbine.pushfenv(
    		lua,
    		globalsIndex,
    		"UI.Swing",
    		"UI", "Swing"
    );
  	Turbine.pushModule(
  			lua,
  			LuaTools.relativizeIndex(globalsIndex, -1),
  			"UI", "Swing"
  	);

  	if ((error = LuaObject.callInherit(lua, -3, "Turbine", "Object")) != Lua.LuaError.OK) return error;
    LuaTools.setFunction(lua, -1, -3, "SetName", LuaComponent::setName);
    lua.setField(-2, "Component");
    
  	lua.pop(2); /* pop env, module */
  	return error;
	}
	
	private static int setName(Lua lua) {
		Component component = LuaControl.findComponentFromLuaObject(lua, 1);
		String value = lua.toString(2);

		SwingUtilities.invokeLater(() -> component.setName(value));
		return 1;
	}
}
