package com.payjunction.flytrap;

import java.util.Scanner;

public class CashRefundMenu implements MenuOption
{
	private final TransactionDatabase transactionDatabase = new TransactionDatabase();
	private final TransactionInfo transactionInfo;
	private final Scanner scanner;

	public CashRefundMenu(TransactionInfo transactionInfo, Scanner scanner)
	{
		this.transactionInfo = transactionInfo;
		this.scanner = scanner;
	}

	@Override
	public String getMenuInput()
	{
		return "X";
	}

	@Override
	public String getHelpText()
	{
		return "Enter the cash amount to be refunded:\n";
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
			transactionInfo.setAction("REFUND");
			transactionDatabase.appendDatabase(transactionInfo);
			System.out.println("Cash amount of " + chargeAmount + " refunded.");
		}
		else
		{
			System.out.println("Invalid input. Returned to main menu."
					+ "\nPlease enter a valid option:\n");
		}
	}
}
