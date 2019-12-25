package com.payjunction.flytrap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ConnectionClientTest
{
	@SuppressWarnings("unchecked")
	@Test
	public void testConnectionClientConfiguresTheClientProperly() throws IOException
	{
		ArgumentCaptor<CredentialsProvider> credentialsProviderCaptor = ArgumentCaptor
				.forClass(CredentialsProvider.class);
		ArgumentCaptor<HttpUriRequest> requestCaptor = ArgumentCaptor.forClass(HttpUriRequest.class);

		ArgumentCaptor<List<Header>>  headerListCaptor = ArgumentCaptor.forClass(List.class);
		HttpClientFactory clientFactory = mock(HttpClientFactory.class);
		HttpClient httpClient = mock(HttpClient.class);
		HttpResponse response = mock(HttpResponse.class);
		HttpEntity entity = mock(HttpEntity.class);
		InputStream inputStream = new ByteArrayInputStream("Hello".getBytes());
		StatusLine statusLine = mock(StatusLine.class);

		ConnectionClient connectionClient = new ConnectionClient(clientFactory, new CoreTerminalInfo());

		when(clientFactory
				.makeHttpClient(any(CredentialsProvider.class), any(List.class)))
				.thenReturn(httpClient);

		when(httpClient.execute(any())).thenReturn(response);
		when(response.getEntity()).thenReturn(entity);
		when(entity.getContent()).thenReturn(inputStream);
		when(response.getStatusLine()).thenReturn(statusLine);
		when(statusLine.getStatusCode()).thenReturn(200);


		connectionClient.execute(new HttpGet("http://www.payjunction.com"));

		verify(clientFactory)
				.makeHttpClient(credentialsProviderCaptor.capture(), headerListCaptor.capture());

		verify(httpClient).execute(requestCaptor.capture());

		BasicCredentialsProvider credentialsProvider =
				(BasicCredentialsProvider) credentialsProviderCaptor.getValue();

		Credentials credentials = credentialsProvider.getCredentials(AuthScope.ANY);

		assertThat(credentials.getPassword()).isEqualTo("s0m3-pa55w0rd");
		assertThat(credentials.getUserPrincipal().getName()).isEqualTo("myLogin");
		assertThat(getHeader(headerListCaptor.getValue(), "Accept")).isEqualTo("application/json");
		assertThat(getHeader(headerListCaptor.getValue(), "X-PJ-Application-Key"))
				.isEqualTo("11111111-2222-3333-4444-555555555555");
		assertThat(requestCaptor.getValue().getURI().toString())
				.isEqualTo("http://www.payjunction.com");
		assertThat(requestCaptor.getValue().getMethod()).isEqualTo("GET");
	}

	private String getHeader(List<Header> headerList, String headerName)
	{
		return headerList
				.stream()
				.filter(header->header.getName().equals(headerName))
				.findFirst()
				.map(NameValuePair::getValue)
				.orElse(null);
	}
}
