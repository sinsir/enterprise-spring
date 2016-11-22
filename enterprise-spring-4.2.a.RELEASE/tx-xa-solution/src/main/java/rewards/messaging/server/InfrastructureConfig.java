/**
 * 
 */
package rewards.messaging.server;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;

import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.derby.jdbc.EmbeddedXADataSource40;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jms.AtomikosConnectionFactoryBean;

@Configuration
public class InfrastructureConfig {

	@Bean PlatformTransactionManager transactionManager() throws Exception {
		JtaTransactionManager transactionManager = new JtaTransactionManager();
		transactionManager.setTransactionManager(userTransactionManager());
		
		UserTransactionImp userTransaction = new UserTransactionImp();
		userTransaction.setTransactionTimeout(300);
		transactionManager.setUserTransaction(userTransaction);
		
		return transactionManager;
	}
	
	@Bean(destroyMethod="close") UserTransactionManager userTransactionManager() throws Exception {
		UserTransactionManager userTransactionManager = new UserTransactionManager();
		userTransactionManager.init();
		return userTransactionManager;
	}
	
	@Bean(destroyMethod="close") DataSource dataSource() throws Exception {
		AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
		atomikosDataSourceBean.setUniqueResourceName("XADBMS");
					
		EmbeddedXADataSource40 dataSource = new EmbeddedXADataSource40();
		dataSource.setDatabaseName("rewardsdb");
		dataSource.setCreateDatabase("create");
		
		atomikosDataSourceBean.setXaDataSource(dataSource);
		atomikosDataSourceBean.init();
		return atomikosDataSourceBean;
	}
	
	@Bean(destroyMethod="close") ConnectionFactory connectionFactory() throws Exception {
		AtomikosConnectionFactoryBean atomikosConnectionFactoryBean = new AtomikosConnectionFactoryBean();
		atomikosConnectionFactoryBean.setUniqueResourceName("QUEUE_BROKER");
		
		ActiveMQXAConnectionFactory connectionFactory = new ActiveMQXAConnectionFactory();
		connectionFactory.setBrokerURL("vm:broker:(tcp://localhost:61616)?persistent=false");
		RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
		redeliveryPolicy.setMaximumRedeliveries(3);
		connectionFactory.setRedeliveryPolicy(redeliveryPolicy);
		
		atomikosConnectionFactoryBean.setXaConnectionFactory(connectionFactory);
		
		atomikosConnectionFactoryBean.init();
		return atomikosConnectionFactoryBean;
	}
	
	@Bean
	public DataSourceInitializer dsInitializer(DataSource dataSource) {
	    ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
	    databasePopulator.addScripts(new ClassPathResource("rewards/messaging/server/schema.sql"),
	    							 new ClassPathResource("rewards/messaging/server/test-data.sql"));
	    databasePopulator.setIgnoreFailedDrops(true);

	    DataSourceInitializer initializer = new DataSourceInitializer();
	    initializer.setDataSource(dataSource);
	    initializer.setDatabasePopulator(databasePopulator);

	    return initializer;
	}
	
	@Bean JdbcTemplate jdbcTemplate() throws Exception {
		return new JdbcTemplate(dataSource());
	}
	
}
