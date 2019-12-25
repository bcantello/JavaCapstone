package com.payjunction.flytrap;

import org.apache.http.client.methods.HttpUriRequest;

public class ApiConnectionTestUtil
{
	public static ConnectionClientStub payJunctionApiWillReturn(Integer responseCode, String body)
	{
		ConnectionClient.Response response = new ConnectionClient.Response(body, responseCode);
		return new ConnectionClientStub(response);
	}

	public static class ConnectionClientStub extends ConnectionClient
	{
		private final Response returnsResponse;
		HttpUriRequest lastRequest;

		ConnectionClientStub(Response returnsResponse)
		{
			super(null, null);
			this.returnsResponse = returnsResponse;
		}

		@Override
		public Response execute(HttpUriRequest request)
		{
			this.lastRequest = request;
			return returnsResponse;
		}
	}

}
