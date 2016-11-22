package rewards.batch;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ScheduledDiningBatchProcessorBootstrap {

	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("scheduled-tasks.xml");
	}

}
