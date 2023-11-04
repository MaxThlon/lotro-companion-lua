package delta.games.lotro.lua.turbine.object;

import javax.annotation.Nullable;
import javax.swing.JComponent;

import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.client.plugin.Plugin;
import delta.games.lotro.lua.turbine.ui.LuaControl;
import delta.games.lotro.lua.utils.LuaTools;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.lua51.Lua51Consts;
import party.iroiro.luajava.lua51.Lua51Natives;
import party.iroiro.luajava.value.LuaValue;

/**
 * @author MaxThlon
 */
public class LuaObject
{
	public static String REGISTRY_OBJECT_NAME = "__RegistryObjects";

	/**
   * Pushes onto the stack the RegistryObject table
	 * @param lua .
   */
  private static void getRegistryObject(Lua lua) {
    if (lua.getLuaNative() instanceof Lua51Natives) {
      new Lua51Natives() {
        {
          lua.getField(getRegistryIndex(), REGISTRY_OBJECT_NAME);
          if (lua.isNil(-1)) { /* No registry */
          	lua.pop(1);
          	lua.createTable(0, 0); /* Create one */
          	lua.createTable(0, 0); /* new metatable */
          	lua.push("k");
          	lua.setField(-2, "__mode"); /* with weak keys */
          	lua.setMetatable(-2);
          	lua.pushValue(-1); /* push duplicate new table for registry */
          	lua.setField(getRegistryIndex(), REGISTRY_OBJECT_NAME); /* Set it to registry */
          }
        }
      };
    }
  }
  
  public static Lua.LuaError openPackage(Lua lua) {
  	Lua.LuaError error;

  	if ((error = callInherit(lua, Lua51Consts.LUA_GLOBALSINDEX)) != Lua.LuaError.OK) return error;
  	LuaTools.setFunction(lua, -1, -3, "Constructor", LuaObject::constructor);
    lua.setField(-2, "Object");
    return error;
  }

  public static Lua.LuaError callInherit(Lua lua, int envIndex, @Nullable String... parentClassName) {
    Lua.LuaError error;
    lua.getGlobal("class");
    if (parentClassName.length != 0) {
    	if ((error = LuaTools.pushValue(lua, envIndex, parentClassName)) != Lua.LuaError.OK) {
    		return error;
    	}
    	error = lua.pCall(1, 1);
    } else {
    	error = lua.pCall(0, 1);
    }
    return error;
  }
  
  public static Object objectSelf(Lua lua, int index) {
  	Object objectSelf = null;

  	getRegistryObject(lua);
  	LuaTools.pushValueRelative(lua, index, -1);
    lua.getTable(-2);  /* RegistryObjects[Self] */
    objectSelf = lua.toJavaObject(-1);
    lua.pop(2); /* pop RegistryObject, objectSelf */

    return objectSelf;
  }

  public static <T> T objectSelf(Lua lua, int index, Class<T> clazz) {
  	 Object instance = objectSelf(lua, index);
     //if (!clazz.isAssignableFrom(instance.getClass())) throw typeError(value, c.getName());
     return clazz.cast(instance);
     
  }
  
  public static LuaValue findLuaObjectFromObject(Object objectSelf) {
    JComponent jComponent = LuaControl.findJComponentFromObject(objectSelf);
    if (jComponent != null) {
      return (LuaValue)jComponent.getClientProperty(LuaControl.jComponentKey_luaObjectSelf);
    } else if (objectSelf instanceof WindowController) {
      return ((WindowController)objectSelf).getContextProperty(LuaControl.jComponentKey_luaObjectSelf, LuaValue.class);
    } else if (objectSelf instanceof Plugin) {
      return ((Plugin)objectSelf)._context.getValue(LuaControl.jComponentKey_luaObjectSelf, LuaValue.class);
    }
    return null;
  }

  private static int constructor(Lua lua) {
    return 1;
  }

  public static int ObjectInheritedConstructor(Lua lua,
  																						 int indexSelf,
                                               Object objectSelf,
                                               LuaValue luaNewIndexMetaFunc,
                                               LuaValue luaIndexMetaFunc)
  {
  	if (objectSelf != null) {
    	getRegistryObject(lua);
    	LuaTools.pushValueRelative(lua, indexSelf, -1);
    	lua.pushJavaObject(objectSelf); /* objectSelf */
      lua.setTable(-3);  /* RegistryObjects[Self] = objectSelf */
      lua.pop(1); /* pop RegistryObject */
  	}

    if ((luaNewIndexMetaFunc != null) || (luaIndexMetaFunc != null)) {
    	lua.getMetatable(indexSelf);

    	if (luaNewIndexMetaFunc != null) {
    		lua.push("__newindex");
    		luaNewIndexMetaFunc.push();
    		lua.rawSet(-3);
    	}
    	
    	if (luaIndexMetaFunc != null) {
    		lua.push("__index");
    		luaIndexMetaFunc.push();
    		lua.rawSet(-3);
    	}
    	lua.pop(1); /* pop Metatable */
    }
    return 1;
  }
}
