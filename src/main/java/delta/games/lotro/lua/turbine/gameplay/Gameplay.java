package delta.games.lotro.lua.turbine.gameplay;

import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.ValueFactory;

/**
 * Gameplay library for lua scripts.
 * @author MaxThlon
 */
public class Gameplay {
  public LuaTable _gameplay = ValueFactory.tableOf();
  private static LuaTable _activeSkill = ValueFactory.tableOf();
  
  public Gameplay() {
    _activeSkill.rawset("GetBaseCooldown", ActiveSkill.BaseCooldown);
    
    _gameplay.rawset("ActiveSkill", _activeSkill);
  }

  static class ActiveSkill {
    private static LuaValue BaseCooldown = ValueFactory.tableOf();    
  }
}

