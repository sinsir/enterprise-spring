package rewards;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;

@Configuration
public class SpringIntegrationInfraConfig {

	// TODO 23 (BONUS): add a poller with a fixed delay of 250 ms (same as in 'spring-integration-infrastructure-config.xml')


	@Bean
	public MessagingTemplate messagingTemplate() {
		MessagingTemplate messagingTemplate = new MessagingTemplate();
		messagingTemplate.setReceiveTimeout(2000);

		return messagingTemplate;
	}
}
