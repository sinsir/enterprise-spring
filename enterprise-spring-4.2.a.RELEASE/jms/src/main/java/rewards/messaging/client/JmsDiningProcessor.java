package rewards.messaging.client;

import org.springframework.jms.core.JmsTemplate;

import rewards.Dining;

/**
 * A batch processor that sends dining event notifications via JMS.
 */
public class JmsDiningProcessor implements DiningProcessor {

	private JmsTemplate jmsTemplate;

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void process(Dining dining) {
				
		/*  xTODO 02: Convert the dining object to a message and send it to its destination.
		 * 			 Remove the line that throws an exception.
		 * 			 Run the test.  It should fail at this point with a NullPointerException.
		 */
		
		jmsTemplate.convertAndSend(dining);
		
		//throw new UnsupportedOperationException("You still need to implement this method");
	}
}
