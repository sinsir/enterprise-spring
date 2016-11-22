package rewards.messaging.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public class ConfirmationLister {
	private JdbcTemplate jdbcTemplate;

	public ConfirmationLister(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<String> listAllConfirmations() {
		List<String> confirmations = new ArrayList<>();
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from T_REWARD");
		for (Map<String, Object> map : list) {
			StringBuilder b = new StringBuilder();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				b.append(entry.getKey())
				 .append('=')
				 .append(entry.getValue())
				 .append(", ");
			}
			confirmations.add(b.toString());
		}
		return confirmations;
	}

	public int getNrOfConfirmations() {
		return jdbcTemplate.queryForObject("select count(0) from T_REWARD", Integer.class);
	}
}
