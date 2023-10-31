import "Turbine"
import "Turbine.UI"
import "Turbine.UI.Lotro"

while (true) do
  local event = coroutine.yield()
  processEvents(event)
end
