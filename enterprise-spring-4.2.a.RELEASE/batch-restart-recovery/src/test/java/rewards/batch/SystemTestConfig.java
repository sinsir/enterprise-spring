package rewards.batch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;


// TODO 15 (BONUS): Check the content of this configuration class
//   - it import the Spring Batch infrastructure configuration class and 
// the job configuation
//   - it declares a DataSource bean
@Configuration
@Import({BatchExecutionConfig.class,BatchJobConfig.class})
public class SystemTestConfig {

	@Bean
	public DataSource dataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.addScript("classpath:org/springframework/batch/core/schema-hsqldb.sql")
					  .addScript("classpath:db-schema.sql")
					  .addScript("classpath:db-test-data.sql")
					  .build();
	}

}
