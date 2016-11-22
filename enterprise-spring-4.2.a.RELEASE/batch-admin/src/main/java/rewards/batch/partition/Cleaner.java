package rewards.batch.partition;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class Cleaner {
	
	private JdbcTemplate jdbcTemplate;
	
	public Cleaner(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void clean() {
		jdbcTemplate.update("delete from t_dining_request");
		jdbcTemplate.update("delete from t_report");
	}

}
