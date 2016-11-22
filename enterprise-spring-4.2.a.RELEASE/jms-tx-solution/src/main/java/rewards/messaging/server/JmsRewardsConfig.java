/**
 * 
 */
package rewards.messaging.server;

import javax.jms.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import rewards.RewardNetwork;

@Configuration
@ImportResource("classpath:/app-config.xml")
@EnableJms
public class JmsRewardsConfig {

	@Bean DiningListener diningListener(RewardNetwork rewardNetwork,ConnectionFactory connectionFactory) {
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName("rewards.queue.confirmation");
		jmsTemplate.setSessionTransacted(true);
		return new DiningListener(rewardNetwork, jmsTemplate);
	}
	
	@Bean DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory cf = 
                new DefaultJmsListenerContainerFactory( );
        cf.setConnectionFactory(connectionFactory);
        cf.setSessionTransacted(true);
        return cf;
	}
	
}
