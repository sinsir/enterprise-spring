package rewards.messaging.server;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsUtils;

import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.RewardNetwork;

public class DiningListener {

	private Log log = LogFactory.getLog(getClass());

	private RewardNetwork rewardNetwork;
	private JmsTemplate jmsTemplate;

	private boolean causeErrorAfterReceiving = false;
	private boolean causeErrorAfterProcessing = false;
	private boolean causeErrorAfterSending = false;

	public DiningListener(RewardNetwork rewardNetwork, JmsTemplate jmsTemplate) {
		this.rewardNetwork = rewardNetwork;
		this.jmsTemplate = jmsTemplate;
	}

	public boolean isCauseErrorAfterReceiving() { return causeErrorAfterReceiving; }
	public void setCauseErrorAfterReceiving(boolean flag) { this.causeErrorAfterReceiving = flag; }

	public boolean isCauseErrorAfterProcessing() { return causeErrorAfterProcessing; }
	public void setCauseErrorAfterProcessing(boolean flag) { this.causeErrorAfterProcessing = flag; }

	public boolean isCauseErrorAfterSending() {	return causeErrorAfterSending; }
	public void setCauseErrorAfterSending(boolean flag) { this.causeErrorAfterSending = flag; }

	@JmsListener(destination="rewards.queue.dining")
	public void onMessage(Message message) {
		try {
			Dining dining = (Dining) ((ObjectMessage) message).getObject();
			logMessage(message, dining);
			if (causeErrorAfterReceiving)
				throw new RuntimeException("error after receiving dining with amount " + dining.getAmount());
			RewardConfirmation confirmation = this.rewardNetwork.rewardAccountFor(dining);
			if (causeErrorAfterProcessing) 
				throw new RuntimeException("error after processing dining with amount " + dining.getAmount());
			jmsTemplate.convertAndSend(confirmation);
			log.debug("Sent response with confirmation nr " + confirmation.getConfirmationNumber());
			if (causeErrorAfterSending) 
				throw new RuntimeException("error after confirming dining with amount " + dining.getAmount());
		} catch (JMSException e) {
			throw JmsUtils.convertJmsAccessException(e);
		}
	}

	private void logMessage(Message message, Dining dining) throws JMSException {
		if (log.isDebugEnabled()) {
			String msg = "Received Dining with amount " + dining.getAmount();
			if (message.getJMSRedelivered()) {
				int nrOfDeliveries = message.getIntProperty("JMSXDeliveryCount");
				msg += " (redelivered " + (nrOfDeliveries - 1) + " times)";
			}
			log.debug(msg);
		}
	}
}
