package rewards.batch;

import org.springframework.context.support.ClassPathXmlApplicationContext;

//	TODO 08: Run this class as a Java Application.  
//			Look for console output indicating successful
//			processing of batch records.  

public class ScheduledDiningBatchProcessorBootstrap {

	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("scheduled-tasks.xml");
	}

}
