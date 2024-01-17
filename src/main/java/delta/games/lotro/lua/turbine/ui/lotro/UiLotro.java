package delta.games.lotro.lua.turbine.ui.lotro;

import java.util.HashMap;

import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.lua51.Lua51Consts;

/**
 * Lotro library for lua scripts.
 * @author MaxThlon
 */
public abstract class UiLotro {
	
  /**
   * Initialize lua UiLotro package
   * @param lua .
   * @param envIndex .
   */
  @SuppressWarnings("boxing")
  public static void openPackage(Lua lua, int envIndex) {
  	Turbine.pushfenv(
    		lua,
    		envIndex,
    		"Turbine.UI.Lotro",
    		"Turbine", "UI", "Lotro"
    );

    lua.push(new HashMap<String, String>() {{
      put("Verdana12", "Verdana12");
      put("BookAntiqua12", "BookAntiqua12");
      put("BookAntiqua14", "BookAntiqua14");
      put("BookAntiqua16", "BookAntiqua16");
      put("BookAntiqua18", "BookAntiqua18");
      put("BookAntiqua20", "BookAntiqua20");
      put("BookAntiqua22", "BookAntiqua22");
      put("BookAntiqua24", "BookAntiqua24");
      
      put("BookAntiquaBold12", "BookAntiquaBold12");
      put("BookAntiquaBold14", "BookAntiquaBold14");
      put("BookAntiquaBold18", "BookAntiquaBold18");
      put("BookAntiquaBold19", "BookAntiquaBold19");
      put("BookAntiquaBold22", "BookAntiquaBold22");
      put("BookAntiquaBold24", "BookAntiquaBold24");
      put("BookAntiquaBold26", "BookAntiquaBold26");
      put("BookAntiquaBold28", "BookAntiquaBold28");
      put("BookAntiquaBold32", "BookAntiquaBold32");
      put("BookAntiquaBold36", "BookAntiquaBold36");
      
      put("FixedSys15", "FixedSys15");
      
      put("LucidaConsole12", "LucidaConsole12");
      
      put("TrajanPro13", "TrajanPro13");
      put("TrajanPro14", "TrajanPro14");
      put("TrajanPro15", "TrajanPro15");
      put("TrajanPro16", "TrajanPro16");
      put("TrajanPro18", "TrajanPro18");
      put("TrajanPro19", "TrajanPro19");
      put("TrajanPro20", "TrajanPro20");
      put("TrajanPro21", "TrajanPro21");
      put("TrajanPro23", "TrajanPro23");
      put("TrajanPro24", "TrajanPro24");
      put("TrajanPro25", "TrajanPro25");
      put("TrajanPro26", "TrajanPro26");
      put("TrajanPro28", "TrajanPro28");
      
      put("TrajanProBold16", "TrajanProBold16");
      put("TrajanProBold22", "TrajanProBold22");
      put("TrajanProBold24", "TrajanProBold24");
      put("TrajanProBold25", "TrajanProBold25");
      put("TrajanProBold30", "TrajanProBold30");
      put("TrajanProBold36", "TrajanProBold36");
      
      put("Undefined", "Undefined");
      
      put("Verdana10", "Verdana10");
      put("Verdana12", "Verdana12");
      put("Verdana14", "Verdana14");
      put("Verdana16", "Verdana16");
      put("Verdana18", "Verdana18");
      put("Verdana20", "Verdana20");
      put("Verdana22", "Verdana22");
      put("Verdana23", "Verdana23");
      put("VerdanaBold16", "VerdanaBold16");
    }}, Lua.Conversion.FULL);
    lua.setField(-2, "Font");

    lua.push(new HashMap<String, Integer>() {{
      put("Backpack1", 1);
      put("Backpack2", 2);
      put("Backpack3", 3);
      put("Backpack4", 4);
      put("Backpack5", 5);
      put("Backpack6", 6);
      put("Party", 7);
      put("PluginsManager", 8);
      put("Target", 10);
      put("Vitals", 11);
    }}, Lua.Conversion.FULL);
    lua.setField(-2, "LotroUIElement");

    lua.push(new HashMap<String, Integer>() {{
      put("QuickslotPageDown", 1);
      put("QuickslotPageUp", 1);
      put("Quickslot_1", 1);
      put("Quickslot_2", 1);
      put("Quickslot_3", 1);
      put("Quickslot_4", 1);
      put("Quickslot_5", 1);
      put("Quickslot_6", 1);
      put("Quickslot_7", 1);
      put("Quickslot_8", 1);
      put("Quickslot_9", 1);
      put("Quickslot_10", 1);
      put("Quickslot_11", 1);
      put("Quickslot_12", 1);
      
      put("Quickbar1Visibility", 1);
      put("Quickslot_13", 1);
      put("Quickslot_14", 1);
      put("Quickslot_15", 1);
      put("Quickslot_16", 1);
      put("Quickslot_17", 1);
      put("Quickslot_18", 1);
      put("Quickslot_19", 1);
      put("Quickslot_20", 1);
      put("Quickslot_21", 1);
      put("Quickslot_22", 1);
      put("Quickslot_23", 1);
      put("Quickslot_24", 1);
      
      put("Quickbar2Visibility", 1);
      put("Quickslot_25", 1);
      put("Quickslot_26", 1);
      put("Quickslot_27", 1);
      put("Quickslot_28", 1);
      put("Quickslot_29", 1);
      put("Quickslot_30", 1);
      put("Quickslot_31", 1);
      put("Quickslot_32", 1);
      put("Quickslot_33", 1);
      put("Quickslot_34", 1);
      put("Quickslot_35", 1);
      put("Quickslot_36", 1);
      
      put("Quickbar3Visibility", 1);
      put("Quickslot_37", 1);
      put("Quickslot_38", 1);
      put("Quickslot_39", 1);
      put("Quickslot_40", 1);
      put("Quickslot_41", 1);
      put("Quickslot_42", 1);
      put("Quickslot_43", 1);
      put("Quickslot_44", 1);
      put("Quickslot_45", 1);
      put("Quickslot_46", 1);
      put("Quickslot_47", 1);
      put("Quickslot_48", 1);
      
      put("Quickbar4Visibility", 1);
      put("Quickslot_49", 1);
      put("Quickslot_50", 1);
      put("Quickslot_51", 1);
      put("Quickslot_52", 1);
      put("Quickslot_53", 1);
      put("Quickslot_54", 1);
      put("Quickslot_55", 1);
      put("Quickslot_56", 1);
      put("Quickslot_57", 1);
      put("Quickslot_58", 1);
      put("Quickslot_59", 1);
      put("Quickslot_60", 1);
      
      put("Quickbar5Visibility", 1);
      put("Quickslot_61", 1);
      put("Quickslot_62", 1);
      put("Quickslot_63", 1);
      put("Quickslot_64", 1);
      put("Quickslot_65", 1);
      put("Quickslot_66", 1);
      put("Quickslot_67", 1);
      put("Quickslot_68", 1);
      put("Quickslot_69", 1);
      put("Quickslot_70", 1);
      put("Quickslot_71", 1);
      put("Quickslot_72", 1);
      
      put("SelectionSelf", 1);
      put("SelectionNearestFoe", 1);
      put("SelectionNextFoe", 1);
      put("SelectionPreviousFoe", 1);
      put("SelectionNextTracked", 1);
      put("SelectionPreviousTracked", 1);
      put("SelectFellowOne", 1);
      put("SelectFellowTwo", 1);
      put("SelectFellowThree", 1);
      put("SelectFellowFour", 1);
      put("SelectFellowFive", 1);
      put("SelectFellowSix", 1);
      put("AssistFellowTwo", 1);
      put("AssistFellowThree", 1);
      put("AssistFellowFour", 1);
      put("AssistFellowFive", 1);
      put("AssistFellowSix", 1);
      put("SelectionNearestFellow", 1);
      put("SelectionNearestPlayer", 1);
      put("SelectionNextPlayer", 1);
      put("SelectionPreviousPlayer", 1);
      put("SelectionNearestCreature", 1);
      put("SelectionNextCreature", 1);
      put("SelectionPreviousCreature", 1);
      put("SelectionNearestItem", 1);
      put("SelectionNextItem", 1);
      put("SelectionPreviousItem", 1);
      put("PreviousSelection", 1);
      put("PreviousAttacker", 1);
      put("SelectionAssist", 1);
      
      put("ToggleSkillPanel", 1);
      put("ToggleTraitPanel", 1);
      put("HousingPanel", 1);
      put("ToggleCraftingPanel", 1);
      put("MapPanel", 1);
      put("ToggleJournalPanel", 1);
      put("TitlesPanel", 1);
      put("ToggleSocialPanel", 1);
      put("ToggleBags", 1);
      put("ToggleBag1", 1);
      put("ToggleBag2", 1);
      put("ToggleBag3", 1);
      put("ToggleBag4", 1);
      put("ToggleBag5", 1);
      put("ToggleBag6", 1);
      put("DressingRoom", 1);
      put("ItemLinkToChat", 1);
      put("MultiUseItem", 1);
      put("ToggleOptionsPanel", 1);
      put("ToggleAssistancePanel", 1);
      put("ToggleRadar", 1);
      put("ToggleQuestPanel", 1);
      put("ToggleAccomplishmentPanel", 1);
      put("ToggleItemAdvancementPanel", 1);
      put("ToggleMountsPanel", 1);
      put("ToggleInstanceFinderPanel", 1);
      put("ToggleSkirmishPanel", 1);
      put("ToggleMountedCombatUI", 1);
      put("ToggleWebStore", 1);
      put("ReputationPanel", 1);
      put("HobbyPanel", 1);
      put("DestinyPointPerksPanel", 1);
      put("ToggleSocialFellowingPanel", 1);
      put("FriendsPanel", 1);
      put("KinshipPanel", 1);
      put("RaidPanel", 1);
      put("GroupStagePanel", 1);
      put("TogglePaperItemPanel", 1);
      put("ToggleRandomItemRewardUI", 1);
      put("TogglePendingLoot", 1);
      put("TogglePluginManager", 1);
      put("ToggleCollectionUI", 1);
      put("ToggleFilterUI", 1);
      
      put("ChatModeReply", 1);
      
      put("QuickSlot_SkillMode", 1);
      put("Use", 1);
      put("FollowSelection", 1);
      put("FindItems", 1);
      put("ToggleBigBattlesUI", 1);
      put("ToggleMailUI", 1);
      put("Show_Names", 1);
      put("ShowDamage", 1);
      put("CaptureScreenshot", 1);
      put("Tooltip_Detach", 1);
      put("ToggleHiddenDragBoxes", 1);
      put("ToggleQuickslotLock", 1);
      put("UI_Toggle / ToggleHUD", 1);
      put("Logout", 1);
      put("VoiceChat_Talk", 1);
      put("ToggleItemSellLock", 1);
      put("(Loot all)", 1);
      put("DismountRemount", 1);
      put("ShowRemoteQuestActions", 1);
      put("TrackNearbyQuests", 1);
      put("ClearAllFilters", 1);
      
      put("ToggleMusicMode", 1);
      put("MusicEndSong", 1);
      put("Music_C2", 1);
      put("Music_Db2", 1);
      put("Music_D2", 1);
      put("Music_Eb2", 1);
      put("Music_E2", 1);
      put("Music_F2", 1);
      put("Music_Gb2", 1);
      put("Music_G2", 1);
      put("Music_Ab2", 1);
      put("Music_A2", 1);
      put("Music_Bb2", 1);
      put("Music_B2", 1);
      put("Music_C3", 1);
      put("Music_Db3", 1);
      put("Music_D3", 1);
      put("Music_Eb3", 1);
      put("Music_E3", 1);
      put("Music_F3", 1);
      put("Music_Gb3", 1);
      put("Music_G3", 1);
      put("Music_Ab3", 1);
      put("Music_A3", 1);
      put("Music_Bb3", 1);
      put("Music_B3", 1);
      put("Music_C4", 1);
      put("Music_Db4", 1);
      put("Music_D4", 1);
      put("Music_Eb4", 1);
      put("Music_E4", 1);
      put("Music_F4", 1);
      put("Music_Gb4", 1);
      put("Music_G4", 1);
      put("Music_Ab4", 1);
      put("Music_A4", 1);
      put("Music_Bb4", 1);
      put("Music_B4", 1);
      put("Music_C5", 1);
      
      put("FellowshipSkillAssist", 1);
      put("TopFellowshipManoeuvre", 1);
      put("BottomFellowshipManoeuvre", 1);
      put("LeftFellowshipManoeuvre", 1);
      put("RightFellowshipManoeuvre", 1);
      
      put("ShieldMark", 1);
      put("SpearMark", 1);
      put("ArrowMark", 1);
      put("SunMark", 1);
      put("SwordsMark", 1);
      put("MoonMark", 1);
      put("StarMark", 1);
      put("ClawMark", 1);
      put("SkullMark", 1);
      put("LeafMark", 1);
      
      put("PresentMainInventory", 1);
      put("PresentOutfit1", 1);
      put("PresentOutfit2", 1);
      put("PresentOutfit3", 1);
      put("PresentOutfit4", 1);
      put("PresentOutfit5", 1);
      put("PresentOutfit6", 1);
      put("PresentOutfit7", 1);
      put("PresentOutfit8", 1);
      
      put("CameraInstantMouseLook", 1);
      put("Escape", 1);
      put("Start_Command", 1);
      put("ToggleAlertOverflow", 1);
      put("ToggleBioPanel", 1);
      put("TogglePVPPanel", 1);
      put("VendorFullStack", 1);
      put("VendorQuantity", 1);
      put("ToggleStackDisplay", 1);
    }}, Lua.Conversion.FULL);
    lua.setField(-2, "Action");

    LuaLotroDragDropInfo.add(lua, -1);
    
    LuaTools.pushValue(lua, Lua51Consts.LUA_ENVIRONINDEX, "Turbine", "UI" , "TextBox");
    lua.setField(-2, "TextBox");

    LuaTools.pushValue(lua, Lua51Consts.LUA_ENVIRONINDEX, "Turbine", "UI" , "Button");
    lua.setField(-2, "Button");

    LuaTools.pushValue(lua, Lua51Consts.LUA_ENVIRONINDEX, "Turbine", "UI" , "ScrollBar");
    lua.setField(-2, "ScrollBar");

    LuaTools.pushValue(lua, Lua51Consts.LUA_ENVIRONINDEX, "Turbine", "UI" , "Window");
    lua.setField(-2, "Window");

    Quickslot.add(lua, envIndex);

    /* LotroUI */
    LuaTools.newClassInstance(lua, -1, (relativeEnvIndex) -> {
      LuaTools.pushClass(lua, "Turbine", "Object");
      LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(relativeEnvIndex.intValue(), -1), "Constructor", UiLotro::constructor);
      LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(relativeEnvIndex.intValue(), -1), "IsEnabled", UiLotro::isEnabled);
      LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(relativeEnvIndex.intValue(), -1), "SetEnabled", UiLotro::setEnabled);
      LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(relativeEnvIndex.intValue(), -1), "Reset", UiLotro::reset);
    });
    lua.setField(-2, "LotroUI");

    lua.pop(1); /* pop env */
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }

  private static int isEnabled(Lua lua) {
    return 1;
  }
  
  private static int setEnabled(Lua lua) {
    return 1;
  }
  
  private static int reset(Lua lua) {
    return 1;
  }
}