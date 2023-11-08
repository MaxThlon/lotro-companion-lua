local function capture_args(...)
  return {...}, select("#", ...)
end

while true do
  local varargs, len = capture_args(coroutine.yield())
  
  if len ~= 0 then
    if len == 1 then
      Turbine.Event.Fire(varargs[1])
    else
      Turbine.Event.Fire(varargs[1], unpack(varargs, 2, len))
    end
  end
end
