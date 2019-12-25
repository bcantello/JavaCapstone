package com.payjunction.flytrap;

import static com.payjunction.flytrap.ApiConnectionTestUtil.payJunctionApiWillReturn;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;

public class ApprovalResponseServiceTest
{
	@Test
	public void TestApprovalResponseServiceConfiguresGetProperly() throws IOException
	{
		String data = IOUtils.toString(this.getClass()
				.getResourceAsStream("/exampleApprovalResponse.json"), "UTF-8");
		ApiConnectionTestUtil.ConnectionClientStub clientStub = payJunctionApiWillReturn(200, data);
		TransactionID transactionID = new TransactionID();
		transactionID.setTransactionId(1217);

		String returnedString = new ApprovalResponseService(clientStub, transactionID)
				.getApprovalResponse();

		assertThat(returnedString).isEqualTo("Approved");
		assertThat(clientStub.lastRequest.getURI().toString()).containsPattern(
				"^https://api.payjunctionlabs.com/transactions/" + 1217);
	}
}
