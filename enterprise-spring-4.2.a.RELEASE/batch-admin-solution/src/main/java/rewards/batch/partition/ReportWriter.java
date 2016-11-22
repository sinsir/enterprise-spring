package rewards.batch.partition;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import rewards.RewardConfirmation;

public class ReportWriter implements ItemWriter<RewardConfirmation> {

	private int count;
	
	private JdbcTemplate jdbcTemplate;
	
	
	private Logger logger = Logger.getLogger(getClass());

	public ReportWriter(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void write(final List<? extends RewardConfirmation> items) throws Exception {
		final Iterator<? extends RewardConfirmation> iterator = items.iterator();
		jdbcTemplate.batchUpdate("insert into t_report (text) values(?)", new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, iterator.next().toString());
				// pause to slow down the job
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			@Override
			public int getBatchSize() {
				return items.size();
			}
		});
		
		logger.debug("wrote " + items.size() 
				+ " confirmations, last confirmation nr = " 
				+ items.get(items.size() - 1).getConfirmationNumber());
		count += items.size();
		
	}

}
