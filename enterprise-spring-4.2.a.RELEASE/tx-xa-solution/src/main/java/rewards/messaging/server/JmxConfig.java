/**
 * 
 */
package rewards.messaging.server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jmx.export.MBeanExporter;

@Configuration
public class JmxConfig {

	@Bean MBeanExporter mBeanExporter(DiningListener diningListener,JdbcTemplate jdbcTemplate) {
		MBeanExporter exporter = new MBeanExporter();
		
		Map<String,Object> beans = new HashMap<>();
		beans.put("rewards:type=ConfirmationLister",new ConfirmationLister(jdbcTemplate));
		beans.put("rewards:type=DiningListener",diningListener);
		
		exporter.setBeans(beans);
		
		return exporter;
	}
	
}
