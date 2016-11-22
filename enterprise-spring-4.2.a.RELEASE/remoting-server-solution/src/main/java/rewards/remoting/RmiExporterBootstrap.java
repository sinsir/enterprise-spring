package rewards.remoting;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("rewards/remoting/rmi-exporter-config.xml")
public class RmiExporterBootstrap {
	
	public static void main(String[] args) {
		SpringApplication.run(RmiExporterBootstrap.class);
	}
}
