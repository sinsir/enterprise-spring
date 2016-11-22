package rewards.batch.partition;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

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
				                   cal.get(MONTH) + 1,
				                   cal.get(DAY_OF_MONTH),
				                   cal.get(YEAR));
				                   
	}

}
