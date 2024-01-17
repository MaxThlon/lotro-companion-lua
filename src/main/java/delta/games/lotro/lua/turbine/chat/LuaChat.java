package delta.games.lotro.lua.turbine.chat;

import java.util.HashMap;

import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * Chat library for lua scripts.
 * 
 * @author MaxThlon
 */
public final class LuaChat {
  /**
   * Initialize lua Chat package
   * 
   * @param lua .
   * @param envIndex .
   * @param namespaceIndex .
   */
  public static void add(Lua lua, int envIndex, int namespaceIndex) {
    /**
     * Create ChatType table.
     */
    lua.push(new HashMap<String, Integer>() {{
        put("Undef", Integer.valueOf(0));
        put("Error", Integer.valueOf(1));
        put("Admin", Integer.valueOf(3));
        put("Standard", Integer.valueOf(4));
        put("Say", Integer.valueOf(5));
        put("Tell", Integer.valueOf(6));
        put("Emote", Integer.valueOf(7));
        put("Fellowship", Integer.valueOf(11));
        put("Kinship", Integer.valueOf(12));
        put("Officer", Integer.valueOf(13));
        put("Advancement", Integer.valueOf(14));
        put("Trade", Integer.valueOf(15));
        put("LFF", Integer.valueOf(16));
        //put("Advice", Integer.valueOf(17));
        put("OOC", Integer.valueOf(18));
        put("Regional", Integer.valueOf(19));
        put("Death", Integer.valueOf(20));
        put("Quest", Integer.valueOf(21));
        put("Raid", Integer.valueOf(23));
        put("Unfiltered", Integer.valueOf(25));
        put("Roleplay", Integer.valueOf(27));
        put("UserChat1", Integer.valueOf(28));
        put("UserChat2", Integer.valueOf(29));
        put("UserChat3", Integer.valueOf(30));
        put("UserChat4", Integer.valueOf(31));
        put("Tribe", Integer.valueOf(32));
        put("PlayerCombat", Integer.valueOf(34));
        put("EnemyCombat", Integer.valueOf(35));
        put("SelfLoot", Integer.valueOf(36));
        put("FellowLoot", Integer.valueOf(37));
        put("World", Integer.valueOf(38));
        put("UserChat5", Integer.valueOf(39));
        put("UserChat6", Integer.valueOf(40));
        put("UserChat7", Integer.valueOf(41));
        put("UserChat8", Integer.valueOf(42));
    }}, Lua.Conversion.FULL);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsA", LuaObject::isA);
    lua.setField(LuaTools.relativizeIndex(namespaceIndex, -1), "ChatType");

    LuaTools.pushClass(lua);
    lua.setField(LuaTools.relativizeIndex(namespaceIndex, -1), "Chat");
  }
}
