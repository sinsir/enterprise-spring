package rewards.client.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import rewards.oxm.Reward;

public class CcpClientTestsForServer {

	private CcpClient client = new CcpClient();
	
	@Test
	public void processDining() throws Exception {
		String txId = UUID.randomUUID().toString();
		Reward reward = client.sendDiningRequest(txId, readDining());
		assertNotNull(reward);
		assertEquals(new BigDecimal("8.00"), reward.getRewardAmount().setScale(2));
	}
	
	@Test
	public void processDiningTwice() throws Exception {
		String txId = UUID.randomUUID().toString();
		Reward reward = client.sendDiningRequest(txId, readDining());
		assertNotNull(reward);
		try {
			client.sendDiningRequest(txId, readDining());
			fail("POST for processed dining should have thrown HttpClientErrorException");
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.CONFLICT, e.getStatusCode());
		}
	}

	private Source readDining() throws IOException {
		return new StreamSource(new ClassPathResource("rewards/client/rest/diningRequest.xml").getInputStream());
	}
}
