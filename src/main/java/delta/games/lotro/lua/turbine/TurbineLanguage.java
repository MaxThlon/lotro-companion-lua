package delta.games.lotro.lua.turbine;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public enum TurbineLanguage {
	/**
	 * 
	 */
  INVALID("Invalid", Integer.valueOf(0)),
	/**
	 * 
	 */
  ENGLISH("English", Integer.valueOf(2)),
	/**
	 * 
	 */
  ENGLISHGB("EnglishGB", Integer.valueOf(268435457)),
	/**
   * 
   */
  FRENCH("French", Integer.valueOf(268435459)),
  /**
   * 
   */
  GERMAN("German", Integer.valueOf(268435460)),
  /**
   * 
   */
  RUSSIAN("Russian", Integer.valueOf(268435463));

  private static final Map<Integer, TurbineLanguage> BY_NUMBER = new HashMap<>();
  
  static {
    for (TurbineLanguage e:values()) {
      BY_NUMBER.put(e._value, e);
    }
  }

	private final String _label;
	private final Integer _value;

  private TurbineLanguage(String label, Integer value) {
  	_label = label;
  	_value = value;
  }
  
  /**
   * @param number .
   * @return a valid DataScope enum.
   */
  public static TurbineLanguage valueOfNumber(Integer number) {
    return BY_NUMBER.get(number);
  }
  
  /**
   * @return label.
   */
  public String getLabel() {
  	return _label;
  }
  
  /**
   * @return value.
   */
  public Integer getValue() {
  	return _value;
  }
  
  /**
   * Initialize lua turbine package
   * @param lua thread.
   * @param envIndex .
   */
  public static void openPackage(Lua lua, int envIndex) {
    Turbine.pushNamespace(lua, envIndex, "Turbine"); /* push namespace */
    
    /**
     * Create Language table.
     */
    lua.createTable(0, 0);
    for (TurbineLanguage turbineLanguage:TurbineLanguage.values()) {
      lua.push(turbineLanguage.getValue());
      lua.setField(-2, turbineLanguage.getLabel());
    }
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -2), "IsA", LuaObject::isA);
    lua.setField(-2, "Language");

    lua.pop(1); /* pop namespace */
  }

  /**
   * @param turbineLanguage .
   * @return a Locale or null.
   */
  public static Locale ToLocale(TurbineLanguage turbineLanguage) {
    switch (turbineLanguage) {
      case ENGLISH: return Locale.ENGLISH;
      case ENGLISHGB: return Locale.UK;
      case FRENCH: return Locale.FRENCH;
      case GERMAN: return Locale.GERMAN;
      case RUSSIAN: return new Locale("ru");
      case INVALID:
      default: return null;
    }
  }
}
