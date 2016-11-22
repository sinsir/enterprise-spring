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
import org.springframework.transaction.PlatformTransactionManager;

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
	
	// xTODO 04: Set the transactionManager property on the JmsListenerContainerFactory 
	
	@Bean DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
			ConnectionFactory connectionFactory,
			PlatformTransactionManager transactionManager) {
		DefaultJmsListenerContainerFactory cf = 
                new DefaultJmsListenerContainerFactory( );
        cf.setConnectionFactory(connectionFactory);
        cf.setSessionTransacted(true);
        cf.setTransactionManager(transactionManager);
        
        return cf;
	}
	
	/* xTODO 05:  Run the StartServer application.
       Then run the StartSender application to send messages. 
	   Use the instructions in lab document for using JConsole for rest of the lab.
    */
	
}
