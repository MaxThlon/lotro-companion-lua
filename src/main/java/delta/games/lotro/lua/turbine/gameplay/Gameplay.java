package delta.games.lotro.lua.turbine.gameplay;

import java.util.HashMap;

import delta.games.lotro.character.classes.WellKnownCharacterClassKeys;
import delta.games.lotro.character.classes.WellKnownMonsterClassKeys;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.races.RacesManager;
import delta.games.lotro.common.enums.CraftTier;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.lore.crafting.CraftingSystem;
import delta.games.lotro.lore.crafting.Vocation;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lua.turbine.gameplay.backpack.Backpack;
import delta.games.lotro.lua.turbine.gameplay.bank.Bank;
import delta.games.lotro.lua.turbine.gameplay.effect.Effect;
import delta.games.lotro.lua.turbine.gameplay.entity.Actor;
import delta.games.lotro.lua.turbine.gameplay.entity.Entity;
import delta.games.lotro.lua.turbine.gameplay.item.Item;
import delta.games.lotro.lua.turbine.gameplay.player.LocalPlayer;
import delta.games.lotro.lua.turbine.gameplay.player.Player;
import delta.games.lotro.lua.turbine.gameplay.skill.Skill;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;

/**
 * Gameplay library for lua scripts.
 * @author MaxThlon
 */
public class Gameplay {
  @SuppressWarnings("boxing")
  public static Lua.LuaError openPackage(Lua lua, int globalsIndex) {
  	Lua.LuaError error;
  	
  	LuaTools.pushfenv(
    		lua,
    		globalsIndex,
    		"Turbine.Gameplay",
    		"Turbine", "Gameplay"
    );
    LuaTools.pushModule(
    		lua,
    		LuaTools.relativizeIndex(globalsIndex, -1),
    		"Turbine", "Gameplay"
    );
    lua.push(new HashMap<String, Integer>() {{
        put("Undefined", 0);
        put("FreePeople", 1);
        put("MonsterPlayer", 2);
    }});
    lua.setField(-2, "Alignment");

    lua.push(new HashMap<String, Integer>() {{
        put("Undefined", 0);
        put(WellKnownCharacterClassKeys.GUARDIAN, 23);
        put(WellKnownCharacterClassKeys.CAPTAIN, 24);
        put(WellKnownCharacterClassKeys.MINSTREL, 31);
        put(WellKnownCharacterClassKeys.BURGLAR, 40);
        put(WellKnownMonsterClassKeys.WARLEADER, 52);
        put(WellKnownMonsterClassKeys.REAVER, 71);
        put(WellKnownMonsterClassKeys.STALKER, 126);
        put(WellKnownMonsterClassKeys.WEAVER, 127);
        put(WellKnownMonsterClassKeys.DEFILER, 128);
        put(WellKnownCharacterClassKeys.HUNTER, 162);
        put(WellKnownCharacterClassKeys.CHAMPION, 172);
        put(WellKnownMonsterClassKeys.BLACKARROW, 179);
        put(WellKnownCharacterClassKeys.LORE_MASTER, 185);
        put("Troll", 190);
        put("Ranger", 191);
        put("Chicken", 192);
        put(WellKnownCharacterClassKeys.RUNE_KEEPER, 193);
        put(WellKnownCharacterClassKeys.WARDEN, 194);
        put(WellKnownCharacterClassKeys.BEORNING, 214);
        put("Mariner", 216);
    }});
    lua.setField(-2, "Class");

    lua.push(new HashMap<String, Integer>() {{
      for (CraftTier craftTier:LotroEnumsRegistry.getInstance().get(CraftTier.class).getAll()) {
        put(craftTier.getLabel(), craftTier.getCode());
      }
    }});
    lua.setField(-2, "CraftTier");
   
    lua.push(new HashMap<String, Integer>() {{
        put("Undefined", 0);
        put("Disease", 2);
        put("Physical", 4);
        put("Wound", 8);
        put("Cry", 16);
        put("Song", 32);
        put("Fear", 64);
        put("Poison", 128);
        put("Elemental", 512);
        put("Corruption", 1024);
        put("Dispellable", 1226);
    }});
    lua.setField(-2, "EffectCategory");

    lua.push(new HashMap<String, Integer>() {{
      for (EquipmentLocation equipmentLocation:EquipmentLocation.getAll()) {
        put(equipmentLocation.getKey(), equipmentLocation.getCode());
      }
    }});
    lua.setField(-2, "Equipment");

    lua.push(new HashMap<String, Integer>() {{
        put("Undefined", 0);
        put("Metalsmith", 1);
        put("Forester", 2);
        put("Scholar", 3);
        put("Jeweller", 4);
        put("Tailor", 5);
        put("Weaponsmith", 6);
        put("Prospector", 7);
        put("Woodworker", 8);
        put("Farmer", 9);
        put("Cook", 10);
    }});
    lua.setField(-2, "Profession");

    lua.push(new HashMap<String, Integer>() {{
      for (RaceDescription raceDescription:RacesManager.getInstance().getAll()) {
        put(raceDescription.getKey(), raceDescription.getCode());
      }
    }});
    lua.setField(-2, "Race");

    lua.push(new HashMap<String, Integer>() {{
        put("Unset", 0);
        put("Ready", 1);
        put("NotReady", 2);
    }});
    lua.setField(-2, "ReadyState");

    lua.push(new HashMap<String, Integer>() {{
        put("Normal", 0);
        put("Mount", 1);
        put("Gambit", 2);
    }});
    lua.setField(-2, "SkillType");

    lua.push(new HashMap<String, Integer>() {{
      for(Vocation vocation : CraftingSystem.getInstance().getData().getVocationsRegistry().getAll()) {
        put(vocation.getKey(), vocation.getIdentifier());
      }
    }});
    lua.setField(-2, "Vocation");
    
    error = Entity.add(lua);
    if (error != Lua.LuaError.OK) return error;
    error = Item.add(lua);
    if (error != Lua.LuaError.OK) return error;
    error = Backpack.add(lua);
    if (error != Lua.LuaError.OK) return error;
    error = Bank.add(lua);
    if (error != Lua.LuaError.OK) return error;

    error = Skill.add(lua);
    if (error != Lua.LuaError.OK) return error;
    error = Effect.add(lua);
    if (error != Lua.LuaError.OK) return error;
    error = Actor.add(lua);
    if (error != Lua.LuaError.OK) return error;
    error = Player.add(lua);
    if (error != Lua.LuaError.OK) return error;
    error = LocalPlayer.add(lua);
    if (error != Lua.LuaError.OK) return error;

    lua.pop(2); /* pop env, module */
    return error;
  }
}

