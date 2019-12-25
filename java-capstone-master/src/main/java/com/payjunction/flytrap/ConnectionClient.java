package com.payjunction.flytrap;

import com.google.common.collect.ImmutableList;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class ConnectionClient
{
	private final HttpClientFactory clientFactory;
	private final CoreTerminalInfo setup;

	public ConnectionClient (HttpClientFactory clientFactory, CoreTerminalInfo setup)
	{
		this.clientFactory = clientFactory;
		this.setup = setup;
	}

	private HttpClient getClient()
	{
		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials =
				new UsernamePasswordCredentials(setup.getLogin(), setup.getPassword());
		provider.setCredentials(AuthScope.ANY, credentials);

		Header acceptHeader = new BasicHeader("Accept", "application/json");
		Header appKeyHeader =
				new BasicHeader("X-PJ-Application-Key", setup.getApplicationKey());

		return clientFactory.makeHttpClient(provider, ImmutableList.of(acceptHeader, appKeyHeader));
	}

	public Response execute(HttpUriRequest request) throws IOException
	{
		StringBuilder result = new StringBuilder();
		HttpResponse response = getClient().execute(request);

		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));

		String line;
		while ((line = rd.readLine()) != null)
		{
			result.append(line);
		}
		return new Response(result.toString(), response.getStatusLine().getStatusCode());
	}

	static class Response
	{
		private final String result;
		private final Integer statusCode;

		public Response(String result, Integer statusCode)
		{
			this.result = result;
			this.statusCode = statusCode;
		}

		public String getBody()
		{
			return result;
		}

		Integer getStatusCode()
		{
			return statusCode;
		}
	}
}
