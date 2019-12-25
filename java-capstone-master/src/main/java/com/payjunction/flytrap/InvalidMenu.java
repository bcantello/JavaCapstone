package com.payjunction.flytrap;

public class InvalidMenu implements MenuOption
{

	@Override
	public String getMenuInput()
	{
		return "impossible";
	}

	@Override
	public String getHelpText()
	{
		return "Invalid command. Please select another option";
	}

	@Override
	public void go()
	{
		// Do nothing
	}
}
