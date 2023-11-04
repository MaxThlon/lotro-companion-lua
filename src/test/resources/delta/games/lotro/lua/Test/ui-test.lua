import "Turbine"
import "Turbine.Type"
import "Turbine.UI"
import "Turbine.UI.Lotro"
import "UI.Swing"

-- local luarocks = require "luarocks.luarocks"

-- TestWindow = class(Turbine.UI.Window)

-- function TestWindow:Constructor()
--   Turbine.UI.Window.Constructor(self)
--   self:SetText("Debug Console");
-- end;

-- Test Color --
-- local color = Turbine.UI.Color()
-- local color2 = Turbine.UI.Color( 0.0, 0.0, 1.0 )
-- local color3 = Turbine.UI.Color( 0.5, 0.0, 0.0, 1.0 )

-- Test Window --
-- luarocks.cmd.run_command(luarocks.description, luarocks.commands, "luarocks.cmd.external", "install", "luasocket")

-- local window = TestWindow()
-- window:Activate()

import "Test.RadioButtonGroup"
import "Test.DebugWindow"
local inspect = import "inspect"

debugWindow = DebugWindow()
UI.Swing.Component.SetName(debugWindow, "debugWindow");
debugWindow:Activate()

local SystemInfo = class()

function SystemInfo:Constructor()
  self._systemInfos = {}
end

function SystemInfo:packType(object)
  local currentType = Turbine.Type.StaticGetType(object)

  if (currentType.typeInfo ~= nil) then
    local data = currentType.type
    if (currentType.typeInfo.Class == true) then
      data = {}

      for key, value in pairs(currentType.type) do
        data[key] = Turbine.Type.StaticGetType(value).type
      end
    end
    return { currentType.typeInfo, data }
  end
  return currentType.type
end

function SystemInfo:parseSystemValue(systemValue)
  local localSystemInfos = {}
  for key, value in pairs(systemValue) do
    -- print("[" .. key .. "]: " .. self:packType(value))
    localSystemInfos[key] = self:packType(value)
  end

  return localSystemInfos
end

function SystemInfo:parseSystem()
  self._systemInfos["Turbine"] = self:parseSystemValue(_G["Turbine"])
end

local systemInfo = SystemInfo()
systemInfo:parseSystem()


print(inspect.inspect(systemInfo._systemInfos))

