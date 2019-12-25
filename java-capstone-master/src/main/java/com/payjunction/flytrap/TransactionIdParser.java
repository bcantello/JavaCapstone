package com.payjunction.flytrap;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Scanner;

class TransactionIdParser
{
	private final Scanner scanner = new Scanner(System.in);
	private final TerminalResponse terminalResponse;
	private final ApiService apiService;

	TransactionIdParser(TerminalResponse terminalResponse,
			ApiService apiService)
	{
		this.terminalResponse = terminalResponse;
		this.apiService = apiService;
	}

	public Integer getTransactionID() throws IOException, InterruptedException
	{
		String statusResponse;
		do
		{
			apiService.getStatusResponse();
			statusResponse = terminalResponse.getStatusResponse();
			Thread.sleep(250);
		}
		while (!statusResponse.contains("COMPLETE"));
		return transactionIdParser(statusResponse);
	}

	private Integer transactionIdParser(String statusResponse) throws IOException
	{
		if (new ObjectMapper().readTree(statusResponse).size() == 2)
		{
			return new ObjectMapper()
					.readTree(statusResponse)
					.get("transactionId").asInt();
		}
		else
		{
			System.out.println("\nTransaction has been canceled.\n");
//			scanner.next();
			try
			{
				POS pos = new POS();
				pos.menu();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}
}
