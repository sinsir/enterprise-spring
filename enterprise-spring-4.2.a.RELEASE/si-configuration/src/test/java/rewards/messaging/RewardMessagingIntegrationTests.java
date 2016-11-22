package rewards.messaging;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.integration.MessageRejectedException;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import rewards.Dining;
import rewards.RewardConfirmation;

// TODO 13 (BONUS): Use the SpringIntegrationIdempotentReceiverConfig, SpringIntegrationInfraConfig and SystemTestConfig classes for the test instead of xml files
//   hint: @SpringApplicationConfiguration(classes = {class1.class, class2.class, class3.class})
// TODO 14 (BONUS): Take a look at the configuration classes : SpringIntegrationIdempotentReceiverConfig
//                                                            SpringIntegrationInfraConfig
//                                                            SystemTestConfig
// TODO 24 (BONUS): Run all tests. They should pass.
@SpringApplicationConfiguration(locations={"/rewards/messaging/spring-integration-idempotent-receiver-config.xml",
	                   "/rewards/messaging/spring-integration-infrastructure-config.xml",
	                   "classpath:system-test-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class RewardMessagingIntegrationTests {

	@Autowired MessagingTemplate template;
	@Autowired PublishSubscribeChannel errorChannel;

	@Test
	public void configOk() {
		// successful ApplicationContext creation test
	}
	
	@Test
	//@Ignore // xTODO 04: remove this @Ignore and run the test, it should pass.
	public void sendDiningTwice() throws Exception {
		final String txId = "tx-id";
		Dining dining = Dining.createDining(txId, "100.00", "1234123412341234", "1234567890");
		template.convertAndSend("dinings", dining);
		
		RewardConfirmation payload = template.receiveAndConvert("confirmations",RewardConfirmation.class);
		assertEquals(txId, payload.getDiningTransactionId());
		
		template.convertAndSend("dinings", dining);
		assertEquals(payload, template.receiveAndConvert("confirmations",RewardConfirmation.class));
	}
	
	@Test
	//@Ignore // xTODO 11: remove this @Ignore and run the test, it should pass.
	public void sendInvalidDining() throws Exception {
		ErrorHandler handler = new ErrorHandler();
		errorChannel.subscribe(handler);
		
		Dining invalidDining = new Dining(null, null, null, null, null);
		template.convertAndSend("dinings", invalidDining);

		Thread.sleep(1000);
		assertNotNull("No error message received", handler.msg);
		assertThat(handler.msg.getPayload(), is(instanceOf(MessageRejectedException.class)));
	}
	
	static class ErrorHandler implements MessageHandler {
		volatile Message<?> msg;

		public void handleMessage(Message<?> message) throws MessagingException {
			msg = message;
		}
	}

}
