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

@Configuration
// Enabling Spring Integration infrastructure
@EnableIntegration
// Discovering @MessagingGateway interfaces
@IntegrationComponentScan
public class SpringIntegrationIdempotentReceiverConfig {

	@Autowired AlreadyRewardedConfirmer alreadyRewardedConfirmer;
	
	@Autowired RewardNetwork rewardNetwork;
	
	@Bean
	public IntegrationFlow diningsFlow() {

		return IntegrationFlows
				//input channel
				.from(dinings())

				.filter(Dining.class,
						d -> d.getAmount() != null && d.getCreditCardNumber() != null && d.getMerchantNumber() != null && d.getDate() != null,
						filterSpec -> filterSpec.throwExceptionOnRejection(true))

				//Services Activators
				.<Dining>handle((d,h) -> alreadyRewardedConfirmer.sendConfirmationForExistingDining(d))
				.<Dining>handle((d,h) -> rewardNetwork.rewardAccountFor(d))				

				//output channel
				.channel(confirmations())

				.get();
	}

	@Bean
	public AlreadyRewardedConfirmer alreadyRewardedConfirmer(RewardRepository rewardRepository, ConfirmationProcessor confirmationProcessor) {
		return new AlreadyRewardedConfirmer(rewardRepository, confirmationProcessor);
	}

	@Bean
	public MessageChannel dinings() {
		return MessageChannels.queue()
							  .get();
	}

	@Bean
	public QueueChannel confirmations() {
		return MessageChannels.queue(10)
							  .get();
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
