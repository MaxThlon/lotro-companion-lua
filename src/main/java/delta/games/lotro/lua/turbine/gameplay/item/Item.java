package delta.games.lotro.lua.turbine.gameplay.item;

import java.util.HashMap;

import delta.games.lotro.common.enums.ItemClass;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;

/**
 * @author MaxThlon
 */
public class Item
{
  /**
   * Initialize lua Item package
   * @param lua .
   * @param envIndex .
   * @param errfunc .
   * @return Lua.LuaError.
   */
  @SuppressWarnings("boxing")
	public static Lua.LuaError add(Lua lua, int envIndex, int errfunc) {
  	Lua.LuaError error;

    lua.push(new HashMap<String, Integer>() {{
      for (ItemClass itemClass:LotroEnumsRegistry.getInstance().get(ItemClass.class).getAll()) {
        put(itemClass.getLabel(), itemClass.getCode());
      }
    }}, Lua.Conversion.FULL);
    lua.setField(LuaTools.relativizeIndex(envIndex, -1), "ItemCategory");

    lua.push(new HashMap<String, Integer>() {{
        put("Undefined", 0);
        put("Substantial", 1);
        put("Brittle", 2);
        put("Normal", 3);
        put("Tough", 4);
        put("Flimsy", 5);
        put("Indestructible", 6);
        put("Weak", 7);
    }}, Lua.Conversion.FULL);
    lua.setField(LuaTools.relativizeIndex(envIndex, -1), "ItemDurability");

    lua.push(new HashMap<String, Integer>() {{
      for (ItemQuality itemQuality:LotroEnumsRegistry.getInstance().get(ItemQuality.class).getAll()) {
        put(itemQuality.getKey(), itemQuality.getCode());
      }
    }}, Lua.Conversion.FULL);
    lua.setField(LuaTools.relativizeIndex(envIndex, -1), "ItemQuality");

    lua.push(new HashMap<String, Integer>() {{
        put("Undefined", 0);
        put("Pristine", 1);
        put("Worn", 2);
        put("Damaged", 3);
        put("Broken", 4);
    }}, Lua.Conversion.FULL);
    lua.setField(LuaTools.relativizeIndex(envIndex, -1), "ItemWearState");

    error = ItemInfo.add(lua, envIndex, errfunc);
    if (error != Lua.LuaError.OK) return error;

    error = LuaTools.pushClass(lua, errfunc, "Turbine", "Gameplay", "Entity");
    if (error != Lua.LuaError.OK) return error;
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "Constructor", Item::constructor);
    lua.push((JFunction)Item::constructor);
    lua.setField(LuaTools.relativizeIndex(envIndex, -1), "Constructor");
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetCategory", Item::getCategory);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetItemInfo", Item::getItemInfo);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "IsMagic", Item::isMagic);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetEffects", Item::getEffects);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetQuality", Item::getQuality);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetDurability", Item::getDurability);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetWearState", Item::getWearState);
    
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetMaxStackSize", Item::getMaxStackSize);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetQuantity", Item::getQuantity);
    
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetIconImageID", Item::getIconImageID);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetBackgroundImageID", Item::getBackgroundImageID);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetQualityImageID", Item::getQualityImageID);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetUnderlayImageID", Item::getUnderlayImageID);
    LuaTools.setFunction(lua, -1, LuaTools.relativizeIndex(envIndex, -1), "GetShadowImageID", Item::getShadowImageID);

    lua.setField(LuaTools.relativizeIndex(envIndex, -1), "Item");
    return error;
  }
  
  private static int constructor(Lua lua) {
    return 1;
  }
  
  private static int getCategory(Lua lua) {
    return 1;
  }
  
  private static int getItemInfo(Lua lua) {
    return 1;
  }
  
  private static int isMagic(Lua lua) {
    return 1;
  }
  
  private static int getEffects(Lua lua) {
    return 1;
  }
  
  private static int getQuality(Lua lua) {
    return 1;
  }
  
  private static int getDurability(Lua lua) {
    return 1;
  }
  
  private static int getWearState(Lua lua) {
    return 1;
  }
  
  private static int getMaxStackSize(Lua lua) {
    return 1;
  }
  
  private static int getQuantity(Lua lua) {
    return 1;
  }
  
  private static int getIconImageID(Lua lua) {
    return 1;
  }
  
  private static int getBackgroundImageID(Lua lua) {
    return 1;
  }
  
  private static int getQualityImageID(Lua lua) {
    return 1;
  }
  
  private static int getUnderlayImageID(Lua lua) {
    return 1;
  }
  
  private static int getShadowImageID(Lua lua) {
    return 1;
  }
}
