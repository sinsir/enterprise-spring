package rewards.messaging;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import rewards.Dining;
import rewards.RewardConfirmation;

@SpringApplicationConfiguration(locations="/rewards/messaging/MarshallingIntegrationTests-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MarshallingIntegrationTests {

	@Autowired MessagingTemplate template;
	
	@Test
	public void inboundDiningXml() throws Exception {
		File xmlFile = new ClassPathResource("dining-sample.xml", getClass()).getFile();
		
		template.convertAndSend("xmlDinings", xmlFile);
		
		Dining receivedPayload = template.receiveAndConvert("dinings", Dining.class);
		assertThat(receivedPayload.getTransactionId(), is("universallyUniqueString"));
	}

	@Test
	public void outboundConfirmation() throws Exception {
		RewardConfirmation confirmation = mock(RewardConfirmation.class);
		when(confirmation.getDiningTransactionId()).thenReturn("UUID");
		
		template.convertAndSend("confirmations", confirmation);
		
		String receivedPayload = template.receiveAndConvert("xmlConfirmations", String.class);
		assertTrue(receivedPayload.contains("dining-transaction-id=\"UUID\""));
	}

}
