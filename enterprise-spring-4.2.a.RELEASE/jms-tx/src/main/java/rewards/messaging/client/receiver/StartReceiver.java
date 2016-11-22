package rewards.messaging.client.receiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import rewards.RewardConfirmation;

@SpringBootApplication
public class StartReceiver {

	private static final Log LOGGER = LogFactory.getLog(StartReceiver.class);

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(StartReceiver.class);
		
		JmsTemplate template = context.getBean(JmsTemplate.class);

		RewardConfirmation confirmation = null;
		do {
			confirmation = (RewardConfirmation) template.receiveAndConvert();
			if (confirmation != null) LOGGER.info("received confirmation: " + confirmation);
		} while (confirmation != null);
	}
}
