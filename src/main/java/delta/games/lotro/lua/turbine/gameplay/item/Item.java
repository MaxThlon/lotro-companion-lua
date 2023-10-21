package delta.games.lotro.lua.turbine.gameplay.item;

import static org.squiddev.cobalt.ValueFactory.tableOf;
import static org.squiddev.cobalt.ValueFactory.valueOf;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaBoolean;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaNumber;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.LuaValue;
import org.squiddev.cobalt.UnwindThrowable;
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.function.RegisteredFunction;

import delta.games.lotro.common.enums.ItemClass;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.lore.items.ItemQuality;

/**
 * @author MaxThlon
 */
public class Item
{
  public static void add(LuaState state, LuaTable gameplayEnv,
                         LuaFunction luaClass, LuaValue luaEntityClass) throws LuaError, UnwindThrowable {
    LuaTable itemCategoryTable = new LuaTable();
    for (ItemClass itemClass:LotroEnumsRegistry.getInstance().get(ItemClass.class).getAll()) {
      itemCategoryTable.rawset(valueOf(itemClass.getLabel()), valueOf(itemClass.getCode()));
    }
    gameplayEnv.rawset("ItemCategory", itemCategoryTable);
    
    gameplayEnv.rawset("ItemDurability", tableOf(
        valueOf("Undefined"), valueOf(0),
        valueOf("Substantial"), valueOf(1),
        valueOf("Brittle"), valueOf(2),
        valueOf("Normal"), valueOf(3),
        valueOf("Tough"), valueOf(4),
        valueOf("Flimsy"), valueOf(5),
        valueOf("Indestructible"), valueOf(6),
        valueOf("Weak"), valueOf(7)
    ));
    
    LuaTable itemQualityTable = new LuaTable();
    for (ItemQuality itemQuality:LotroEnumsRegistry.getInstance().get(ItemQuality.class).getAll()) {
      itemQualityTable.rawset(valueOf(itemQuality.getKey()), valueOf(itemQuality.getCode()));
    }
    gameplayEnv.rawset("ItemQuality", itemQualityTable);
    
    gameplayEnv.rawset("ItemWearState", tableOf(
        valueOf("Undefined"), valueOf(0),
        valueOf("Pristine"), valueOf(1),
        valueOf("Worn"), valueOf(2),
        valueOf("Damaged"), valueOf(3),
        valueOf("Broken"), valueOf(4)
    ));

    ItemInfo.add(state, gameplayEnv);

    LuaTable luaItemClass = luaClass.call(state, luaEntityClass).checkTable();
    RegisteredFunction.bind(luaItemClass, new RegisteredFunction[]{
      RegisteredFunction.of("Constructor", Item::constructor),
      RegisteredFunction.of("GetCategory", Item::getCategory),
      RegisteredFunction.of("GetItemInfo", Item::getItemInfo),
      RegisteredFunction.of("IsMagic", Item::isMagic),
      RegisteredFunction.of("GetEffects", Item::getEffects),
      RegisteredFunction.of("GetQuality", Item::getQuality),
      RegisteredFunction.of("GetDurability", Item::getDurability),
      RegisteredFunction.of("GetWearState", Item::getWearState),

      RegisteredFunction.of("GetMaxStackSize", Item::getMaxStackSize),
      RegisteredFunction.of("GetQuantity", Item::getQuantity),
      
      RegisteredFunction.of("GetIconImageID", Item::getIconImageID),
      RegisteredFunction.of("GetBackgroundImageID", Item::getBackgroundImageID),
      RegisteredFunction.of("GetQualityImageID", Item::getQualityImageID),
      RegisteredFunction.of("GetUnderlayImageID", Item::getUnderlayImageID),
      RegisteredFunction.of("GetShadowImageID", Item::getShadowImageID)
    });
    gameplayEnv.rawset("Item", luaItemClass);
  }
  
  public static LuaValue constructor(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getCategory(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getItemInfo(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaBoolean isMagic(LuaState state, LuaValue self) {
    return Constants.FALSE;
  }
  
  public static LuaValue getEffects(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getQuality(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getDurability(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaValue getWearState(LuaState state, LuaValue self) {
    return Constants.NIL;
  }
  
  public static LuaNumber getMaxStackSize(LuaState state, LuaValue self) {
    return Constants.ZERO;
  }
  
  public static LuaNumber getQuantity(LuaState state, LuaValue self) {
    return Constants.ZERO;
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
