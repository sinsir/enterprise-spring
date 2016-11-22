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
		rabbitTemplate.convertAndSend("rewards.queue.dining", dining);
	}
}
