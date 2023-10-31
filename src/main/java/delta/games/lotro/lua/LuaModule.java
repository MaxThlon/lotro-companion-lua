package delta.games.lotro.lua;

import java.nio.Buffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import delta.common.framework.module.Module;
import delta.common.framework.module.ModuleEvent;
import delta.games.lotro.lua.turbine.Turbine;
import delta.games.lotro.lua.utils.LuaTools;
import delta.games.lotro.lua.utils.URLToolsLua;
import party.iroiro.luajava.ExternalLoader;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.Lua.LuaType;
import party.iroiro.luajava.lua51.Lua51;
import party.iroiro.luajava.lua51.Lua51Consts;
import party.iroiro.luajava.value.LuaValue;

/**
 * LuaModule for lua scripts.
 * 
 * @author MaxThlon
 */
public class LuaModule implements Module {
	public enum LuaBootstrap {
		Lotro,
		LotroSandBox
	}

  //private static Logger LOGGER = Logger.getLogger(LuaModule.class);
  
  private UUID _uuid;
  private Lua _lua;
  private volatile boolean _isClosed = false;

  public LuaModule(UUID uuid, @Nullable ExternalLoader loader, Path... paths) {
  	_uuid = uuid;
    // Create an environment to run in
    _lua = new Lua51();
    // Reload native library to allow externals natives libraries
    /*if (_lua.getLuaNative() instanceof Lua51Natives) {
    	((Lua51Natives)_lua.getLuaNative()).loadAsGlobal();
    }*/
    
    if (loader != null) {
      _lua.setExternalLoader(loader);
    }
    _lua.openLibraries();
    LuaTools.setJavaModuleUuid(_lua, _uuid);
    initPackageLib(paths);
  }
  
  @Override
  public UUID getUuid() {
		return _uuid;
	}

	@Override
	public String getName() {
		return "LuaModule";
	}

  public Lua getLua() {
  	return _lua;
  }
  
  private void initPackageLib(Path... paths) {
    LuaValue packageLib = _lua.get("package");
    
    if (packageLib.type() == LuaType.TABLE) {
    	String FILE_SEP = System.getProperty("file.separator");

    	List<String> packagePaths = new ArrayList<String>();
    	packagePaths.addAll(Arrays.stream(paths).map(path -> path.toString()).collect(Collectors.toList()));
    	packagePaths.add(URLToolsLua.getFromClassPath(""));
    	packagePaths.add(URLToolsLua.getFromClassPath(Paths.get("luarocks", "lua").toString()));
    	packagePaths.add(URLToolsLua.getFromClassPath("thirdpart"));
      
    	/* Set path */
    	packageLib.set("path", _lua.from(
    			((String)packageLib.get("path").toJavaObject()) + ";" +
      		packagePaths.stream().flatMap(path -> Stream.of(
      				path + FILE_SEP + "?.lua",
      				path + FILE_SEP + "?" + FILE_SEP + "__init__.lua"
      		)).collect(Collectors.joining (";"))
      ));
      
    	/* Set cpath */
    	packageLib.set("cpath", _lua.from(
      		((String)packageLib.get("cpath").toJavaObject()) + ";" +
      		packagePaths.stream().map(path -> path + FILE_SEP + "?.dll")
      							  .collect(Collectors.joining (";"))
      ));

      packageLib.set("config", _lua.from(FILE_SEP + "\n;\n?\n!\n-\n"));
    }
  }

  private Lua.LuaError bootstrapLotro(Buffer buffer, String name) {
    Lua.LuaError error;

    String[] modules = LuaTools.libraryNameSplit(_lua, name, true);
    LuaTools.setEnvInfos(_lua, Lua51Consts.LUA_GLOBALSINDEX, "Globals");
    if ((error = LuaTools.openRootPackage(_lua)) != Lua.LuaError.OK) return error;
    if ((error = Turbine.openRootPackage(_lua)) != Lua.LuaError.OK) return error;
    LuaTools.pushGlobals(_lua, String.join(".", modules), modules);
    LuaTools.openPackage(_lua);
    if ((error = Turbine.openPackage(_lua)) != Lua.LuaError.OK) return error;
    if ((error = _lua.load(buffer, name)) != Lua.LuaError.OK) return error;
    LuaTools.pushfenv(_lua, -2, name, modules); /* push new env with env["_G"] = globals */
    LuaTools.setfenv(_lua, -2); /* set library globals */
    _lua.replace(-2); /* replace globals with loaded chunk */
    return error;
  }

  private Lua.LuaError bootstrapSandBoxedLotro() {
    Lua.LuaError error;

    Turbine.openRootPackage(_lua);
    error = _lua.load(
    		LuaTools.loadBuffer(Paths.get("Turbine", "turbine-thread.lua"), LuaModule.class),
        "turbine-thread"
    );
    return error;
  }

  @Override
  public boolean canAccept(ModuleEvent event) {
  	return event._sender == _uuid;
  }

  @Override
  public void handleEvent(ModuleEvent event) {
    Lua.LuaError error = null;

    if (_isClosed) throw new IllegalStateException("LuaRunner has been closed");

		switch (event._executorEvent) {
			case LOAD:
				switch((LuaBootstrap)event._args[0]) {
					case Lotro:
						error = bootstrapLotro((Buffer)event._args[1], (String)event._args[2]);
						break;
					case LotroSandBox:
						error = bootstrapSandBoxedLotro();
						break;
				}
				LuaTools.handleLuaResult(_lua, error);
        break;

			case EXECUTE:
				String eventName = (event != null)?event._name:"";
		    switch (eventName) {
		    	case "debug": {
		    		/*error = _lua.run("local luarocks = require('luarocks.luarocks') "
		    				+ "luarocks.cmd.run_command(luarocks.description, luarocks.commands, \"luarocks.cmd.external\", 'install', 'luasocket')");*/
		    		error = _lua.run("require('mobdebug').listen()");
		    		break;
		    	}
		    	default:
		    		error = _lua.pCall(0, 1);
		    }
		    LuaTools.handleLuaResult(_lua, error);
				break;

      case UNLOAD:
      case ABORT:
        if (!_isClosed) {
          _isClosed = true;
          _lua.close();
          _lua = null;
      	}
        break;
      case ERROR:
        break;
		}
  }
}
