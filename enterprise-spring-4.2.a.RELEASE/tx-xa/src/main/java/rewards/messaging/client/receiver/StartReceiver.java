package rewards.messaging.client.receiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;

import rewards.RewardConfirmation;

@Configuration
@Import(ReceiverConfig.class)
public class StartReceiver {

	private static final Log LOGGER = LogFactory.getLog(StartReceiver.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(StartReceiver.class);
		
		JmsTemplate template = context.getBean(JmsTemplate.class);

		RewardConfirmation confirmation = null;
		do {
			confirmation = (RewardConfirmation) template.receiveAndConvert();
			if (confirmation != null) LOGGER.info("received confirmation: " + confirmation);
		} while (confirmation != null);
		
		context.close();
	}
}
