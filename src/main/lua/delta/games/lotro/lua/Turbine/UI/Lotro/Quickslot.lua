
local oldSetShortcut = Quickslot.SetShortcut;

-- This fix solves a problem that occurs when setting the shortcut
-- of quickslots. The setting causes the quickslot to become
-- invisible which is incorrect behavior. The fix will will maintain
-- the previous visibility state.
function Quickslot:SetShortcut( value )
	local isVisible = self:IsVisible();

	local success, result = pcall(oldSetShortcut, self, value);

	if success then
		self:SetVisible( value );
	else
		return nil
	end
end
