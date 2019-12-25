package com.payjunction.flytrap;

import java.sql.SQLException;
import java.util.Scanner;

public class CashVoidMenu implements MenuOption
{
	private final TransactionDatabase transactionDatabase = new TransactionDatabase();
	private final Scanner scanner = new Scanner(System.in);

	@Override
	public String getMenuInput()
	{
		return "Z";
	}

	@Override
	public String getHelpText()
	{
		transactionDatabase.listAllTransactions();
		return "Enter the transactionID for the transaction that you wish to void:\n";
	}

	@Override
	public void go()
	{
		String input = scanner.nextLine();
		if (input.matches("^[0-9]*$"))
		{
			int transactionID = Integer.parseInt(input);
			if (transactionDatabase.searchTransactionList(transactionID) &&
					transactionDatabase.transactionIdInRange(transactionID) &&
					!transactionDatabase.voidTransactionIsCredit(transactionID))
			{
				try
				{
					transactionDatabase.updateList(transactionID);
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("\nInvalid selection. "
						+ "TransactionID not found in transaction history,\n"
						+ "or the transaction is a CREDIT transaction. To void\n"
						+ "a CREDIT transaction please select V from the main menu.\n"
						+ "Returned to Main Menu\n");
			}
		}
		else
		{
			System.out.println("Invalid selection. TransactionID must only contain digits.\n"
					+ "Returned to Main Menu\n");
		}
	}
}
