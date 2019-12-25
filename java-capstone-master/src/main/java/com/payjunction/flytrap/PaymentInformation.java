package com.payjunction.flytrap;

import com.google.common.collect.ImmutableList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.List;

class PaymentInformation
{
	private final CoreTerminalInfo setup = new CoreTerminalInfo();
	private String amountBase;

	PaymentInformation() throws IOException
	{
	}

	public void setAmountBase(String amountBase)
	{
		this.amountBase = amountBase;
	}

	public List<NameValuePair> chargeUrlParameters()
	{
		return ImmutableList.of(
				new BasicNameValuePair("terminalId", setup.getTerminalId()),
				new BasicNameValuePair("amountBase", amountBase)
		);
	}

	public List<NameValuePair> refundUrlParameters()
	{
		return ImmutableList.of(
				new BasicNameValuePair("action", "REFUND"),
				new BasicNameValuePair("terminalId", setup.getTerminalId()),
				new BasicNameValuePair("amountBase", amountBase)
		);
	}

	public List<NameValuePair> voidUrlParameters()
	{
		return ImmutableList.of(
				new BasicNameValuePair("status", "VOID")
		);
	}
}

