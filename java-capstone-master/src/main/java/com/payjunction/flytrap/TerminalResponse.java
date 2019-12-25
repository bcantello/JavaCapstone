package com.payjunction.flytrap;

class TerminalResponse
{
	private String chargeResponse;
	private String statusResponse;

	String getChargeResponse()
	{
		return chargeResponse;
	}

	void setChargeResponse(String chargeResponse)
	{
		this.chargeResponse = chargeResponse;
	}

	String getStatusResponse()
	{
		return statusResponse;
	}

	void setStatusResponse(String statusResponse)
	{
		this.statusResponse = statusResponse;
	}
}
