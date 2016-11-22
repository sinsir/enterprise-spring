/**
 * 
 */
package rewards.messaging.client.sender;

import javax.jms.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;

import rewards.messaging.client.ClientInfrastructureConfig;

@Configuration
@Import(ClientInfrastructureConfig.class)
public class SenderConfig {

	@Bean JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName("rewards.queue.dining");
		return jmsTemplate;
	}
	
}
