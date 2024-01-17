package delta.games.lotro.lua.turbine;

import java.util.UUID;

import delta.common.framework.lua.LuaThreadState;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class LotroLuaScriptState extends LuaThreadState{
	String[] _namespace;

  /**
   * @param threadUuid .
   * @param thread .
   * @param namespace .
   */
  public LotroLuaScriptState(UUID threadUuid, Lua thread, String[] namespace) {
  	super(threadUuid, thread);
  	_namespace = namespace;
  }

  /**
   * @return namespace .
   */
  public String[] getNamespace() {
    return _namespace;
  }
}
