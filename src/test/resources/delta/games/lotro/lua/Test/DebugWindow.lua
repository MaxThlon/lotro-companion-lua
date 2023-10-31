-- creates an interactive debugging window
DebugWindow = class(Turbine.UI.Lotro.Window);
debugBackColor=Turbine.UI.Color(.1,.1,.1);
debugFont=Turbine.UI.Lotro.Font.Verdana12;
if backColor==nil then backColor=Turbine.UI.Color(0,0,0) end
if fontColor==nil then fontColor=Turbine.UI.Color(1,1,1) end
if trimColor==nil then trimColor=Turbine.UI.Color(.5,.5,.5) end
function DebugWindow:Constructor()
	Turbine.UI.Lotro.Window.Constructor( self );
	self:SetText("Debug Console");
	-- self:SetVisible(false);
	self:SetSize(640,580);
	self:SetPosition((Turbine.UI.Display:GetWidth() - self:GetWidth()) / 2,(Turbine.UI.Display:GetHeight() - self:GetHeight()) / 2);
	self:SetBackColor(debugBackColor);

	self.CommandPanel=Turbine.UI.Control();
	self.CommandPanel:SetParent(self);
	self.CommandPanel:SetSize(self:GetWidth()-30,100);
	self.CommandPanel:SetPosition(15,40);
	self.CommandPanel:SetBackColor(debugBackColor);

	self.CommandCaption=Turbine.UI.Label();
	self.CommandCaption:SetParent(self.CommandPanel);
--	self.CommandCaption:SetSize(self.CommandPanel:GetWidth()-2,20);
	self.CommandCaption:SetSize(100,20);
	self.CommandCaption:SetPosition(1,1);
	self.CommandCaption:SetFont(debugFont);
	self.CommandCaption:SetText("Command:");

	self.CommandEnvironment=RadioButtonGroup();
	self.CommandEnvironment:SetParent(self.CommandPanel);
	self.CommandEnvironment:SetPosition(self.CommandCaption:GetLeft()+self.CommandCaption:GetWidth()+15,self.CommandCaption:GetTop()-2);
	self.CommandEnvironment:SetSize(self.CommandPanel:GetWidth()-self.CommandEnvironment:GetLeft()-3,20);
	self.CommandEnvironment.UnselectedIcon="GaranStuff/Cards/Resources/RB_unselected.tga";
	self.CommandEnvironment.SelectedIcon="GaranStuff/Cards/Resources/RB_selected.tga";
	self.CommandEnvironment.IconWidth=16;
	self.CommandEnvironment.IconHeight=16;
	self.CommandEnvironment:SetBorderColor(backColor);
	self.CommandEnvironment:SetBackColor(backColor);
	self.CommandEnvironment:SetTextColor(fontColor);
	self.CommandEnvironment:AddChoice("Global Environment");
	self.CommandEnvironment:AddChoice("Plugin Environment");
	self.CommandEnvironment:SetRows(1);
	self.CommandEnvironment:SetSelectedChoice(1);

	self.CommandText=Turbine.UI.Lotro.TextBox();
	self.CommandText:SetParent(self.CommandPanel);
	self.CommandText:SetSize(self.CommandPanel:GetWidth()-2,self.CommandPanel:GetHeight()-62);
	self.CommandText:SetPosition(1,31);
	self.CommandText:SetFont(debugFont);

	self.CommandExec=Turbine.UI.Lotro.Button();
	self.CommandExec:SetParent(self.CommandPanel);
	self.CommandExec:SetSize(100,20);
	self.CommandExec:SetText("Execute");
	self.CommandExec:SetPosition((self.CommandPanel:GetWidth() - self.CommandExec:GetWidth()) / 2, self.CommandPanel:GetHeight()-21);
	self.CommandExec.Click=function()
		-- execute the text in the command window
		local userFunc, success, retval, error;

		userFunc,error=loadstring(self.CommandText:GetText());
		if userFunc==nil then
			Turbine.Shell.WriteLine("Error:"..tostring(error));
		else
			if self.CommandEnvironment:GetSelectedChoice()==1 then
				setfenv(userFunc,getfenv(0)); -- we set the environment to the global environment
			else
				setfenv(userFunc,getfenv()); -- we set the environment to the current plugin environment
			end
			success,retval=pcall(userFunc);
			if success then
				-- if table, yada yada yada
				if type(retval)=="table" then
					Table.Dump(retval)
				else
					Turbine.Shell.WriteLine(tostring(retval));
				end
			else
				Turbine.Shell.WriteLine("Error:"..tostring(retval))
			end

		end
	end

	self.WatchPanel=Turbine.UI.Control();
	self.WatchPanel:SetParent(self);
	self.WatchPanel:SetPosition(15,self.CommandPanel:GetTop()+self.CommandPanel:GetHeight()+10);
	self.WatchPanel:SetSize(self:GetWidth()-30,200);
	self.WatchPanel:SetBackColor(debugBackColor);

	self.WatchCaption=Turbine.UI.Label();
	self.WatchCaption:SetParent(self.WatchPanel);
	self.WatchCaption:SetSize(180,20);
	self.WatchCaption:SetPosition(1,1);
	self.WatchCaption:SetFont(debugFont);
	self.WatchCaption:SetText("Var/Prop:");

	self.WatchText=Turbine.UI.Lotro.TextBox();
	self.WatchText:SetParent(self.WatchPanel);
	self.WatchText:SetSize(180,20)
	self.WatchText:SetPosition(1,25);
	self.WatchText:SetFont(debugFont);

	self.WatchAdd=Turbine.UI.Lotro.Button();
	self.WatchAdd:SetParent(self.WatchPanel);
	self.WatchAdd:SetSize(180,20);
	self.WatchAdd:SetPosition(1,55);
	self.WatchAdd:SetText("Add");
	self.WatchAdd.Click=function()
		-- attempt to resolve the watchtext
		local success, retval, error;

		result,error=loadstring("return "..self.WatchText:GetText());
		if result==nil then
			Turbine.Shell.WriteLine("Error:"..tostring(error));
		else
			setfenv(result,getfenv());
			success,retval=pcall(result);
			if success then
				-- if watchtext resolves without error, then add a watch list entry for it
				local tmpWatch=Turbine.UI.Label()
				tmpWatch:SetParent(self.WatchList);
				tmpWatch:SetSize(self.WatchList:GetWidth(),20);
				tmpWatch.Variable=self.WatchText:GetText();
				tmpWatch:SetFont(debugFont);
				tmpWatch:SetText(tmpWatch.Variable.."="..tostring((retval)));
				self.WatchList:AddItem(tmpWatch);
			else
				Turbine.Shell.WriteLine("Error:"..tostring(retval))
			end

		end
	end

	self.WatchRemove=Turbine.UI.Lotro.Button();
	self.WatchRemove:SetParent(self.WatchPanel);
	self.WatchRemove:SetSize(180,20);
	self.WatchRemove:SetPosition(1,85);
	self.WatchRemove:SetText("Remove");
	self.WatchRemove.Click=function()
		if self.WatchList:GetSelectedIndex()~=nil and self.WatchList:GetSelectedIndex()>0 then
			-- remove the currently selected watch list item
			self.WatchList:RemoveItemAt(self.WatchList:GetSelectedIndex());
		end
		self.WatchList:SelectedIndexChanged();
	end

	self.WatchList=Turbine.UI.ListBox();
	self.WatchList:SetParent(self.WatchPanel);
	self.WatchList:SetSize(400, self.WatchPanel:GetHeight()-2);
	self.WatchList:SetPosition(190, 1);
	self.WatchList.OldSelectedIndex=nil;
	self.WatchList.SelectedIndexChanged=function()
		if self.WatchList.OldSelectedIndex~=nil and self.WatchList.OldSelectedIndex>0 and self.WatchList.OldSelectedIndex<=self.WatchList:GetItemCount() then
			self.WatchList:GetItem(self.WatchList.OldSelectedIndex):SetBackColor(self:GetBackColor());
		end
		if self.WatchList:GetSelectedIndex()~=nil and self.WatchList:GetSelectedIndex()>0 and self.WatchList:GetSelectedIndex()<=self.WatchList:GetItemCount() then
			self.WatchList:GetItem(self.WatchList:GetSelectedIndex()):SetBackColor(Turbine.UI.Color(.2,.2,.6));
		end
		self.WatchList.OldSelectedIndex=self.WatchList:GetSelectedIndex();
	end

	self.WatchVScroll=Turbine.UI.Lotro.ScrollBar();
	self.WatchVScroll:SetOrientation(Turbine.UI.Orientation.Vertical);
	self.WatchVScroll:SetParent(self.WatchPanel);
	self.WatchVScroll:SetPosition(self.WatchList:GetLeft()+self.WatchList:GetWidth(),self.WatchList:GetTop());
	self.WatchVScroll:SetWidth(12);
	self.WatchVScroll:SetHeight(self.WatchList:GetHeight());
	self.WatchList:SetVerticalScrollBar(self.WatchVScroll);
	self.WatchVScroll:SetVisible(false);

	self.InspectionPanel=Turbine.UI.ScrollableControl(); -- needs to be a "scrollable" control
	self.InspectionPanel:SetParent(self);
	self.InspectionPanel:SetPosition(15,self.WatchPanel:GetTop()+self.WatchPanel:GetHeight()+10);
	self.InspectionPanel:SetSize(self:GetWidth()-30,self:GetHeight()-self.InspectionPanel:GetTop()-30);
	self.InspectionPanel:SetBackColor(debugBackColor);
	
	InspectionPanelControl=Turbine.UI.Control();
  InspectionPanelControl:SetParent(self.InspectionPanel);
  InspectionPanelControl:SetSize(self.InspectionPanel:GetSize());
  InspectionPanelControl:SetBackColor(debugBackColor);
  

	self.InspectionTree=Turbine.UI.TreeView();
	self.InspectionTree:SetParent(self.InspectionPanel);
	self.InspectionTree:SetSize(self.InspectionPanel:GetWidth()/2-14,self.InspectionPanel:GetHeight()-14);
	self.InspectionTree:SetPosition(1,1);
	self.InspectionTree:SetIndentationWidth(0); -- I manually control the indentation so that rows don't miss the mouse events in the indentation area
	self.InspectionTree.SelectedNode=nil;
	self.InspectionTree.LastSelectedNode=nil;
	self.InspectionTree.SelectedChanged=false;

	self.InspectionVScroll=Turbine.UI.Lotro.ScrollBar();
	self.InspectionVScroll:SetOrientation(Turbine.UI.Orientation.Vertical);
	self.InspectionVScroll:SetParent(self.InspectionPanel);
	self.InspectionVScroll:SetPosition(self.InspectionTree:GetLeft()+self.InspectionTree:GetWidth(),self.InspectionTree:GetTop());
	self.InspectionVScroll:SetWidth(12);
	self.InspectionVScroll:SetHeight(self.InspectionPanel:GetHeight());
	self.InspectionTree:SetVerticalScrollBar(self.InspectionVScroll);
	self.InspectionVScroll:SetVisible(false);

	self.InspectionHScroll=Turbine.UI.Lotro.ScrollBar();
	self.InspectionHScroll:SetOrientation(Turbine.UI.Orientation.Horizontal);
	self.InspectionHScroll:SetParent(self.InspectionPanel);
	self.InspectionHScroll:SetPosition(self.InspectionTree:GetLeft(),self.InspectionTree:GetHeight()+self.InspectionTree:GetTop());
	self.InspectionHScroll:SetHeight(12);
	self.InspectionHScroll:SetWidth(self.InspectionTree:GetWidth());
	self.InspectionTree:SetHorizontalScrollBar(self.InspectionHScroll);
	self.InspectionHScroll:SetVisible(false);

	self.InspectionTree.SelectedNodeChanged=function()
		self.InspectionTree.SelectedChanged=true;
		if self.InspectionTree.LastSelectedNode~=nil then
			self.InspectionTree.LastSelectedNode.Text:SetBackColor(debugBackColor);
		end
		self.InspectionTree.LastSelectedNode=self.InspectionTree.SelectedNode;
		Turbine.Shell.WriteLine("self.InspectionTree.SelectedNode=:"..tostring(self.InspectionTree.SelectedNode))
		if self.InspectionTree.SelectedNode~=nil then
		  Turbine.Shell.WriteLine("how do we get the fully qualified name?")
			-- how do we get the fully qualified name?
			local tmpNode=self.InspectionTree.SelectedNode
			local tmpName=self.InspectionTree.SelectedNode.Text:GetText();
			local objType=type(self.InspectionTree.SelectedNode.Object);
			local isFunction=false;
			if objType=="function" then
				tmpName=tmpName.."()";
				isFunction=true;
			else
				if tonumber(tmpName)~=nil then 
					tmpName="["..tmpName.."]";
				end
			end
			if tmpNode.Tier>0 then
				tmpNode=tmpNode:GetParentNode();
				while tmpNode.Tier>0 do
					if tonumber(tmpNode.Text:GetText())~=nil then
						if isFunction then
							tmpName="["..tmpNode.Text:GetText().."]:"..tmpName;
							isFunction=false;
						else
							tmpName="["..tmpNode.Text:GetText().."]"..tmpName;
						end
					else
						if string.sub(tmpName,1,1)=="[" then
							tmpName=tmpNode.Text:GetText()..tmpName;
						else
							if isFunction then
								tmpName=tmpNode.Text:GetText()..":"..tmpName;
								isFunction=false;
							else
								tmpName=tmpNode.Text:GetText().."."..tmpName;
							end
						end
					end
					tmpNode=tmpNode:GetParentNode();
				end
			end
			self.InspectionName:SetText(tmpName);
			self.WatchText:SetText(tmpName);

			self.InspectionTree.SelectedNode.Text:SetBackColor(Turbine.UI.Color(.2,.2,.6));
			-- need to fill in the detail pane
			self.InspectionType:SetText(objType)
			if self.InspectionTree.SelectedNode.Object==nil then
				self.InspectionVal:SetText("nil");
			elseif objType=="number" then
				self.InspectionVal:SetText(tostring(self.InspectionTree.SelectedNode.Object).." ("..string.format("0x%x",tostring(self.InspectionTree.SelectedNode.Object))..")");
			elseif objType=="string" or objType=="boolean" then
				self.InspectionVal:SetText(tostring(self.InspectionTree.SelectedNode.Object));
			else
				self.InspectionVal:SetText("n/a");
			end
		end
	end

	self.InspectionName=Turbine.UI.Label();
	self.InspectionName:SetParent(self.InspectionPanel);
	self.InspectionName:SetSize(self.InspectionPanel:GetWidth()-self.InspectionTree:GetLeft()-self.InspectionTree:GetWidth()-14,20);
	self.InspectionName:SetPosition(self.InspectionTree:GetLeft()+self.InspectionTree:GetWidth()+14,1);
	self.InspectionName:SetFont(debugFont);
	self.InspectionName:SetText("");

	self.InspectionTypeCaption=Turbine.UI.Label();
	self.InspectionTypeCaption:SetParent(self.InspectionPanel);
	self.InspectionTypeCaption:SetSize(80,20);
	self.InspectionTypeCaption:SetPosition(self.InspectionTree:GetWidth()+14,23);
	self.InspectionTypeCaption:SetFont(debugFont);
	self.InspectionTypeCaption:SetText("Type:");

	self.InspectionType=Turbine.UI.Label();
	self.InspectionType:SetParent(self.InspectionPanel);
	self.InspectionType:SetSize(self.InspectionPanel:GetWidth()-self.InspectionType:GetLeft()-self.InspectionType:GetWidth()-2,20);
	self.InspectionType:SetPosition(self.InspectionTypeCaption:GetLeft()+self.InspectionTypeCaption:GetWidth()+1,self.InspectionTypeCaption:GetTop());
	self.InspectionType:SetFont(debugFont);
	self.InspectionType:SetText("");

	self.InspectionValCaption=Turbine.UI.Label();
	self.InspectionValCaption:SetParent(self.InspectionPanel);
	self.InspectionValCaption:SetSize(self.InspectionTypeCaption:GetWidth(),20);
	self.InspectionValCaption:SetPosition(self.InspectionTypeCaption:GetLeft(),45);
	self.InspectionValCaption:SetFont(debugFont);
	self.InspectionValCaption:SetText("Value:");

	self.InspectionVal=Turbine.UI.Label();
	self.InspectionVal:SetParent(self.InspectionPanel);
	self.InspectionVal:SetSize(self.InspectionType:GetWidth(),20);
	self.InspectionVal:SetPosition(self.InspectionType:GetLeft(),self.InspectionValCaption:GetTop());
	self.InspectionVal:SetFont(debugFont);
	self.InspectionVal:SetText("");

	function AddNode(object,parentNodeList)
		-- for efficiency sake, child nodes are only built when they are actually exposed.
		local node=Turbine.UI.TreeNode();
		--node:SetParent(self.InspectionTree)
		node:SetSize(self.InspectionTree:GetWidth(),20)
		node.Tier=0;
		node.Object=object;
		node.Icon=Turbine.UI.Control();
		node.Icon:SetParent(node);
		node.Icon:SetSize(16,16);
		node.Icon:SetPosition(0,2);
		node.Icon:SetBlendMode(Turbine.UI.BlendMode.Overlay);
		node.Icon:SetMouseVisible(false);

		node.Text=Turbine.UI.Label();
		node.Text:SetParent(node);
		node.Text:SetFont(debugFont);
		node.Text:SetSize(node:GetSize());
		node.Text:SetPosition(18,0)
		node.Text:SetText("_G");
		node.Text:SetMouseVisible(false);

		node.Text.MouseDown=function(sender)
		  Turbine.Shell.WriteLine("SelectedNode=:"..tostring(node))
			self.InspectionTree.SelectedNode=node;
		end

		parentNodeList:Add(node)

		if type(node.Object)=="table" then
			node.Icon:SetBackground(0x41007e27);
			node.Icon:SetVisible(true);
			-- we create one bogus node so that the Turbine tree control will work
			local tmpNode=Turbine.UI.TreeNode();
			tmpNode:SetParent(self.InspectionTree)
			tmpNode:SetSize(self.InspectionTree:GetWidth(),20)
			tmpNode:SetBackColor(trimColor)
			tmpNode.MouseDown=function(sender)
			  Turbine.Shell.WriteLine("SelectedNode=:"..tostring(sender))
				self.InspectionTree.SelectedNode=sender;
			end
			node:GetChildNodes():Add(tmpNode);

			tmpNode=Turbine.UI.TreeNode();
			tmpNode:SetParent(self.InspectionTree)
			tmpNode:SetSize(self.InspectionTree:GetWidth(),20)
			tmpNode:SetBackColor(fontColor)
			tmpNode.MouseDown=function(sender)
			  Turbine.Shell.WriteLine("SelectedNode=:"..tostring(sender))
				self.InspectionTree.SelectedNode=sender;
			end
			node:GetChildNodes():Add(tmpNode);
		else
			node.Icon:SetBackground(0x410001a4);
			node.Icon:SetVisible(false);
		end
	end

	function AddChildNodes(node)
		if node~=nil and self.InspectionTree.SelectedChanged and self.InspectionTree.SelectedNode:IsExpanded() then
			self.InspectionTree.SelectedChanged=false;
			local object=node.Object;
			local tmpNode;
			local childNodes=node:GetChildNodes();
			for tmpNode=1,childNodes:GetCount() do
				childNodes:Get(tmpNode).MouseDown=nil;
			end
			childNodes:Clear();
			local tmpChildObj={}
			if type(node.Object)=="table" then
				-- refresh the child nodes
				for k,v in pairs(node.Object) do
					tmpChildObj[#tmpChildObj+1]={k,v};
				end
				-- check if there is a metatable for this
				local tmpMetaTable=getmetatable(node.Object);
				if tmpMetaTable~=nil then
					for k,v in pairs(tmpMetaTable) do
						tmpChildObj[#tmpChildObj+1]={k,v};
					end

				end
				table.sort(tmpChildObj,function(arg1,arg2) if arg1[1]<arg2[1] then return(true) end end);

				for tmpIndex=1,#tmpChildObj do
					local newNode;
					newNode=Turbine.UI.TreeNode();
					newNode:SetParent(self.InspectionTree);
					newNode.Tier=node.Tier+1;
					newNode:SetSize(self.InspectionTree:GetWidth()+16*newNode.Tier,20);
					newNode.Object=tmpChildObj[tmpIndex][2];
					newNode.Icon=Turbine.UI.Control();
					newNode.Icon:SetParent(newNode);
					newNode.Icon:SetSize(16,16);
					newNode.Icon:SetPosition(16*newNode.Tier,2);
					newNode.Icon:SetBlendMode(Turbine.UI.BlendMode.Overlay);
					newNode.Icon:SetMouseVisible(false);

					newNode.Text=Turbine.UI.Label();
					newNode.Text:SetParent(newNode);
					newNode.Text:SetFont(debugFont);
					newNode.Text:SetSize(newNode:GetSize());
					newNode.Text:SetPosition(18+16*newNode.Tier,0)
					newNode.Text:SetText(tostring(tmpChildObj[tmpIndex][1]));
					newNode.Text:SetMouseVisible(false);
					childNodes:Add(newNode);
					newNode.MouseDown=function(sender)
					  Turbine.Shell.WriteLine("SelectedNode=:"..tostring(sender))
						self.InspectionTree.SelectedNode=sender;
					end
					if type(newNode.Object)=="table" then
						newNode.Icon:SetBackground(0x41007e27);
						newNode:GetChildNodes():Add(Turbine.UI.TreeNode());
						newNode.Icon:SetVisible(true);
					else
						newNode.Icon:SetBackground(0x410001a4);
						newNode.Icon:SetVisible(false);
					end
				end
			else
				-- check if there is a metatable for this
				local tmpMetaTable=getmetatable(node.Object);
				if tmpMetaTable~=nil then
					for k,v in pairs(tmpMetaTable) do
						tmpChildObj[#tmpChildObj+1]={k,v};
					end

				end
				if #tmpChildObj>0 then
					table.sort(tmpChildObj,function(arg1,arg2) if arg1[1]<arg2[1] then return(true) end end);
					for tmpIndex=1,#tmpChildObj do
						local newNode;
						newNode=Turbine.UI.TreeNode();
						newNode:SetParent(self.InspectionTree);
						newNode.Tier=node.Tier+1;
						newNode:SetSize(self.InspectionTree:GetWidth()+16*newNode.Tier,20);
						newNode.Object=tmpChildObj[tmpIndex][2];
						newNode.Icon=Turbine.UI.Control();
						newNode.Icon:SetParent(newNode);
						newNode.Icon:SetSize(16,16);
						newNode.Icon:SetPosition(16*newNode.Tier,2);
						newNode.Icon:SetBlendMode(Turbine.UI.BlendMode.Overlay);
						newNode.Icon:SetMouseVisible(false);

						newNode.Text=Turbine.UI.Label();
						newNode.Text:SetParent(newNode);
						newNode.Text:SetFont(debugFont);
						newNode.Text:SetSize(newNode:GetSize());
						newNode.Text:SetPosition(18+16*newNode.Tier,0)
						newNode.Text:SetText(tostring(tmpChildObj[tmpIndex][1]));
						newNode.Text:SetMouseVisible(false);
						childNodes:Add(newNode);
						newNode.MouseDown=function(sender)
						  Turbine.Shell.WriteLine("SelectedNode=:"..tostring(sender))
							self.InspectionTree.SelectedNode=sender;
						end
						if type(newNode.Object)=="table" then
							newNode.Icon:SetBackground(0x41007e27);
							newNode:GetChildNodes():Add(Turbine.UI.TreeNode());
							newNode.Icon:SetVisible(true);
						else
							newNode.Icon:SetBackground(0x410001a4);
							newNode.Icon:SetVisible(false);
						end
					end
				else
					node.Icon:SetBackground(0x410001a4);
					node.Icon:SetVisible(false);
				end
			end
			self.InspectionTree.SelectedNode:SetExpanded(true);
		end
	end

	-- add the root global environment node
	AddNode(_G,self.InspectionTree:GetNodes())

	self.Update=function()
		-- attempt to resolve the watchtext
		local success, retval, error, tmpIndex;
		for tmpIndex=1,self.WatchList:GetItemCount() do
			result,error=loadstring("return "..self.WatchList:GetItem(tmpIndex).Variable);
			if result~=nil then
				setfenv(result,getfenv());
				success,retval=pcall(result);
				if success then
					-- if watchtext resolves without error, then add a watch list entry for it
					self.WatchList:GetItem(tmpIndex):SetText(self.WatchList:GetItem(tmpIndex).Variable.."="..tostring((retval)));
				else
					Turbine.Shell.WriteLine("Error:"..tostring(retval))
				end

			end
		end
		-- refresh the child nodes for the current node if it is expanded
		if self.InspectionTree.SelectedNode~=nil then
			AddChildNodes(self.InspectionTree.SelectedNode)
			if self.InspectionTree.SelectedNode:GetChildNodes():GetCount()>0 then
				if self.InspectionTree.SelectedNode:IsExpanded() then
					self.InspectionTree.SelectedNode.Icon:SetBackground(0x41007e26);
				else
					self.InspectionTree.SelectedNode.Icon:SetBackground(0x41007e27);
				end
				self.InspectionTree.SelectedNode.Icon:SetVisible(true);
			else
				self.InspectionTree.SelectedNode.Icon:SetBackground(0x410001a4);
				self.InspectionTree.SelectedNode.Icon:SetVisible(false);
			end
		end
	end
	self.VisibleChanged=function()
		self:SetWantsUpdates(self:IsVisible());
	end

end
