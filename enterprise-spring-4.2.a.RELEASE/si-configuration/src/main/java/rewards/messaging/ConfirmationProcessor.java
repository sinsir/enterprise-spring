package rewards.messaging;

import rewards.RewardConfirmation;

// TODO 16 (BONUS): create a Messaging Gateway with annotations
// the Messaging Gateway should be the same as in the 'spring-integration-idempotent-receiver-config.xml' file
// the request channel is 'confirmations'
public interface ConfirmationProcessor {

	void process(RewardConfirmation confirmation);

}
