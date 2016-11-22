package rewards.messaging.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.subethamail.wiser.Wiser;

import rewards.Dining;

/**
 * Tests the Dining batch processor
 */
@SpringApplicationConfiguration(locations = {
		"/rewards/messaging/client/client-config.xml",
		"classpath*:rewards/messaging/*-config.xml",
		"/rewards/mail-server-config.xml",
		"/system-test-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class DiningProcessorTests {

	@Autowired Wiser mailServer;
	@Autowired DiningProcessor diningBatchProcessor;
	@Autowired RewardConfirmationLogger confirmationLogger;

	@Test
	public void testBatch() throws Exception {
		Dining dining1 = Dining.createDining("80.93", "1234123412341234", "1234567890");
		Dining dining2 = Dining.createDining("56.12", "1234123412341234", "1234567890");
		Dining dining3 = Dining.createDining("32.64", "1234123412341234", "1234567890");
		Dining dining4 = Dining.createDining("77.05", "1234123412341234", "1234567890");
		Dining dining5 = Dining.createDining("94.50", "1234123412341234", "1234567890");

		diningBatchProcessor.process(dining1);
		diningBatchProcessor.process(dining2);
		diningBatchProcessor.process(dining3);
		diningBatchProcessor.process(dining4);
		diningBatchProcessor.process(dining5);

		Thread.sleep(1000);
		assertEquals(5, confirmationLogger.getConfirmations().size());
		
		assertEquals(5, mailServer.getMessages().size());
		assertEquals("keith@example.com", mailServer.getMessages().get(0).getEnvelopeReceiver());
	}

}
