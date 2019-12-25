package com.payjunction.flytrap;

import org.apache.http.Header;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.List;

class HttpClientFactory
{
	public HttpClient makeHttpClient(CredentialsProvider provider, List<Header> defaultHeaders)
	{
		return HttpClientBuilder.create()
				.setDefaultCredentialsProvider(provider)
				.setDefaultHeaders(defaultHeaders)
				.build();
	}
}
