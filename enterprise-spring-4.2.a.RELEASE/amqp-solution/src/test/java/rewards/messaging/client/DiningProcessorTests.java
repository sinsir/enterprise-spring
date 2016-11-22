package rewards.messaging.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import rewards.Dining;

/**
 * Tests the Dining batch processor
 */
@ContextConfiguration(locations = {
		"/rewards/messaging/client/client-config.xml",
		"/rewards/messaging/amqp-rewards-config.xml",
		"/rewards/messaging/amqp-infrastructure-config.xml",
		"/system-test-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class DiningProcessorTests {

	@Autowired
	private DiningProcessor diningProcessor;

	@Autowired
	private RewardConfirmationLogger confirmationLogger;

	@Test
	public void testBatch() throws Exception {
		Dining dining1 = Dining.createDining("80.93", "1234123412341234", "1234567890");
		Dining dining2 = Dining.createDining("56.12", "1234123412341234", "1234567890");
		Dining dining3 = Dining.createDining("32.64", "1234123412341234", "1234567890");
		Dining dining4 = Dining.createDining("77.05", "1234123412341234", "1234567890");
		Dining dining5 = Dining.createDining("94.50", "1234123412341234", "1234567890");


		diningProcessor.process(dining1);
		diningProcessor.process(dining2);
		diningProcessor.process(dining3);
		diningProcessor.process(dining4);
		diningProcessor.process(dining5);
		waitForBatch(5, 10000);

		assertEquals(5, confirmationLogger.getConfirmations().size());
	}

	private void waitForBatch(int batchSize, int timeout) throws InterruptedException {
		int sleepTime = 100;
		while (confirmationLogger.getConfirmations().size() < batchSize && timeout > 0) {
			Thread.sleep(sleepTime);
			timeout -= sleepTime;
		}
	}
}
