package rewards.messaging.client;

import rewards.Dining;

/**
 * A batch processor for dining events.
 * 
 * Typical implementations would send notifications to the reward network in order to generate reward confirmations.
 */
public interface DiningProcessor {

	/**
	 * Processes a batch of dining events.
	 * 
	 * @param dining a list of dining events
	 */
	void process(Dining dining);

}
