package demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Bootstrap {
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("demo/config.xml");
		MessageProcessor messageProcessor = context.getBean("messageProcessor", MessageProcessor.class);
		messageProcessor.processMessage("Hello World");
		
	}

}
