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

		Object receivedPayload = template.receive("xmlDinings").getPayload();
		assertThat(receivedPayload, is(instanceOf(String.class)));
		assertThat(xpathTemplate.evaluateAsString("/dining/@transaction-id",
				new StringSource((String) receivedPayload)), is("universallyUniqueString"));
	}

	@Test(timeout = 2000)
	public void inboundMultipleDiningXml() throws Exception {
		File diningsFile = new ClassPathResource("dinings-sample.xml", getClass()).getFile();
		template.convertAndSend("mixedXmlDinings", diningsFile);

		String receivedPayload = template.receiveAndConvert("xmlDinings", String.class);

		assertThat(xpathTemplate.evaluateAsString("/dining/@transaction-id", 
				new StringSource(receivedPayload)), is("universallyUniqueString1"));
		assertThat(xpathTemplate.evaluateAsString("/dining/@transaction-id", 
				new StringSource(template.receiveAndConvert("xmlDinings", String.class))),
				is("universallyUniqueString2"));
	}
}
