
_G.quicksort = function( t, start, endi )
	start = start or 1;
	endi  = endi or #t;
	
	--partition w.r.t. first element
	if ( endi - start < 2 ) then
		return t;
	end 

	local pivot = start;

	for i = start + 1, endi do
		if ( t[i] <= t[pivot] ) then
			local temp = t[pivot + 1];
			t[pivot + 1] = t[pivot];
			
			if(i == pivot + 1) then
				t[pivot] = temp;
			else
				t[pivot] = t[i];
				t[i] = temp;
			end
			
			pivot = pivot + 1;
		end
	end 
	
	t = quicksort( t, start, pivot - 1 );
	return quicksort( t, pivot + 1, endi );
end
