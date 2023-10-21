package delta.games.lotro.lua.turbine.gameplay.item;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaString;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.function.RegisteredFunction;

/**
 * ActiveSkill library for lua scripts.
 * @author MaxThlon
 */
public class ItemInfo {
  public static void add(LuaState state, LuaTable gameplayEnv) {
    gameplayEnv.rawset("ItemInfo", RegisteredFunction.bind(new RegisteredFunction[]{
        RegisteredFunction.of("GetName", ItemInfo::getName),
        RegisteredFunction.of("GetNameWithQuantity", ItemInfo::getNameWithQuantity),
        RegisteredFunction.of("GetDescription", ItemInfo::getDescription),
        RegisteredFunction.of("GetCategory", ItemInfo::getCategory),
        RegisteredFunction.of("IsMagic", ItemInfo::isMagic),
        RegisteredFunction.of("IsUnique", ItemInfo::isUnique),
        RegisteredFunction.of("GetQuality", ItemInfo::getQuality),
        RegisteredFunction.of("GetDurability", ItemInfo::getDurability),
        
        RegisteredFunction.of("GetMaxQuantity", ItemInfo::getMaxQuantity),
        RegisteredFunction.of("GetMaxStackSize", ItemInfo::getMaxStackSize),
        
        RegisteredFunction.of("GetIconImageID", ItemInfo::getIconImageID),
        RegisteredFunction.of("GetBackgroundImageID", ItemInfo::getBackgroundImageID),
        RegisteredFunction.of("GetQualityImageID", ItemInfo::getQualityImageID),
        RegisteredFunction.of("GetUnderlayImageID", ItemInfo::getUnderlayImageID),
        RegisteredFunction.of("GetShadowImageID", ItemInfo::getShadowImageID)
        
    }));
  }

  public static LuaString getName(LuaState state, LuaValue self) {
    return Constants.EMPTYSTRING;
  }
  
  public static LuaString getNameWithQuantity(LuaState state, LuaValue self) {
    return Constants.EMPTYSTRING;
  }
  
  public static LuaValue getDescription(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getCategory(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue isMagic(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue isUnique(LuaState state, LuaValue self) {
    return Constants.NIL;
  }

  public static LuaValue getQuality(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getDurability(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getMaxQuantity(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getMaxStackSize(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getIconImageID(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getBackgroundImageID(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getQualityImageID(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getUnderlayImageID(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getShadowImageID(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
}

