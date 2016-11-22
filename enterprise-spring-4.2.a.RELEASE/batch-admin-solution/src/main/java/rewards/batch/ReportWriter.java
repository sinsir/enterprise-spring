package rewards.batch;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

import rewards.RewardConfirmation;

@ManagedResource(objectName="spring.application:application=batch-2,type=Writer")
public class ReportWriter implements ItemWriter<RewardConfirmation> {

	private int failOnConfirmationNumber = Integer.MAX_VALUE;
	
	private int count;
	
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void write(List<? extends RewardConfirmation> items) throws Exception {
		logger.debug("wrote " + items.size() 
				+ " confirmations, last confirmation nr = " 
				+ items.get(items.size() - 1).getConfirmationNumber());
		count += items.size();
		
		if (count > failOnConfirmationNumber) {
			throw new Exception("Planned exception, processed " + count + 
					            " requested failure at " + failOnConfirmationNumber);
		}
		
// 		Uncomment this line to simulate a one second delay for each item.
		
//		Thread.sleep(1000 * items.size());
	}

	@ManagedAttribute
	public int getFailOnConfirmationNumber() {
		return failOnConfirmationNumber;
	}

	@ManagedAttribute
	public void setFailOnConfirmationNumber(int failOnConfirmationNumber) {
		this.failOnConfirmationNumber = failOnConfirmationNumber;
	}

}
