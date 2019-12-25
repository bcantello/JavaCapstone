package com.payjunction.flytrap;

import static com.payjunction.flytrap.ApiConnectionTestUtil.payJunctionApiWillReturn;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@SuppressWarnings("SameParameterValue")
public class ApiServiceTest
{
	@Test
	public void testConnectionRequestsTwoDollarsProperly() throws IOException
	{
		ApiConnectionTestUtil.ConnectionClientStub clientStub =
				payJunctionApiWillReturn(200, "Hello!");

		PaymentInformation paymentInformation = new PaymentInformation();
		paymentInformation.setAmountBase("2.00");

		TerminalResponse terminalResponse = new TerminalResponse();

		String returnedString = new ApiService(clientStub, new CoreTerminalInfo(), terminalResponse)
				.chargeCard(paymentInformation);

		assertThat(returnedString).isEqualTo("Hello!");
		assertThat(clientStub.lastRequest.getURI().toString()).containsPattern(
				"^https://thisisatest.com/smartterminals/"
						+ "997f03cc-2028-4a1b-9875-79dff40785f5/request-payment$");

		assertThat(getRequestContent(clientStub.lastRequest))
				.isEqualTo("terminalId=12345&amountBase=2.00");
	}

	@Test
	public void testConnectionRequestsStatusResponse() throws IOException
	{
		ApiConnectionTestUtil.ConnectionClientStub
				clientStub = payJunctionApiWillReturn(200, "Hello");

		TerminalResponse terminalResponse = new TerminalResponse();
		terminalResponse.setChargeResponse("{ " + "\"requestPaymentId\"" + " : " + "\"Hello\"}");

		String returnedString = new ApiService(clientStub, new CoreTerminalInfo(), terminalResponse)
				.getStatusResponse();

		assertThat(returnedString).isEqualTo("Hello");
		assertThat(clientStub.lastRequest.getURI().toString()).containsPattern(
				"^https://api.payjunctionlabs.com/smartterminals/requests/"
						+ "Hello$");
	}


	private String getRequestContent(HttpUriRequest request) throws IOException
	{
		HttpEntityEnclosingRequest requestWithEntity = (HttpEntityEnclosingRequest) request;
		return new BufferedReader(new InputStreamReader(requestWithEntity.getEntity().getContent()))
				.lines().collect(Collectors.joining("\n"));
	}
}
