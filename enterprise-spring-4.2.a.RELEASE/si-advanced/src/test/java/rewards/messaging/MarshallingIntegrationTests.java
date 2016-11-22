package rewards.messaging;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Ignore;
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
	//@Ignore("To remove when you add the first assertion")
	public void inboundDiningXml() throws Exception {
		File xmlFile = new ClassPathResource("dining-sample.xml", getClass()).getFile();
		
		template.convertAndSend("xmlDinings", xmlFile);
		
		Dining receivedPayload = template.receiveAndConvert("dinings", Dining.class);
		// xTODO 01: Assert that the received Dining contains values originally found in the dining-sample.xml.
		//          Remove annotation @Ignore on test
		//          Run this test, it should initially fail.
		assertThat(receivedPayload.getTransactionId(), is("universallyUniqueString"));
	}

	@Test
	//@Ignore("To remove when you add the first assertion")
	public void outboundConfirmation() throws Exception {
		RewardConfirmation confirmation = mock(RewardConfirmation.class);
		when(confirmation.getDiningTransactionId()).thenReturn("UUID");
		
		template.convertAndSend("confirmations", confirmation);
		
		String receivedPayload = template.receiveAndConvert("xmlConfirmations", String.class);
		// xTODO 03: Add assertions on the returned String to ensure it contains an XML confirmation.
		//          (For example, check that the String contains a "dining-transaction-id" sub-string.)
		//          Remove annotation @Ignore on test
		//          Run this test, it should initially fail.
		assertTrue(receivedPayload.contains("dining-transaction-id=\"UUID\""));
	}

	@Test
	public void configOk() throws Exception {
		//
	}	
}
