package com.payjunction.flytrap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import java.io.IOException;

public class CoreTerminalInfoTest
{
	@Test
	public void testGetTerminalIdFromCoreTerminalInfo() throws IOException
	{
		CoreTerminalInfo coreTerminalInfo = new CoreTerminalInfo();
		coreTerminalInfo.getTerminalId();

		assertThat(coreTerminalInfo.getTerminalId()).isEqualTo("12345");
	}

	@Test
	public void testGetLoginFromCoreTerminalInfo() throws IOException
	{
		CoreTerminalInfo coreTerminalInfo = new CoreTerminalInfo();
		coreTerminalInfo.getLogin();

		assertThat(coreTerminalInfo.getLogin()).isEqualTo("myLogin");
	}

	@Test
	public void testGetPasswordFromCoreTerminalInfo() throws IOException
	{
		CoreTerminalInfo coreTerminalInfo = new CoreTerminalInfo();
		coreTerminalInfo.getPassword();

		assertThat(coreTerminalInfo.getPassword()).isEqualTo("s0m3-pa55w0rd");
	}

	@Test
	public void testGetPjURLFromCoreTerminalInfo() throws IOException
	{
		CoreTerminalInfo coreTerminalInfo = new CoreTerminalInfo();
		coreTerminalInfo.getPjURL();

		assertThat(coreTerminalInfo.getPjURL()).isEqualTo("https://thisisatest.com/");
	}

	@Test
	public void testGetApplicationKeyFromCoreTerminalInfo() throws IOException
	{
		CoreTerminalInfo coreTerminalInfo = new CoreTerminalInfo();
		coreTerminalInfo.getApplicationKey();

		assertThat(coreTerminalInfo.getApplicationKey()).isEqualTo(
				"11111111-2222-3333-4444-555555555555");
	}
}
