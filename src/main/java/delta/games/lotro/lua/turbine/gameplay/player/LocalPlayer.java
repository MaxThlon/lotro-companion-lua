package delta.games.lotro.lua.turbine.gameplay.player;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.lua.LotroLuaModule;
import delta.games.lotro.lua.turbine.gameplay.attribute.Attributes;
import delta.games.lotro.lua.turbine.object.LuaObject;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.Conversion;
import party.iroiro.luajava.lua51.Lua51Consts;
import party.iroiro.luajava.value.LuaValue;

/**
 * @author MaxThlon
 */
public class LocalPlayer
{
  /**
   * Initialize lua LocalPlayer package
   * @param lua .
   * @param envIndex .
   */
  public static void add(Lua lua, int envIndex) {
  	LuaTools.pushClass(lua, "Turbine", "Gameplay", "Player");
  	lua.push((JFunction)LocalPlayer::constructor);
    lua.setField(-2, "Constructor");
    lua.push((JFunction)LocalPlayer::getInstance);
    lua.setField(-2, "GetInstance");
    lua.push((JFunction)LocalPlayer::getRaceAttributes);
    lua.setField(-2, "GetRaceAttributes");
    lua.push((JFunction)LocalPlayer::getClassAttributes);
    lua.setField(-2, "GetClassAttributes");
    lua.push((JFunction)LocalPlayer::getAttributes);
    lua.setField(-2, "GetAttributes");
    lua.push((JFunction)LocalPlayer::getTrainedSkills);
    lua.setField(-2, "GetTrainedSkills");
    lua.push((JFunction)LocalPlayer::getUntrainedSkills);
    lua.setField(-2, "GetUntrainedSkills");

    lua.push((JFunction)LocalPlayer::getWallet);
    lua.setField(-2, "GetWallet");
    lua.push((JFunction)LocalPlayer::getEquipment);
    lua.setField(-2, "GetEquipment");
    lua.push((JFunction)LocalPlayer::getBackpack);
    lua.setField(-2, "GetBackpack");
    lua.push((JFunction)LocalPlayer::getVault);
    lua.setField(-2, "GetVault");
    lua.push((JFunction)LocalPlayer::getSharedStorage);
    lua.setField(-2, "GetSharedStorage");
    lua.push((JFunction)LocalPlayer::getMount);
    lua.setField(-2, "GetMount");

    lua.push((JFunction)LocalPlayer::getParty);
    lua.setField(-2, "GetParty");
    lua.push((JFunction)LocalPlayer::isInCombat);
    lua.setField(-2, "IsInCombat");

    lua.setField(-2, "LocalPlayer");
  }
  
  /**
   * Push a already existing or new lua plugin instance.
   * @param lua .
   * @param characterFile .
   */
  public static void pushLocalPlayer(Lua lua, CharacterFile characterFile) {
  	LuaValue luaLocalPLayer = LuaObject.findLuaObjectFromObject(characterFile);
  	
  	if (luaLocalPLayer != null) {
  		lua.push(luaLocalPLayer, Conversion.NONE);
  	} else {
    	lua.pushValue(Lua51Consts.LUA_ENVIRONINDEX);
    	LuaTools.newClassInstance(lua, -1, (relativeEnvIndex) -> {
    		LuaTools.pushValue(lua, relativeEnvIndex.intValue(), "Turbine", "Gameplay", "Player");
    	});
      LuaObject.ObjectInheritedConstructor(
          lua,
          -1,
          luaLocalPLayer,
          null,
          null
      );
      lua.pushValue(-1);
      //luaLocalPLayer.getContext().setValue(LuaControl.jComponentKey_luaObjectSelf, lua.get());
      lua.replace(-1); /* env <- instance */
  	}
  }

  private static int constructor(Lua lua) {
    return 1;
  }
  
  private static int getInstance(Lua lua) {
  	LotroLuaModule luaLotro = (LotroLuaModule)LuaTools.getJavaLuaModule(lua);

  	pushLocalPlayer(lua, luaLotro.getCharacterFile());
  	return 1;
  }
  
  private static int getRaceAttributes(Lua lua) {
  	//LotroLuaModule luaLotro = (LotroLuaModule)LuaTools.getJavaLuaModule(lua);
  	//CharacterData toon = luaLotro.getCharacterFile().getInfosManager().getCurrentData();
  	//toon.getRace()
  	lua.getGlobal(LuaTools.PCALL_ERR_FUNC_NAME);
  	lua.pushValue(Lua51Consts.LUA_ENVIRONINDEX);
  	Attributes.pushAttributes(lua, -1, -2);
    return 1;
  }
  
  private static int getClassAttributes(Lua lua) {
    return 1;
  }
  
  private static int getAttributes(Lua lua) {
    return 1;
  }
  
  private static int getTrainedSkills(Lua lua) {
    return 1;
  }
  
  private static int getUntrainedSkills(Lua lua) {
    return 1;
  }

  private static int getWallet(Lua lua) {
    return 1;
  }

  private static int getEquipment(Lua lua) {
    return 1;
  }
  
  private static int getBackpack(Lua lua) {
    return 1;
  }
  
  private static int getVault(Lua lua) {
    return 1;
  }
  
  private static int getSharedStorage(Lua lua) {
    return 1;
  }
  
  private static int getMount(Lua lua) {
    return 1;
  }
  
  private static int getParty(Lua lua) {
    return 1;
  }
  
  private static int isInCombat(Lua lua) {
    return 1;
  }
}
