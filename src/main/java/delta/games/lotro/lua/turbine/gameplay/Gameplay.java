package delta.games.lotro.lua.turbine.gameplay;

import static org.squiddev.cobalt.ValueFactory.tableOf;
import static org.squiddev.cobalt.ValueFactory.valueOf;

import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.LuaFunction;

import delta.games.lotro.character.classes.WellKnownCharacterClassKeys;
import delta.games.lotro.character.classes.WellKnownMonsterClassKeys;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.races.RacesManager;
import delta.games.lotro.common.enums.CraftTier;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.lore.crafting.CraftingSystem;
import delta.games.lotro.lore.crafting.Vocation;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.turbine.gameplay.backpack.Backpack;
import delta.games.lotro.lua.turbine.gameplay.bank.Bank;
import delta.games.lotro.lua.turbine.gameplay.effect.Effect;
import delta.games.lotro.lua.turbine.gameplay.entity.Actor;
import delta.games.lotro.lua.turbine.gameplay.entity.Entity;
import delta.games.lotro.lua.turbine.gameplay.item.Item;
import delta.games.lotro.lua.turbine.gameplay.party.Party;
import delta.games.lotro.lua.turbine.gameplay.player.LocalPlayer;
import delta.games.lotro.lua.turbine.gameplay.player.Player;
import delta.games.lotro.lua.turbine.gameplay.skill.Skill;

/**
 * Gameplay library for lua scripts.
 * @author MaxThlon
 */
public class Gameplay {
  public static void add(LuaState state, LuaTable turbineEnv,
                         LuaFunction luaClass, LuaValue luaObjectClass) throws LuaError, UnwindThrowable {
    LuaTable gameplayEnv = Turbine.generatefenv(state, turbineEnv, "Gameplay");
    gameplayEnv.rawset("Alignment", tableOf(
        valueOf("Undefined"), valueOf(0),
        valueOf("FreePeople"), valueOf(1),
        valueOf("MonsterPlayer"), valueOf(2)
    ));
    
    gameplayEnv.rawset("Class", tableOf(
        valueOf("Undefined"), valueOf(0),
        valueOf(WellKnownCharacterClassKeys.GUARDIAN), valueOf(23),
        valueOf(WellKnownCharacterClassKeys.CAPTAIN), valueOf(24),
        valueOf(WellKnownCharacterClassKeys.MINSTREL), valueOf(31),
        valueOf(WellKnownCharacterClassKeys.BURGLAR), valueOf(40),
        valueOf(WellKnownMonsterClassKeys.WARLEADER), valueOf(52),
        valueOf(WellKnownMonsterClassKeys.REAVER), valueOf(71),
        valueOf(WellKnownMonsterClassKeys.STALKER), valueOf(126),
        valueOf(WellKnownMonsterClassKeys.WEAVER), valueOf(127),
        valueOf(WellKnownMonsterClassKeys.DEFILER), valueOf(128),
        valueOf(WellKnownCharacterClassKeys.HUNTER), valueOf(162),
        valueOf(WellKnownCharacterClassKeys.CHAMPION), valueOf(172),
        valueOf(WellKnownMonsterClassKeys.BLACKARROW), valueOf(179),
        valueOf(WellKnownCharacterClassKeys.LORE_MASTER), valueOf(185),
        valueOf("Troll"), valueOf(190),
        valueOf("Ranger"), valueOf(191),
        valueOf("Chicken"), valueOf(192),
        valueOf(WellKnownCharacterClassKeys.RUNE_KEEPER), valueOf(193),
        valueOf(WellKnownCharacterClassKeys.WARDEN), valueOf(194),
        valueOf(WellKnownCharacterClassKeys.BEORNING), valueOf(214),
        valueOf("Mariner"), valueOf(216)
    ));
    
    LuaTable craftTierTable = new LuaTable();
    for (CraftTier craftTier:LotroEnumsRegistry.getInstance().get(CraftTier.class).getAll()) {
      craftTierTable.rawset(valueOf(craftTier.getLabel()), valueOf(craftTier.getCode()));
    }
    gameplayEnv.rawset("CraftTier", craftTierTable);
    
    
    gameplayEnv.rawset("EffectCategory", tableOf(
        valueOf("Undefined"), valueOf(0),
        valueOf("Disease"), valueOf(2),
        valueOf("Physical"), valueOf(4),
        valueOf("Wound"), valueOf(8),
        valueOf("Cry"), valueOf(16),
        valueOf("Song"), valueOf(32),
        valueOf("Fear"), valueOf(64),
        valueOf("Poison"), valueOf(128),
        valueOf("Elemental"), valueOf(512),
        valueOf("Corruption"), valueOf(1024),
        valueOf("Dispellable"), valueOf(1226)
    ));
    
    LuaTable equipmentLocationTable = new LuaTable();
    for (EquipmentLocation equipmentLocation:EquipmentLocation.getAll()) {
      equipmentLocationTable.rawset(valueOf(equipmentLocation.getKey()), valueOf(equipmentLocation.getCode()));
    }
    gameplayEnv.rawset("Equipment", equipmentLocationTable);
    
    gameplayEnv.rawset("Profession", tableOf(
        valueOf("Undefined"), valueOf(0),
        valueOf("Metalsmith"), valueOf(1),
        valueOf("Forester"), valueOf(2),
        valueOf("Scholar"), valueOf(3),
        valueOf("Jeweller"), valueOf(4),
        valueOf("Tailor"), valueOf(5),
        valueOf("Weaponsmith"), valueOf(6),
        valueOf("Prospector"), valueOf(7),
        valueOf("Woodworker"), valueOf(8),
        valueOf("Farmer"), valueOf(9),
        valueOf("Cook"), valueOf(10)
    ));
    
    
    LuaTable raceTable = new LuaTable();
    for (RaceDescription raceDescription:RacesManager.getInstance().getAll()) {
      raceTable.rawset(valueOf(raceDescription.getKey()), valueOf(raceDescription.getCode()));
    }
    gameplayEnv.rawset("Race", raceTable);
    
    gameplayEnv.rawset("ReadyState", tableOf(
        valueOf("Unset"), valueOf(0),
        valueOf("Ready"), valueOf(1),
        valueOf("NotReady"), valueOf(2)
    ));
    
    gameplayEnv.rawset("SkillType", tableOf(
        valueOf("Normal"), valueOf(0),
        valueOf("Mount"), valueOf(1),
        valueOf("Gambit"), valueOf(2)
    ));
    
    LuaTable vocationTable = new LuaTable();
    for(Vocation vocation : CraftingSystem.getInstance().getData().getVocationsRegistry().getAll()) {
      vocationTable.rawset(valueOf(vocation.getKey()), valueOf(vocation.getIdentifier()));
    }
    gameplayEnv.rawset("Vocation", vocationTable);
    
    LuaTable luaEntityClass = Entity.add(state, gameplayEnv, luaClass, luaObjectClass);
    Item.add(state, gameplayEnv, luaClass, luaEntityClass);
    Backpack.add(state, gameplayEnv, luaClass, luaObjectClass);
    Bank.add(state, gameplayEnv, luaClass, luaObjectClass);

    Skill.add(state, gameplayEnv);
    Effect.add(state, gameplayEnv, luaClass, luaObjectClass);
    LuaTable luaActorClass = Actor.add(state, gameplayEnv, luaClass, luaEntityClass);
    LuaTable luaPlayerClass = Player.add(state, gameplayEnv, luaClass, luaActorClass);
    LocalPlayer.add(state, gameplayEnv, luaClass, luaPlayerClass);
    Party.add(state, gameplayEnv);
  }
}

