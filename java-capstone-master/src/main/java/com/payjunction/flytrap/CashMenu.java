package com.payjunction.flytrap;

import java.util.Scanner;

public class CashMenu implements MenuOption
{
	private final TransactionDatabase transactionDatabase = new TransactionDatabase();
	private final TransactionInfo transactionInfo;
	private final Scanner scanner;

	public CashMenu(TransactionInfo transactionInfo, Scanner scanner)
	{
		this.transactionInfo = transactionInfo;
		this.scanner = scanner;

	}

	@Override
	public String getMenuInput()
	{
		return "$";
	}

	@Override
	public String getHelpText()
	{
		return "Enter the transaction amount:\n";
	}

	@Override
	public void go()
	{
		String chargeAmount = scanner.nextLine();
		if (chargeAmount.matches("\\d{0,7}(?:\\.\\d{2})?")
				&& chargeAmount.matches(".*[1-9].*"))
		{
			transactionInfo.setTransactionAmount(chargeAmount);
			transactionInfo.setTransactionApproval("CASH");
			transactionInfo.setAction("CHARGE");
			transactionDatabase.appendDatabase(transactionInfo);
			System.out.println("Cash amount of " + chargeAmount + " accepted.");
		}
		else
		{
			System.out.println("Invalid input. Returned to main menu."
					+ "\nPlease enter a valid option:\n");
		}
	}
}
