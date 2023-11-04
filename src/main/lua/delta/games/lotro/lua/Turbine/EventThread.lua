import "Turbine.Event"

return function(package)
  local function capture_args(...)
    return {...}, select("#", ...)
  end

  import(package)

  while true do
    local varargs, len = capture_args(coroutine.yield())
    Turbine.Event.Fire(varargs[1], unpack(varargs, 2, len-1))
  end
end
