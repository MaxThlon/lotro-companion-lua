package delta.games.lotro.lua.turbine.plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MaxThlon
 */
public enum DataScope {
	ACCOUNT("Account", Integer.valueOf(1)),
	SERVER("Server", Integer.valueOf(2)),
	CHARACTER("Character", Integer.valueOf(3));

  private static final Map<Integer, DataScope> BY_NUMBER = new HashMap<>();
  
  static {
    for (DataScope e:values()) {
      BY_NUMBER.put(e._value, e);
    }
  }

	public final String _label;
	public final Integer _value;

  private DataScope(String label, Integer value) {
  	this._label = label;
  	this._value = value;
  }
  
  public static DataScope valueOfNumber(Integer number) {
    return BY_NUMBER.get(number);
}
}
