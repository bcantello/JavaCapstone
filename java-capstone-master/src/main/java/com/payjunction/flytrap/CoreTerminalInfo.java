package com.payjunction.flytrap;

import java.io.IOException;
import java.util.Properties;

class CoreTerminalInfo
{
	private final Properties properties;

	public CoreTerminalInfo() throws IOException
	{
		properties = new Properties();

		properties.load(this.getClass().getResourceAsStream("/appconfig.properties"));
	}

	public String getTerminalId()
	{
		return properties.getProperty("terminalId");
	}

	public String getLogin()
	{
		return properties.getProperty("login");
	}

	public String getPassword()
	{
		return properties.getProperty("password");
	}

	public String getPjURL()
	{
		return properties.getProperty("pjURL");
	}

	public String getApplicationKey()
	{
		return properties.getProperty("applicationKey");
	}

	String getRequestPayment()
	{
		return properties.getProperty("requestPayment");
	}
}
