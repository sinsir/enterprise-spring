package rewards.messaging;

import rewards.RewardConfirmation;

public interface ConfirmationProcessor {

	void process(RewardConfirmation confirmation);

}
