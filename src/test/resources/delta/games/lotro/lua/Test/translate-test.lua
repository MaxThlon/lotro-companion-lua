import "metalua.loader"
import "Translate"
-- require('mobdebug').start()

local translate = Translate()
local fileName = URLToolsLua.getFromClassPath("test/translate/GlobalsEN.lua")

translate:Parse(fileName)

