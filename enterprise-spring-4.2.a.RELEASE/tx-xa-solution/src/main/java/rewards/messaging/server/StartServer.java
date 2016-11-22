package rewards.messaging.server;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class StartServer {
	
	public static void main(String[] args) throws IOException {

		ConfigurableApplicationContext context = SpringApplication.run(StartServer.class);
		
		System.out.println("Started server, press Enter to stop");
		System.in.read();
		// ensure a proper shutdown of ActiveMQ and Derby
		context.close();
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException e) {
			// expected, indicates successful shutdown
			System.out.println(e.getMessage());
		}
		System.out.println("Server is shut down");
	}
}
