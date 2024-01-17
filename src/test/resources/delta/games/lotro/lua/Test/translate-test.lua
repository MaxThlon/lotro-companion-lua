require "metalua.loader"
require "Translate"
-- require('mobdebug').start()

local translate = Translate()
local fileName = "target\\test-classes\\delta\\games\\lotro\\lua\\translate\\GlobalsEN.lua"

translate:Parse(fileName)

