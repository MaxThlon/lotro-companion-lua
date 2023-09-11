import "Turbine.Type";
import "Turbine.Enum";

local turbineClass = class();

function turbineClass:Constructor()
--[[
  local ChatType = enum();
  ChatType.Undef = 0;
  ChatType.Error = 1;
  ChatType.Admin = 3;
  ChatType.Standard = 4;
  ChatType.Say = 5;
  ChatType.Tell = 6;
  ChatType.Emote = 7;

  self.Engine = require("delta/games/lotro/lua/turbine/engine/Engine");
--]]
end

_G.Turbine = turbineClass();