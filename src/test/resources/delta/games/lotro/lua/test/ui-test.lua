import "Turbine.Type"
import "Turbine"
import "Turbine.UI"
import "Turbine.UI.Lotro"

TestWindow = class(Turbine.UI.Window)

function TestWindow:Constructor(title)
  Turbine.UI.Window.Constructor(self)
end;

-- Test Color --
-- local color = Turbine.UI.Color()
-- local color2 = Turbine.UI.Color( 0.0, 0.0, 1.0 )
-- local color3 = Turbine.UI.Color( 0.5, 0.0, 0.0, 1.0 )

-- Test Window --

-- local window = TestWindow()
-- window:Activate()

import "RadioButtonGroup"
import "DebugWindow"

debugWindow=DebugWindow()
debugWindow:SetVisible(true)
