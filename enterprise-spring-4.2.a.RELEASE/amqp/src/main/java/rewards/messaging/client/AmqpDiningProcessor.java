package rewards.messaging.client;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import rewards.Dining;

/**
 * A batch processor that sends dining event notifications via JMS.
 */
public class AmqpDiningProcessor implements DiningProcessor {

	private RabbitTemplate rabbitTemplate;

	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void process(Dining dining) {

		/*  TODO 02: Execute the following tasks:
		 
		 1. Convert the dining to a message and send it to its destination.

		 2. Run the test. You will see NullPointerException.

		 */
		
		throw new UnsupportedOperationException("You still need to implement this method");

	}
}
