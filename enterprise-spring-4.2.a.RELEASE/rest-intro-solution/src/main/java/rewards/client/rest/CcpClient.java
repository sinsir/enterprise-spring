package rewards.client.rest;

import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.client.RestTemplate;

import rewards.oxm.Reward;

public class CcpClient {
	private static final String BASE_URI = "http://localhost:8080/rest-intro-solution/rest/";
	
	private final RestTemplate restTemplate = new RestTemplate();
	private final Log log = LogFactory.getLog(getClass());
	
	/**
	 * Sends a dining request to a URI with the given transaction id and returns the received Reward
	 * @param transactionId a unique transaction id
	 * @param diningRequest a {@link rewards.oxm.Dining} instance or a {@link javax.xml.transform.Source} instance with a &lt;dining&gt; element 
	 * @return the created Reward
	 */
	public Reward sendDiningRequest(String transactionId, Object diningRequest) {
		URI location = restTemplate.postForLocation(BASE_URI + "dining/{transactionId}", diningRequest, transactionId);
		log.debug("Received location of newly created reward: " + location);
		Reward reward = restTemplate.getForObject(location, Reward.class);
		log.debug("Received reward: " + toString(reward));
		
		return reward;
	}

	private String toString(Reward reward) {
		if (reward == null) return null;
		return String.format("confirmation no.: %s, account no.: %s, reward: %.2f",
					reward.getConfirmationNumber(),
					reward.getAccountNumber(),
					reward.getRewardAmount()
				);
	}
}
