package com.payjunction.flytrap;

import org.apache.http.NameValuePair;

import java.util.List;
import java.util.Scanner;

public class RefundMenu implements MenuOption
{
	private final TransactionDatabase transactionDatabase = new TransactionDatabase();
	private final Scanner scanner;
	private final PaymentInformation paymentInformation;
	private final ApiService api;
	private final TransactionIdParser transactionIdParser;
	private final ApprovalResponseService responseService;

	public RefundMenu(Scanner scanner,
			PaymentInformation paymentInformation, ApiService apiService,
			TransactionIdParser transactionIdParser, ApprovalResponseService responseService)
	{
		this.scanner = scanner;
		this.paymentInformation = paymentInformation;
		this.api = apiService;
		this.transactionIdParser = transactionIdParser;
		this.responseService = responseService;
	}

	@Override
	public String getMenuInput()
	{
		return "R";
	}

	@Override
	public String getHelpText()
	{
		return "Enter the amount to be refunded:\n";
	}

	@Override
	public void go()
	{
		try
		{
			TransactionInfo transactionInfo = new TransactionInfo();
			String chargeAmount = scanner.nextLine();
			if (chargeAmount.matches("\\d{0,7}(?:\\.\\d{2})?")
					&& chargeAmount.matches(".*[1-9].*"))
			{
				paymentInformation.setAmountBase(chargeAmount);
				transactionInfo.setTransactionAmount(chargeAmount);
				List<NameValuePair> parameters = paymentInformation.refundUrlParameters();
				api.chargeCard(parameters);
				transactionInfo.setTransactionID(transactionIdParser.getTransactionID());
				String response = responseService.getApprovalResponse(
						transactionInfo.getTransactionID());
				transactionInfo.setTransactionApproval(response);
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
