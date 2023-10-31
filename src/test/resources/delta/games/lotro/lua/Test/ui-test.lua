import "Turbine"
import "Turbine.UI"
import "Turbine.UI.Lotro"

TestWindow = class(Turbine.UI.Window)

function TestWindow:Constructor()
  Turbine.UI.Window.Constructor(self)
  self:SetText("Debug Console");
end;

-- Test Color --
-- local color = Turbine.UI.Color()
-- local color2 = Turbine.UI.Color( 0.0, 0.0, 1.0 )
-- local color3 = Turbine.UI.Color( 0.5, 0.0, 0.0, 1.0 )

-- Test Window --

local window = TestWindow()
window:Activate()

import "Test.RadioButtonGroup"
import "Test.DebugWindow"

debugWindow=DebugWindow()
debugWindow:SetVisible(true)
