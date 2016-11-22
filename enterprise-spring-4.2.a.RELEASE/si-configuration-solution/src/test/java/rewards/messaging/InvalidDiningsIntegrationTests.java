package rewards.messaging;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.integration.MessageRejectedException;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import rewards.Dining;
import rewards.RewardNetwork;

/** xml configuration **/
@SpringApplicationConfiguration(locations="/rewards/messaging/test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class InvalidDiningsIntegrationTests {

	Dining invalidDining = new Dining(null, null, null, null, null);

	@Autowired
	RewardNetwork rewardNetwork;
	@Autowired
	MessagingTemplate template;

	@Test
	public void invalidDiningShouldCauseExceptionMessage() throws Exception {
		// set up mock for when there's a mistake in the filter config,
		// the invalid dining would cause an exception then
		Logger.getLogger(LoggingHandler.class).warn(
				"Note: A warning is expected if " + getClass().getSimpleName() + " succeeds ...");

		when(rewardNetwork.rewardAccountFor(invalidDining)).thenThrow(new EmptyResultDataAccessException(1));

		template.convertAndSend("dinings", invalidDining);

		MessageRejectedException exception = template.receiveAndConvert("errorTestChannel", MessageRejectedException.class);
		
		assertNotNull(exception);
	}
}
