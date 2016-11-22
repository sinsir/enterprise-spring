package rewards.messaging;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.xml.transform.StringSource;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.XPathOperations;

@SpringApplicationConfiguration(locations="/rewards/messaging/BatchedDiningSplitterIntegrationTests-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class BatchedDiningSplitterIntegrationTests {

	XPathOperations xpathTemplate = new Jaxp13XPathTemplate();
	
	@Autowired MessagingTemplate template;

	@Test
	public void inboundSingleDiningXml() throws Exception {
		File diningFile = new ClassPathResource("dining-sample.xml", getClass()).getFile();
		template.convertAndSend("mixedXmlDinings", diningFile);

		String receivedPayload = template.receiveAndConvert("xmlDinings", String.class);
		assertThat(xpathTemplate.evaluateAsString("/dining/@transaction-id",
				new StringSource(receivedPayload)), is("universallyUniqueString"));
	}

	@Test(timeout = 2000)
	public void inboundMultipleDiningXml() throws Exception {
		File diningsFile = new ClassPathResource("dinings-sample.xml", getClass()).getFile();
		template.convertAndSend("mixedXmlDinings", diningsFile);
		// xTODO 06:	Use the message template to send the diningsFile to the mixedXmlDinings channel.
		// 			Look at the test above for an example.

		// xTODO 07:	Use the message template to receive and convert from xmlDinings,
		//			assign the result to receivedPayload.
		String receivedPayload = template.receiveAndConvert("xmlDinings", String.class);

		assertThat(xpathTemplate.evaluateAsString("/dining/@transaction-id", 
				new StringSource(receivedPayload)), is("universallyUniqueString1"));
		assertThat(xpathTemplate.evaluateAsString("/dining/@transaction-id", 
				new StringSource(template.receiveAndConvert("xmlDinings", String.class))),
				is("universallyUniqueString2"));

		// xTODO 08:	Assert that the received payload has a dining root element with the right transaction id. 
		//			Look at dinings-sample.xml to find the transaction id, and use xpathTemplate to query for it.
		//			Run this test, it will initially fail.
	}
}
