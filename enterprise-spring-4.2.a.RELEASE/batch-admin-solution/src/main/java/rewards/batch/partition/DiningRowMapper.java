package rewards.batch.partition;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.springframework.jdbc.core.RowMapper;

import rewards.Dining;

public class DiningRowMapper extends Object implements RowMapper<Dining> {

	@Override
	public Dining mapRow(ResultSet rs, int rowNum) throws SQLException {
		Date date = rs.getDate("DINING_DATE");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return Dining.createDining(rs.getString("AMOUNT"), 
				                   rs.getString("CC_NUMBER"),
				                   rs.getString("MERCHANT"),
				                   cal.get(Calendar.MONTH) + 1,
				                   cal.get(Calendar.DAY_OF_MONTH),
				                   cal.get(Calendar.YEAR));
				                   
	}

}
