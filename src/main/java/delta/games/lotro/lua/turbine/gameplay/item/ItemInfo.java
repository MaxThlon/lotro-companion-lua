package delta.games.lotro.lua.turbine.gameplay.item;

import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;

/**
 * ActiveSkill library for lua scripts.
 * @author MaxThlon
 */
public class ItemInfo {
  /**
   * Initialize lua ItemInfo package
   * @param lua .
   * @param envIndex .
   * @param errfunc .
   * @return Lua.LuaError.
   */
  public static Lua.LuaError add(Lua lua, int envIndex, int errfunc) {
  	Lua.LuaError error;
  	error = LuaTools.pushClass(lua, errfunc, "Turbine", "Object");
  	if (error != Lua.LuaError.OK) return error;
    lua.push((JFunction)ItemInfo::constructor);
    lua.setField(-2, "Constructor");
    lua.push((JFunction)ItemInfo::getName);
    lua.setField(-2, "GetName");
    lua.push((JFunction)ItemInfo::getNameWithQuantity);
    lua.setField(-2, "GetNameWithQuantity");
    lua.push((JFunction)ItemInfo::getDescription);
    lua.setField(-2, "GetDescription");
    lua.push((JFunction)ItemInfo::getCategory);
    lua.setField(-2, "GetCategory");
    lua.push((JFunction)ItemInfo::isMagic);
    lua.setField(-2, "IsMagic");
    lua.push((JFunction)ItemInfo::isUnique);
    lua.setField(-2, "IsUnique");
    lua.push((JFunction)ItemInfo::getQuality);
    lua.setField(-2, "GetQuality");
    lua.push((JFunction)ItemInfo::getDurability);
    lua.setField(-2, "GetDurability");
    lua.push((JFunction)ItemInfo::getMaxQuantity);
    lua.setField(-2, "GetMaxQuantity");
    lua.push((JFunction)ItemInfo::getMaxStackSize);
    lua.setField(-2, "GetMaxStackSize");

    lua.push((JFunction)ItemInfo::getIconImageID);
    lua.setField(-2, "GetIconImageID");
    lua.push((JFunction)ItemInfo::getBackgroundImageID);
    lua.setField(-2, "GetBackgroundImageID");
    lua.push((JFunction)ItemInfo::getQualityImageID);
    lua.setField(-2, "GetQualityImageID");
    lua.push((JFunction)ItemInfo::getUnderlayImageID);
    lua.setField(-2, "GetUnderlayImageID");
    lua.push((JFunction)ItemInfo::getShadowImageID);
    lua.setField(-2, "GetShadowImageID");

    lua.setField(-2, "ItemInfo");
    return error;
  }

  private static int constructor(Lua lua) {
    return 1;
  }

  private static int getName(Lua lua) {
    return 1;
  }
  
  private static int getNameWithQuantity(Lua lua) {
    return 1;
  }
  
  private static int getDescription(Lua lua) {
    return 1;
  }
  
  private static int getCategory(Lua lua) {
    return 1;
  }
  
  private static int isMagic(Lua lua) {
    return 1;
  }
  
  private static int isUnique(Lua lua) {
    return 1;
  }

  private static int getQuality(Lua lua) {
    return 1;
  }
  
  private static int getDurability(Lua lua) {
    return 1;
  }
  
  private static int getMaxQuantity(Lua lua) {
    return 1;
  }
  
  private static int getMaxStackSize(Lua lua) {
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

