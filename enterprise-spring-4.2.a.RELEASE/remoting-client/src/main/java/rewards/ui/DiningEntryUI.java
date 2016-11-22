package rewards.ui;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import rewards.Dining;


/**
 * A command line interface for entering {@link Dining} events.
 */
@Configuration
@ImportResource("rewards/ui/diningentry-config.xml")
public class DiningEntryUI {

	/**
	 * Bootstrap a new {@link DiningEntryUI} from the command line.
	 * @param args no arguments expected
	 */
	public static void main(String[] args) throws IOException {
		ApplicationContext context = SpringApplication.run(DiningEntryUI.class);
		context.getBean(DiningEntry.class).start();
	}

}