package com.payjunction.flytrap;

public class ListMenu implements MenuOption
{
	private final TransactionDatabase transactionDatabase = new TransactionDatabase();

	@Override
	public String getMenuInput()
	{
		return "L";
	}

	@Override
	public String getHelpText()
	{
		return "The transactions for the current day are:\n";
	}

	@Override
	public void go()
	{
		transactionDatabase.listAllTransactions();
	}
}
