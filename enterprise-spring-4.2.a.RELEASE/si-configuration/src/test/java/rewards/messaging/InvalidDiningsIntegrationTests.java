package rewards.messaging;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.integration.MessageRejectedException;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import rewards.Dining;
import rewards.RewardNetwork;


@SpringApplicationConfiguration(locations="/rewards/messaging/test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class InvalidDiningsIntegrationTests {

	Dining invalidDining = new Dining(null, null, null, null, null);

	@Autowired RewardNetwork rewardNetwork;
	@Autowired MessagingTemplate template;
	@Autowired MessageChannel dinings;

	@Test
	public void invalidDiningShouldCauseExceptionMessage() throws Exception {
		// set up mock for when there's a mistake in the filter config,
		// the invalid dining would cause an exception then
		when(rewardNetwork.rewardAccountFor(invalidDining)).thenThrow(new EmptyResultDataAccessException(1));
		
		// xTODO 05: Use the messaging template to send the invalidDining to the dinings channel.
		//			Run this test, initially it should fail.  Move onto the next step.
		template.convertAndSend(dinings, invalidDining);
		
		// xTODO 10: Use the messaging template to make sure that 
		//			the errorTestChannel contains a MessageRejectedException.
		//			Run this test, it should pass.
		MessageRejectedException exception = template.receiveAndConvert("errorTestChannel", MessageRejectedException.class);
		
		assertNotNull(exception);
	}
}
