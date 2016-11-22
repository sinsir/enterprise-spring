package rewards.internal;

import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.RewardNetwork;


public class SlowRewardNetwork implements RewardNetwork {

	private final RewardNetwork delegate;

	public SlowRewardNetwork(RewardNetwork delegate) {
		this.delegate = delegate;
	}

	@Override
	public RewardConfirmation rewardAccountFor(Dining dining) {
		sleepFor(6);
		return delegate.rewardAccountFor(dining);
	}

	private void sleepFor(int ms) {
		try {
			Thread.sleep(6);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

}
