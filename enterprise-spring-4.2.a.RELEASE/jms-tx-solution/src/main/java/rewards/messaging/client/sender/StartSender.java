package rewards.messaging.client.sender;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import rewards.Dining;

@SpringBootApplication
public class StartSender {
	private static final Log LOGGER = LogFactory.getLog(StartSender.class);

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(StartSender.class);
		
		JmsTemplate template = context.getBean(JmsTemplate.class);
		
		List<Dining> dinings = Arrays.asList(
				Dining.createDining("80.93", "1234123412341234", "1234567890"),
				Dining.createDining("56.12", "1234123412341234", "1234567890"),
				Dining.createDining("32.64", "1234123412341234", "1234567890"),
				Dining.createDining("77.05", "1234123412341234", "1234567890"),
				Dining.createDining("94.50", "1234123412341234", "1234567890"));
		
		for(Dining dining : dinings) {
			LOGGER.info("Sending dining with amount " + dining.getAmount());
			template.convertAndSend(dining);
		}
	}
}
