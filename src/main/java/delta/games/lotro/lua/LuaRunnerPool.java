package delta.games.lotro.lua;

import static org.squiddev.cobalt.ValueFactory.valueOf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.squiddev.cobalt.LuaError;

/**
 * LuaRunnerPool for LuaRunner.
 * 
 * @author MaxThlon
 */
public class LuaRunnerPool {
  
  private final List<LuaRunner> luaRunnerQueue = new ArrayList<LuaRunner>();

  
  void newRunner(String fileName) throws IOException, LuaError {
    InputStream script = new FileInputStream(fileName);

    LuaRunner luaJRunner = new LuaRunner(script, valueOf(fileName));

    luaRunnerQueue.add(luaJRunner);
  }
}
