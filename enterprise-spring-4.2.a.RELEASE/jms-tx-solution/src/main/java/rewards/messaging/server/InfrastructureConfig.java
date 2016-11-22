/**
 * 
 */
package rewards.messaging.server;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.DERBY;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class InfrastructureConfig {

	@Bean PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
	
	@Bean DataSource dataSource() {
	    return new EmbeddedDatabaseBuilder()
	    				.setType(DERBY)
	    				.setName("rewardsdb")
	    				.addScripts("classpath:rewards/messaging/server/schema.sql",
	    							"classpath:rewards/messaging/server/test-data.sql")
	    				.ignoreFailedDrops(true)
	    				.build();
	}
	
	@Bean ConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL("vm:broker:(tcp://localhost:61616)?persistent=false");
		RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
		redeliveryPolicy.setMaximumRedeliveries(3);
		connectionFactory.setRedeliveryPolicy(redeliveryPolicy);
		return connectionFactory;
	}
	
	@Bean JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}
	
}
