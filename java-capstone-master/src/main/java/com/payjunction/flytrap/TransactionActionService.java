package com.payjunction.flytrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

class TransactionActionService
{
	private final TransactionInfo transactionInfo;
	private final ConnectionClient client;

	TransactionActionService(TransactionInfo transactionInfo, ConnectionClient client)
	{
		this.transactionInfo = transactionInfo;
		this.client = client;
	}

	void getActionResponse(Integer transactionId) throws IOException
	{
		String approvalUrl = "https://api.payjunctionlabs.com/transactions/"
				+ transactionId;
		HttpGet get = new HttpGet(approvalUrl);
		ConnectionClient.Response response = client.execute(get);
		String approvalResponse = response.getBody();
		String action = parseAction(approvalResponse);
		transactionInfo.setAction(action);
	}

	private String parseAction(String approvalResponse) throws IOException
	{
		return new ObjectMapper()
				.readTree(approvalResponse)
				.get("action").asText();
	}
}
