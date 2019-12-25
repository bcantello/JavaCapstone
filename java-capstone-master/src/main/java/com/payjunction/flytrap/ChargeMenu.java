package com.payjunction.flytrap;

import org.apache.http.NameValuePair;

import java.util.List;
import java.util.Scanner;

public class ChargeMenu implements MenuOption
{

	private final TransactionDatabase transactionDatabase = new TransactionDatabase();
	private final Scanner scanner;
	private final PaymentInformation paymentInformation;
	private final ApiService api;
	private final TransactionIdParser transactionIdParser;
	private final ApprovalResponseService responseService;
	private final ConnectionClient client;

	public ChargeMenu(Scanner scanner,
			PaymentInformation paymentInformation, ApiService apiService,
			TransactionIdParser transactionIdParser, ApprovalResponseService responseService,
			ConnectionClient client)
	{
		this.scanner = scanner;
		this.paymentInformation = paymentInformation;
		this.api = apiService;
		this.transactionIdParser = transactionIdParser;
		this.responseService = responseService;
		this.client = client;
	}

	@Override
	public String getMenuInput()
	{
		return "C";
	}

	@Override
	public String getHelpText()
	{
		return "Enter the transaction amount:\n";
	}

	@Override
	public void go()
	{
		try
		{
			TransactionInfo transactionInfo = new TransactionInfo();
			TransactionActionService actionService =
					new TransactionActionService(transactionInfo, client);
			String chargeAmount = scanner.nextLine();
			if (chargeAmount.matches("\\d{0,7}(?:\\.\\d{2})?")
					&& chargeAmount.matches(".*[1-9].*"))
			{
				paymentInformation.setAmountBase(chargeAmount);
				transactionInfo.setTransactionAmount(chargeAmount);
				List<NameValuePair> parameters = paymentInformation.chargeUrlParameters();
				api.chargeCard(parameters);
				transactionInfo.setTransactionID(transactionIdParser.getTransactionID());
				String response = responseService.getApprovalResponse(
						transactionInfo.getTransactionID());
				transactionInfo.setTransactionApproval(response);
				actionService.getActionResponse(transactionInfo.getTransactionID());
				transactionDatabase.appendDatabase(transactionInfo);
				System.out.println(response);
			}
			else
			{
				System.out.println("Invalid input. Returned to main menu."
						+ "\nPlease enter a valid option:\n");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
