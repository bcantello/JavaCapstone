package com.payjunction.flytrap;

public class QuitMenu implements MenuOption
{
	@Override
	public String getMenuInput()
	{
		return "Q";
	}

	@Override
	public String getHelpText()
	{
		return "System exited";
	}

	@Override
	public boolean shouldExit()
	{
		return true;
	}

	@Override
	public void go()
	{
		//Do Nothing
	}
}
