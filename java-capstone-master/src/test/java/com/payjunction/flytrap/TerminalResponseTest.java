package com.payjunction.flytrap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class TerminalResponseTest
{
	@Test
	public void TestSetChargeResponseAndGetChargeResponse()
	{
		TerminalResponse terminalResponse = new TerminalResponse();

		terminalResponse.setChargeResponse("hello");
		terminalResponse.getChargeResponse();

		assertThat(terminalResponse.getChargeResponse()).isEqualTo("hello");
	}

	@Test
	public void TestSetStatusResponseAndGetStatusResponse()
	{
		TerminalResponse terminalResponse = new TerminalResponse();

		terminalResponse.setStatusResponse("hello");
		terminalResponse.getStatusResponse();

		assertThat(terminalResponse.getStatusResponse()).isEqualTo("hello");
	}
}
