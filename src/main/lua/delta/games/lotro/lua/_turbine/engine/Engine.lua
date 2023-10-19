import "Turbine.Enum";

local engineClass = class();

function engineClass:Constructor()
  
end

engineClass.GetCallStack = function()
  error("Not implemented");
  return nil;
end
  
engineClass.GetDate = function()
  error("Not implemented");
  return nil;
end

engineClass.GetGameTime = function()
  error("Not implemented");
  return nil;
end

engineClass.GetLanguage = function()
  error("Not implemented");
  return nil;
end

engineClass.GetLocale = function()
  error("Not implemented");
  return nil;
end

engineClass.GetLocalTime = function()
  error("Not implemented");
  return nil;
end

engineClass.GetScriptVersion = function()
  error("Not implemented");
  return nil;
end


return engineClass();
