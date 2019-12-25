package com.payjunction.flytrap;

import org.apache.http.NameValuePair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class VoidMenu implements MenuOption
{
	private final TransactionDatabase transactionDatabase = new TransactionDatabase();
	private final PaymentInformation paymentInformation;
	private final Scanner scanner = new Scanner(System.in);
	private final ApiService apiService;

	public VoidMenu(PaymentInformation paymentInformation, ApiService apiService)
	{
		this.paymentInformation = paymentInformation;
		this.apiService = apiService;
	}

	@Override
	public String getMenuInput()
	{
		return "V";
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
		TransactionInfo transactionInfo = new TransactionInfo();
		String input = scanner.nextLine();
		if (input.matches("^[0-9]*$"))
		{
			int userInput = Integer.parseInt(input);
			if (transactionDatabase.searchTransactionList(userInput) &&
					transactionDatabase.transactionIdInRange(userInput) &&
					transactionDatabase.voidTransactionIsCredit(userInput) &&
					!transactionDatabase.voidTransactionIsDeclined(userInput))
			{
				int transactionID = transactionDatabase.getTransactionID(userInput);
				List<NameValuePair> parameters = paymentInformation.voidUrlParameters();
				try
				{
					transactionInfo.setStatus(
							apiService.voidTransaction(parameters, transactionID));
							transactionDatabase.updateList(userInput);
				}
				catch (IOException | SQLException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("\nInvalid selection. "
						+ "TransactionID not found in transaction history,\n"
						+ "you are attempting to void a Declined transaction,\n"
						+ "or the transaction is a cash transaction. To void\n"
						+ "a cash transaction enter Z from the main menu.\n"
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
