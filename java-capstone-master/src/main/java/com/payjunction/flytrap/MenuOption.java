package com.payjunction.flytrap;

interface MenuOption
{
	String getMenuInput();

	default boolean shouldExit()
	{
		return false;
	}

	String getHelpText();

	void go();
}
