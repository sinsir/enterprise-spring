/**
 * 
 */
package rewards.messaging;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;

/**
 * These beans provide a messaging infrastructure for the rewards network
 */
@Configuration
@EnableJms

/* xTODO 05: Execute the following tasks:
            1.  Activate the detection of JMS annotation with @EnableJms
            
            2.  Declare a jmsListenerContainerFactory bean that uses the cachingConnectionFactory bean
*/

public class JmsInfrastructureConfig {
	
	
	
	@Bean ConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		factory.setBrokerURL("vm://embedded?broker.persistent=false&broker.useShutdownHook=false");
		return factory;
	}
	
	@Bean ConnectionFactory cachingConnectionFactory() {
		return new CachingConnectionFactory(connectionFactory());
	}
	
	@Bean Queue diningQueue() {
		return new ActiveMQQueue("rewards.queue.dining");
	}
	
	@Bean Queue confirmationQueue() {
		return new ActiveMQQueue("rewards.queue.confirmation");
	}
	
	@Bean DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		final DefaultJmsListenerContainerFactory cf = new DefaultJmsListenerContainerFactory();
		cf.setConnectionFactory(connectionFactory());
		
		return cf;
	}
	
}
