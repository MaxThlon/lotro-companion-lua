package delta.common.framework.lua.command;

import delta.common.framework.module.command.ModuleCommand;
import delta.games.lotro.character.CharacterFile;

/**
 * @author MaxThlon
 */
public class LuaModuleCommandCaracterFile implements ModuleCommand {
	private CharacterFile _characterFile;
	
	/**
	 * @param characterFile .
	 */
	public LuaModuleCommandCaracterFile(CharacterFile characterFile) {
		_characterFile = characterFile;
	}
	
	/**
	 * @return characterFile.
	 */
	public CharacterFile getCharacterFile() {
		return _characterFile;
	}
}
