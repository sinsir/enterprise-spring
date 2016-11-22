package rewards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.MessageChannel;
import rewards.internal.reward.RewardRepository;
import rewards.messaging.AlreadyRewardedConfirmer;
import rewards.messaging.ConfirmationProcessor;

//TODO 15 (BONUS): add 2 annotations to enable Spring Integration and to scan Spring Integration specific components
@Configuration
public class SpringIntegrationIdempotentReceiverConfig {

	@Autowired AlreadyRewardedConfirmer alreadyRewardedConfirmer;
	
	@Autowired RewardNetwork rewardNetwork;
	
	@Bean
	public IntegrationFlow diningsFlow() {

		// TODO 18 (BONUS): create an integration flow and add the 'dinings' channel as the input. 
		// Use the IntegrationFlows class.

		// TODO 21 (BONUS): add a filter using Java for the filtering logic (same logic as in 'spring-integration-idempotent-receiver-config.xml')
		// Don't forget to enable the 'throwExceptionOnRejection' property

		// TODO 22 (BONUS): add the 2 service activators (same as in 'spring-integration-idempotent-receiver-config.xml')
		//                 - The first one uses the 'sendConfirmationForExistingDining' method from the 'alreadyRewardedConfirmer' bean
		//                 - The second one uses the 'rewardAccountFor' method from the 'rewardNetwork' bean

		// TODO 20 (BONUS): add the 'confirmations' channel at the end of the flow


		return null;
	}


	// TODO 17 (BONUS): create a method declaring the input queue channel named 'dinings'


	// TODO 19 (BONUS): create a method declaring the 'confirmations' output queue channel with a capacity of 10 messages


	@Bean
	public AlreadyRewardedConfirmer alreadyRewardedConfirmer(RewardRepository rewardRepository, ConfirmationProcessor confirmationProcessor) {
		return new AlreadyRewardedConfirmer(rewardRepository, confirmationProcessor);
	}

	@Bean
	public MessageChannel errorChannel() {
		return MessageChannels.publishSubscribe()
							  .ignoreFailures(true)
							  .get();
	}

	@Bean
	public IntegrationFlow errorFlow() {
		LoggingHandler loggingHandler =  new LoggingHandler(LoggingHandler.Level.WARN.name());
		loggingHandler.setExpression("'filter rejected message with ' + payload.failedMessage.payload");
		loggingHandler.setLoggerName("logger");

		return IntegrationFlows.from("errorChannel")
							   .handle(loggingHandler)
							   .get();
	}

}
