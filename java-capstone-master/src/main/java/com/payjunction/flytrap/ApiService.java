package com.payjunction.flytrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import java.io.IOException;
import java.util.List;

class ApiService
{
	private final ConnectionClient client;
	private final CoreTerminalInfo setup;
	private final TerminalResponse terminalResponse;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public ApiService(ConnectionClient client, CoreTerminalInfo setup,
			TerminalResponse terminalResponse)
	{
		this.client = client;
		this.setup = setup;
		this.terminalResponse = terminalResponse;
	}

	public String chargeCard(List<NameValuePair> parameters) throws IOException
	{
		String url = setup.getPjURL() + setup.getRequestPayment();
		HttpPost post = new HttpPost(url);

		post.setEntity(new UrlEncodedFormEntity(parameters));
		ConnectionClient.Response result = client.execute(post);

		String chargeResponse = result.getBody();
		terminalResponse.setChargeResponse(chargeResponse);
		return chargeResponse;
	}

	String getStatusResponse() throws IOException
	{
		String url = "https://api.payjunctionlabs.com/" + "smartterminals/requests/"
				+ requestPaymentIdGenerator();

		HttpGet get = new HttpGet(url);
		ConnectionClient.Response response = client.execute(get);
		String statusResponse = response.getBody();
		terminalResponse.setStatusResponse(statusResponse);
		return statusResponse;
	}

	private String requestPaymentIdGenerator() throws IOException
	{
		return objectMapper
				.readTree(terminalResponse.getChargeResponse())
				.get("requestPaymentId").textValue();
	}

	String voidTransaction(List<NameValuePair> parameters, int transactionID) throws
			IOException
	{
		String url = "https://api.payjunctionlabs.com/transactions/" + transactionID;
		HttpPut put = new HttpPut(url);
		put.setEntity(new UrlEncodedFormEntity(parameters));
		ConnectionClient.Response result = client.execute(put);
		String voidResponse = result.getBody();
		return parseStatus(voidResponse);
	}

	private String parseStatus(String response) throws IOException
	{
		return objectMapper
				.readTree(response)
				.get("status").textValue();
	}
}
