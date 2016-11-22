package demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.core.MessagingTemplate;

public class Bootstrap {
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("demo/config.xml");
		MessageProcessor messageProcessor = context.getBean("messageProcessor", MessageProcessor.class);
		messageProcessor.processMessage("Hello World");
		messageProcessor.processMessage("Goodbye World");
		
		// Step 2 with SI and MessagingTemplate
//		MessagingTemplate messagingTemplate = context.getBean(MessagingTemplate.class);
//		messagingTemplate.convertAndSend("messages", "Hello World");
		
		
	}

}
