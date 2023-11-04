package delta.games.lotro.lua;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import delta.games.lotro.client.plugin.Plugin;
import delta.games.lotro.lua.turbine.Apartment;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;
import party.iroiro.luajava.value.LuaValue;

/**
 * @author MaxThlon
 */
public class LuaLotro implements LuaModuleImpl {
	private Map<String, Apartment> _apartments;
	private Map<String, LuaValue> _shellCommands;
	
	public LuaLotro() {
  	_apartments = new HashMap<String, Apartment>();
  	_shellCommands = new HashMap<String, LuaValue>();
  }
	
	public Apartment getApartment(Plugin plugin) {
		return _apartments.get(
				((plugin._configuration != null) && (plugin._configuration._apartment != null))?
						plugin._configuration._apartment:null
		);
	}
	
	public Apartment pushApartmentThread(Lua lua, Plugin plugin) {
		Apartment apartment = getApartment(plugin);
		
		if (apartment == null) {
			apartment = new Apartment(lua.newThread());
			_apartments.put(
					((plugin._configuration != null) && (plugin._configuration._apartment != null))?
							plugin._configuration._apartment:null,
					apartment
			);
		} else {
			lua.push(apartment.getThread(), Conversion.NONE);
		}

		return apartment;
	}
	
	public Set<String> getCommands() {
		return _shellCommands.keySet();
	}
	
	public void addCommand(String name, LuaValue luaCommand) {
		_shellCommands.put(name, luaCommand);
	}
	
	public void removeCommand(String name) {
		_shellCommands.remove(name);
	}
	
	public LuaValue findCommand(String name) {
		return _shellCommands.get(name);
	}
}
