package rewards.batch;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

import rewards.RewardConfirmation;

// TODO 03: Use @ManagedResource to export this bean as an MBean; 
//			Use an objectName of "spring.application:application=batch-2,type=Writer"
@ManagedResource("spring.application:application=batch-2,type=Writer")
public class ReportWriter implements ItemWriter<RewardConfirmation> {

	// xTODO 04:	Make this field a managed attribute by adding getters and 
	// 			setters annotated with @ManagedAttribute.
	// 			Notice how this field is used in write() to intentionally generate an exception.
	//			Save all work, restart the server with JMX enabled (VM argument -Dcom.sun.management.jmxremote).
	//			Connect via JConsole and set the failOnConfirmationNumber to 55 to force an error.
	//			Start a new execution of the job via the web application, parameter: input.resource.path=diningRequests.csv
	//			Observe the failure details.
	//			Use JConsole to change failOnConfirmationNumber to 200.
	//			Restart the job, it should complete successfully. 
	private volatile int failOnConfirmationNumber = Integer.MAX_VALUE;
	
	private int count;
	
	private Logger logger = Logger.getLogger(getClass());

	@ManagedAttribute
	public int getFailOnConfirmationNumber() {
		return failOnConfirmationNumber;
	}
	
	@ManagedAttribute
	public void setFailOnConfirmationNumber(int failOnConfirmationNumber) {
		this.failOnConfirmationNumber = failOnConfirmationNumber;
	}

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
		// TODO 05: Uncomment the line below (Thread.sleep) to intentionally slow down this job.
		//			Save all work, restart the server.
		//			Launch the job using the same parameters (input.resource.path=diningRequests.csv)
		//			Observe the job while it executes.  Stop the job and observe the console results.
		//			Restart the job and observe the job as it completes.
		//
					Thread.sleep(1000 * items.size());
	}

}
