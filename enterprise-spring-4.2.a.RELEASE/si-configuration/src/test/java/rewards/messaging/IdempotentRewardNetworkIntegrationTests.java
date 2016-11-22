package rewards.messaging;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.RewardNetwork;
import rewards.internal.reward.RewardRepository;


@SpringApplicationConfiguration(locations="/rewards/messaging/test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class IdempotentRewardNetworkIntegrationTests {

	Dining dining = Dining.createDining("txId", "100.00", "1234123412341234", "1234567890");

	@Autowired RewardRepository rewardRepository;
	@Autowired RewardNetwork rewardNetwork;
	@Autowired MessagingTemplate template;

	@Test
	public void idempotence() throws Exception {
		RewardConfirmation confirmation = mock(RewardConfirmation.class);
		when(rewardNetwork.rewardAccountFor(dining)).thenReturn(confirmation);
		template.convertAndSend("dinings", dining);
		
		RewardConfirmation firstConfirmation = template.receiveAndConvert("confirmations", RewardConfirmation.class);
		
		// this time the repository will find an existing confirmation 
		// (once it's used by the extra service activator you add as the 3rd ToDo)
		when(rewardRepository.findConfirmationFor(dining)).thenReturn(firstConfirmation);
		template.convertAndSend("dinings", dining);
		
		RewardConfirmation secondConfirmation = template.receiveAndConvert("confirmations", RewardConfirmation.class);
		
		// xTODO 01: Add an assert that ensures that the two confirmations are the same.
		//			Alter the verify to ensure that rewardAccountFor is invoked only once (instead of twice).
		//			Run the test.  Your assertion should pass, but the verify will not yet pass.
		assertEquals(firstConfirmation, secondConfirmation);
		verify(rewardNetwork, times(1)).rewardAccountFor(dining);
	}

}
