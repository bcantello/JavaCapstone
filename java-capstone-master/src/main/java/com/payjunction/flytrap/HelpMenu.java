package com.payjunction.flytrap;

public class HelpMenu implements MenuOption
{
	@Override
	public String getMenuInput()
	{
		return "H";
	}

	@Override
	public String getHelpText()
	{
		return "Select an option:\n"
				+ "\tTo accept a credit card enter: C\n"
				+ "\tTo accept cash enter: $\n"
				+ "\tTo refund a credit transaction enter: R\n"
				+ "\tTo refund a cash transaction enter: X\n"
				+ "\tTo void a credit transaction enter: V\n"
				+ "\tTo void a cash transaction enter: Z\n"
				+ "\tTo list all transactions for the current day enter: L\n"
				+ "\tTo exit the POS enter: Q\n"
				+ "\tYou may also enter 'H' at any time to see the available options.\n";
	}

	@Override
	public void go()
	{

	}
}
