package com.payjunction.flytrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

class ApprovalResponseService
{
	private final ConnectionClient client;

	ApprovalResponseService(ConnectionClient client)
	{
		this.client = client;
	}

	String getApprovalResponse(Integer transactionId) throws IOException
	{
		String approvalUrl = "https://api.payjunctionlabs.com/transactions/"
				+ transactionId;
		HttpGet get = new HttpGet(approvalUrl);
		ConnectionClient.Response response = client.execute(get);
		String approvalResponse = response.getBody();
		return parseApprovalResponse(approvalResponse) ? "Approved" : "Declined";
	}

	private boolean parseApprovalResponse(String approvalResponse) throws IOException
	{
		return new ObjectMapper()
				.readTree(approvalResponse)
				.get("response").get("approved").booleanValue();
	}

//	private String parseAction(String approvalResponse) throws IOException
//	{
//		return new ObjectMapper()
//				.readTree(approvalResponse)
//				.get("action").asText();
//	}
}
