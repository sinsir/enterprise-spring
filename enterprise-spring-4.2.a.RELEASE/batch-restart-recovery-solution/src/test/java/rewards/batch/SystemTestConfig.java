package rewards.batch;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

@Configuration
@Import({BatchExecutionConfig.class, BatchJobConfig.class})
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
