package delta.games.lotro.lua.turbine.ui;

import static org.squiddev.cobalt.ValueFactory.tableOf;
import static org.squiddev.cobalt.ValueFactory.valueOf;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaBoolean;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.games.lotro.lua.turbine.plugin.LuaPlugin;

/**
 * Lotro library for lua scripts.
 * @author MaxThlon
 */
public abstract class Lotro {

  public static void add(LuaState state, LuaTable uiLotroEnv) throws LuaError, UnwindThrowable {
    LuaTable env = state.getMainThread().getfenv();
    LuaTable font = tableOf(
        valueOf("Verdana12"), valueOf("Verdana12"),
        valueOf("BookAntiqua12"), valueOf("BookAntiqua12"),
        valueOf("BookAntiqua14"), valueOf("BookAntiqua14"),
        valueOf("BookAntiqua16"), valueOf("BookAntiqua16"),
        valueOf("BookAntiqua18"), valueOf("BookAntiqua18"),
        valueOf("BookAntiqua20"), valueOf("BookAntiqua20"),
        valueOf("BookAntiqua22"), valueOf("BookAntiqua22"),
        valueOf("BookAntiqua24"), valueOf("BookAntiqua24"),
        
        valueOf("BookAntiquaBold12"), valueOf("BookAntiquaBold12"),
        valueOf("BookAntiquaBold14"), valueOf("BookAntiquaBold14"),
        valueOf("BookAntiquaBold18"), valueOf("BookAntiquaBold18"),
        valueOf("BookAntiquaBold19"), valueOf("BookAntiquaBold19"),
        valueOf("BookAntiquaBold22"), valueOf("BookAntiquaBold22"),
        valueOf("BookAntiquaBold24"), valueOf("BookAntiquaBold24"),
        valueOf("BookAntiquaBold26"), valueOf("BookAntiquaBold26"),
        valueOf("BookAntiquaBold28"), valueOf("BookAntiquaBold28"),
        valueOf("BookAntiquaBold32"), valueOf("BookAntiquaBold32"),
        valueOf("BookAntiquaBold36"), valueOf("BookAntiquaBold36"),
        
        valueOf("FixedSys15"), valueOf("FixedSys15"),
        
        valueOf("LucidaConsole12"), valueOf("LucidaConsole12"),
        
        valueOf("TrajanPro13"), valueOf("TrajanPro13"),
        valueOf("TrajanPro14"), valueOf("TrajanPro14"),
        valueOf("TrajanPro15"), valueOf("TrajanPro15"),
        valueOf("TrajanPro16"), valueOf("TrajanPro16"),
        valueOf("TrajanPro18"), valueOf("TrajanPro18"),
        valueOf("TrajanPro19"), valueOf("TrajanPro19"),
        valueOf("TrajanPro20"), valueOf("TrajanPro20"),
        valueOf("TrajanPro21"), valueOf("TrajanPro21"),
        valueOf("TrajanPro23"), valueOf("TrajanPro23"),
        valueOf("TrajanPro24"), valueOf("TrajanPro24"),
        valueOf("TrajanPro25"), valueOf("TrajanPro25"),
        valueOf("TrajanPro26"), valueOf("TrajanPro26"),
        valueOf("TrajanPro28"), valueOf("TrajanPro28"),
        
        valueOf("TrajanProBold16"), valueOf("TrajanProBold16"),
        valueOf("TrajanProBold22"), valueOf("TrajanProBold22"),
        valueOf("TrajanProBold24"), valueOf("TrajanProBold24"),
        valueOf("TrajanProBold25"), valueOf("TrajanProBold25"),
        valueOf("TrajanProBold30"), valueOf("TrajanProBold30"),
        valueOf("TrajanProBold36"), valueOf("TrajanProBold36"),
        
        valueOf("Undefined"), valueOf("Undefined"),
        
        valueOf("Verdana10"), valueOf("Verdana10"),
        valueOf("Verdana12"), valueOf("Verdana12"),
        valueOf("Verdana14"), valueOf("Verdana14"),
        valueOf("Verdana16"), valueOf("Verdana16"),
        valueOf("Verdana18"), valueOf("Verdana18"),
        valueOf("Verdana20"), valueOf("Verdana20"),
        valueOf("Verdana22"), valueOf("Verdana22"),
        valueOf("Verdana23"), valueOf("Verdana23"),
        valueOf("VerdanaBold16"), valueOf("VerdanaBold16")
    );

    uiLotroEnv.rawset("LotroUIElement", tableOf(
        valueOf("Backpack1"), valueOf(1),
        valueOf("Backpack2"), valueOf(2),
        valueOf("Backpack3"), valueOf(3),
        valueOf("Backpack4"), valueOf(4),
        valueOf("Backpack5"), valueOf(5),
        valueOf("Backpack6"), valueOf(6),
        valueOf("Party"), valueOf(7),
        valueOf("PluginsManager"), valueOf(8),
        valueOf("Target"), valueOf(10),
        valueOf("Vitals"), valueOf(11)
    ));

    uiLotroEnv.rawset("Action", tableOf(
        valueOf("QuickslotPageDown"), valueOf(1),
        valueOf("QuickslotPageUp"), valueOf(1),
        valueOf("Quickslot_1"), valueOf(1),
        valueOf("Quickslot_2"), valueOf(1),
        valueOf("Quickslot_3"), valueOf(1),
        valueOf("Quickslot_4"), valueOf(1),
        valueOf("Quickslot_5"), valueOf(1),
        valueOf("Quickslot_6"), valueOf(1),
        valueOf("Quickslot_7"), valueOf(1),
        valueOf("Quickslot_8"), valueOf(1),
        valueOf("Quickslot_9"), valueOf(1),
        valueOf("Quickslot_10"), valueOf(1),
        valueOf("Quickslot_11"), valueOf(1),
        valueOf("Quickslot_12"), valueOf(1),
        
        valueOf("Quickbar1Visibility"), valueOf(1),
        valueOf("Quickslot_13"), valueOf(1),
        valueOf("Quickslot_14"), valueOf(1),
        valueOf("Quickslot_15"), valueOf(1),
        valueOf("Quickslot_16"), valueOf(1),
        valueOf("Quickslot_17"), valueOf(1),
        valueOf("Quickslot_18"), valueOf(1),
        valueOf("Quickslot_19"), valueOf(1),
        valueOf("Quickslot_20"), valueOf(1),
        valueOf("Quickslot_21"), valueOf(1),
        valueOf("Quickslot_22"), valueOf(1),
        valueOf("Quickslot_23"), valueOf(1),
        valueOf("Quickslot_24"), valueOf(1),
        
        valueOf("Quickbar2Visibility"), valueOf(1),
        valueOf("Quickslot_25"), valueOf(1),
        valueOf("Quickslot_26"), valueOf(1),
        valueOf("Quickslot_27"), valueOf(1),
        valueOf("Quickslot_28"), valueOf(1),
        valueOf("Quickslot_29"), valueOf(1),
        valueOf("Quickslot_30"), valueOf(1),
        valueOf("Quickslot_31"), valueOf(1),
        valueOf("Quickslot_32"), valueOf(1),
        valueOf("Quickslot_33"), valueOf(1),
        valueOf("Quickslot_34"), valueOf(1),
        valueOf("Quickslot_35"), valueOf(1),
        valueOf("Quickslot_36"), valueOf(1),
        
        valueOf("Quickbar3Visibility"), valueOf(1),
        valueOf("Quickslot_37"), valueOf(1),
        valueOf("Quickslot_38"), valueOf(1),
        valueOf("Quickslot_39"), valueOf(1),
        valueOf("Quickslot_40"), valueOf(1),
        valueOf("Quickslot_41"), valueOf(1),
        valueOf("Quickslot_42"), valueOf(1),
        valueOf("Quickslot_43"), valueOf(1),
        valueOf("Quickslot_44"), valueOf(1),
        valueOf("Quickslot_45"), valueOf(1),
        valueOf("Quickslot_46"), valueOf(1),
        valueOf("Quickslot_47"), valueOf(1),
        valueOf("Quickslot_48"), valueOf(1),
        
        valueOf("Quickbar4Visibility"), valueOf(1),
        valueOf("Quickslot_49"), valueOf(1),
        valueOf("Quickslot_50"), valueOf(1),
        valueOf("Quickslot_51"), valueOf(1),
        valueOf("Quickslot_52"), valueOf(1),
        valueOf("Quickslot_53"), valueOf(1),
        valueOf("Quickslot_54"), valueOf(1),
        valueOf("Quickslot_55"), valueOf(1),
        valueOf("Quickslot_56"), valueOf(1),
        valueOf("Quickslot_57"), valueOf(1),
        valueOf("Quickslot_58"), valueOf(1),
        valueOf("Quickslot_59"), valueOf(1),
        valueOf("Quickslot_60"), valueOf(1),
        
        valueOf("Quickbar5Visibility"), valueOf(1),
        valueOf("Quickslot_61"), valueOf(1),
        valueOf("Quickslot_62"), valueOf(1),
        valueOf("Quickslot_63"), valueOf(1),
        valueOf("Quickslot_64"), valueOf(1),
        valueOf("Quickslot_65"), valueOf(1),
        valueOf("Quickslot_66"), valueOf(1),
        valueOf("Quickslot_67"), valueOf(1),
        valueOf("Quickslot_68"), valueOf(1),
        valueOf("Quickslot_69"), valueOf(1),
        valueOf("Quickslot_70"), valueOf(1),
        valueOf("Quickslot_71"), valueOf(1),
        valueOf("Quickslot_72"), valueOf(1),
        
        valueOf("SelectionSelf"), valueOf(1),
        valueOf("SelectionNearestFoe"), valueOf(1),
        valueOf("SelectionNextFoe"), valueOf(1),
        valueOf("SelectionPreviousFoe"), valueOf(1),
        valueOf("SelectionNextTracked"), valueOf(1),
        valueOf("SelectionPreviousTracked"), valueOf(1),
        valueOf("SelectFellowOne"), valueOf(1),
        valueOf("SelectFellowTwo"), valueOf(1),
        valueOf("SelectFellowThree"), valueOf(1),
        valueOf("SelectFellowFour"), valueOf(1),
        valueOf("SelectFellowFive"), valueOf(1),
        valueOf("SelectFellowSix"), valueOf(1),
        valueOf("AssistFellowTwo"), valueOf(1),
        valueOf("AssistFellowThree"), valueOf(1),
        valueOf("AssistFellowFour"), valueOf(1),
        valueOf("AssistFellowFive"), valueOf(1),
        valueOf("AssistFellowSix"), valueOf(1),
        valueOf("SelectionNearestFellow"), valueOf(1),
        valueOf("SelectionNearestPlayer"), valueOf(1),
        valueOf("SelectionNextPlayer"), valueOf(1),
        valueOf("SelectionPreviousPlayer"), valueOf(1),
        valueOf("SelectionNearestCreature"), valueOf(1),
        valueOf("SelectionNextCreature"), valueOf(1),
        valueOf("SelectionPreviousCreature"), valueOf(1),
        valueOf("SelectionNearestItem"), valueOf(1),
        valueOf("SelectionNextItem"), valueOf(1),
        valueOf("SelectionPreviousItem"), valueOf(1),
        valueOf("PreviousSelection"), valueOf(1),
        valueOf("PreviousAttacker"), valueOf(1),
        valueOf("SelectionAssist"), valueOf(1),
        
        valueOf("ToggleSkillPanel"), valueOf(1),
        valueOf("ToggleTraitPanel"), valueOf(1),
        valueOf("HousingPanel"), valueOf(1),
        valueOf("ToggleCraftingPanel"), valueOf(1),
        valueOf("MapPanel"), valueOf(1),
        valueOf("ToggleJournalPanel"), valueOf(1),
        valueOf("TitlesPanel"), valueOf(1),
        valueOf("ToggleSocialPanel"), valueOf(1),
        valueOf("ToggleBags"), valueOf(1),
        valueOf("ToggleBag1"), valueOf(1),
        valueOf("ToggleBag2"), valueOf(1),
        valueOf("ToggleBag3"), valueOf(1),
        valueOf("ToggleBag4"), valueOf(1),
        valueOf("ToggleBag5"), valueOf(1),
        valueOf("ToggleBag6"), valueOf(1),
        valueOf("DressingRoom"), valueOf(1),
        valueOf("ItemLinkToChat"), valueOf(1),
        valueOf("MultiUseItem"), valueOf(1),
        valueOf("ToggleOptionsPanel"), valueOf(1),
        valueOf("ToggleAssistancePanel"), valueOf(1),
        valueOf("ToggleRadar"), valueOf(1),
        valueOf("ToggleQuestPanel"), valueOf(1),
        valueOf("ToggleAccomplishmentPanel"), valueOf(1),
        valueOf("ToggleItemAdvancementPanel"), valueOf(1),
        valueOf("ToggleMountsPanel"), valueOf(1),
        valueOf("ToggleInstanceFinderPanel"), valueOf(1),
        valueOf("ToggleSkirmishPanel"), valueOf(1),
        valueOf("ToggleMountedCombatUI"), valueOf(1),
        valueOf("ToggleWebStore"), valueOf(1),
        valueOf("ReputationPanel"), valueOf(1),
        valueOf("HobbyPanel"), valueOf(1),
        valueOf("DestinyPointPerksPanel"), valueOf(1),
        valueOf("ToggleSocialFellowingPanel"), valueOf(1),
        valueOf("FriendsPanel"), valueOf(1),
        valueOf("KinshipPanel"), valueOf(1),
        valueOf("RaidPanel"), valueOf(1),
        valueOf("GroupStagePanel"), valueOf(1),
        valueOf("TogglePaperItemPanel"), valueOf(1),
        valueOf("ToggleRandomItemRewardUI"), valueOf(1),
        valueOf("TogglePendingLoot"), valueOf(1),
        valueOf("TogglePluginManager"), valueOf(1),
        valueOf("ToggleCollectionUI"), valueOf(1),
        valueOf("ToggleFilterUI"), valueOf(1),
        
        valueOf("ChatModeReply"), valueOf(1),
        
        valueOf("QuickSlot_SkillMode"), valueOf(1),
        valueOf("Use"), valueOf(1),
        valueOf("FollowSelection"), valueOf(1),
        valueOf("FindItems"), valueOf(1),
        valueOf("ToggleBigBattlesUI"), valueOf(1),
        valueOf("ToggleMailUI"), valueOf(1),
        valueOf("Show_Names"), valueOf(1),
        valueOf("ShowDamage"), valueOf(1),
        valueOf("CaptureScreenshot"), valueOf(1),
        valueOf("Tooltip_Detach"), valueOf(1),
        valueOf("ToggleHiddenDragBoxes"), valueOf(1),
        valueOf("ToggleQuickslotLock"), valueOf(1),
        valueOf("UI_Toggle / ToggleHUD"), valueOf(1),
        valueOf("Logout"), valueOf(1),
        valueOf("VoiceChat_Talk"), valueOf(1),
        valueOf("ToggleItemSellLock"), valueOf(1),
        valueOf("(Loot all)"), valueOf(1),
        valueOf("DismountRemount"), valueOf(1),
        valueOf("ShowRemoteQuestActions"), valueOf(1),
        valueOf("TrackNearbyQuests"), valueOf(1),
        valueOf("ClearAllFilters"), valueOf(1),
        
        valueOf("ToggleMusicMode"), valueOf(1),
        valueOf("MusicEndSong"), valueOf(1),
        valueOf("Music_C2"), valueOf(1),
        valueOf("Music_Db2"), valueOf(1),
        valueOf("Music_D2"), valueOf(1),
        valueOf("Music_Eb2"), valueOf(1),
        valueOf("Music_E2"), valueOf(1),
        valueOf("Music_F2"), valueOf(1),
        valueOf("Music_Gb2"), valueOf(1),
        valueOf("Music_G2"), valueOf(1),
        valueOf("Music_Ab2"), valueOf(1),
        valueOf("Music_A2"), valueOf(1),
        valueOf("Music_Bb2"), valueOf(1),
        valueOf("Music_B2"), valueOf(1),
        valueOf("Music_C3"), valueOf(1),
        valueOf("Music_Db3"), valueOf(1),
        valueOf("Music_D3"), valueOf(1),
        valueOf("Music_Eb3"), valueOf(1),
        valueOf("Music_E3"), valueOf(1),
        valueOf("Music_F3"), valueOf(1),
        valueOf("Music_Gb3"), valueOf(1),
        valueOf("Music_G3"), valueOf(1),
        valueOf("Music_Ab3"), valueOf(1),
        valueOf("Music_A3"), valueOf(1),
        valueOf("Music_Bb3"), valueOf(1),
        valueOf("Music_B3"), valueOf(1),
        valueOf("Music_C4"), valueOf(1),
        valueOf("Music_Db4"), valueOf(1),
        valueOf("Music_D4"), valueOf(1),
        valueOf("Music_Eb4"), valueOf(1),
        valueOf("Music_E4"), valueOf(1),
        valueOf("Music_F4"), valueOf(1),
        valueOf("Music_Gb4"), valueOf(1),
        valueOf("Music_G4"), valueOf(1),
        valueOf("Music_Ab4"), valueOf(1),
        valueOf("Music_A4"), valueOf(1),
        valueOf("Music_Bb4"), valueOf(1),
        valueOf("Music_B4"), valueOf(1),
        valueOf("Music_C5"), valueOf(1),
        
        valueOf("FellowshipSkillAssist"), valueOf(1),
        valueOf("TopFellowshipManoeuvre"), valueOf(1),
        valueOf("BottomFellowshipManoeuvre"), valueOf(1),
        valueOf("LeftFellowshipManoeuvre"), valueOf(1),
        valueOf("RightFellowshipManoeuvre"), valueOf(1),
        
        valueOf("ShieldMark"), valueOf(1),
        valueOf("SpearMark"), valueOf(1),
        valueOf("ArrowMark"), valueOf(1),
        valueOf("SunMark"), valueOf(1),
        valueOf("SwordsMark"), valueOf(1),
        valueOf("MoonMark"), valueOf(1),
        valueOf("StarMark"), valueOf(1),
        valueOf("ClawMark"), valueOf(1),
        valueOf("SkullMark"), valueOf(1),
        valueOf("LeafMark"), valueOf(1),
        
        valueOf("PresentMainInventory"), valueOf(1),
        valueOf("PresentOutfit1"), valueOf(1),
        valueOf("PresentOutfit2"), valueOf(1),
        valueOf("PresentOutfit3"), valueOf(1),
        valueOf("PresentOutfit4"), valueOf(1),
        valueOf("PresentOutfit5"), valueOf(1),
        valueOf("PresentOutfit6"), valueOf(1),
        valueOf("PresentOutfit7"), valueOf(1),
        valueOf("PresentOutfit8"), valueOf(1),
        
        valueOf("CameraInstantMouseLook"), valueOf(1),
        valueOf("Escape"), valueOf(1),
        valueOf("Start_Command"), valueOf(1),
        valueOf("ToggleAlertOverflow"), valueOf(1),
        valueOf("ToggleBioPanel"), valueOf(1),
        valueOf("TogglePVPPanel"), valueOf(1),
        valueOf("VendorFullStack"), valueOf(1),
        valueOf("VendorQuantity"), valueOf(1),
        valueOf("ToggleStackDisplay"), valueOf(1)
    ));
    
    LuaTable uiEnv = env.rawget("Turbine").checkTable().rawget("UI").checkTable();
    LuaTable luaControlClass = uiEnv.rawget("Control").checkTable();
    uiLotroEnv.rawset("Font", font);
    uiLotroEnv.rawset("TextBox", uiEnv.rawget("TextBox"));
    uiLotroEnv.rawset("Button", uiEnv.rawget("Button"));
    uiLotroEnv.rawset("ScrollBar", uiEnv.rawget("ScrollBar"));
    LuaWindow.add(state, uiLotroEnv, luaControlClass);
    
    uiLotroEnv.rawset("LotroUI", RegisteredFunction.bind(new RegisteredFunction[]{
        RegisteredFunction.of("IsEnabled", Lotro::isEnabled),
        RegisteredFunction.of("SetEnabled", Lotro::setEnabled),
        RegisteredFunction.of("Reset", Lotro::reset)
    }));
  }
  
  public static LuaBoolean isEnabled(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
  
  public static LuaValue setEnabled(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue reset(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
}