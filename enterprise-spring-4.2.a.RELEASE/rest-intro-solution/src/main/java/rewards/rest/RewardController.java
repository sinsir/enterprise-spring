package rewards.rest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;

import javax.sql.DataSource;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import rewards.oxm.ObjectFactory;
import rewards.oxm.Reward;

@Controller
public class RewardController {
	private JdbcTemplate jdbcTemplate;  // query directly from controller
	
	@Autowired
	public void initJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@RequestMapping(value="/rewards/{confirmationNumber}", method=GET)
	public @ResponseBody Reward getReward(@PathVariable String confirmationNumber) {
		return findRewardByConfirmationNumber(confirmationNumber);
	}
	
	@ExceptionHandler(EmptyResultDataAccessException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void handleEmptyResult() {
	}

	/** 
	 * Retrieves the Reward with the given confirmationNumber.
	 * 
	 * @param confirmationNumber
	 * @return Reward with the given confirmationNumber
	 * @throws EmptyResultDataAccessException if no such Reward exists
	 */
	private Reward findRewardByConfirmationNumber(String confirmationNumber) throws EmptyResultDataAccessException {
		return jdbcTemplate.queryForObject("select * from T_REWARD where CONFIRMATION_NUMBER = ?", 
				new RewardRowMapper(), confirmationNumber);
	}
		
	private static class RewardRowMapper implements RowMapper<Reward> {
		public Reward mapRow(ResultSet rs, int rowNum) throws SQLException {
			Reward reward = new ObjectFactory().createReward();
			reward.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
			reward.setConfirmationNumber(rs.getString("CONFIRMATION_NUMBER"));
			reward.setTransactionId(rs.getString("TRANSACTION_ID"));
			reward.setDiningAmount(rs.getBigDecimal("DINING_AMOUNT"));
			reward.setDiningDate(toXMLCalendar(rs.getDate("DINING_DATE")));
			reward.setMerchantNumber(rs.getString("DINING_MERCHANT_NUMBER"));
			reward.setRewardAmount(rs.getBigDecimal("REWARD_AMOUNT"));
			reward.setRewardDate(toXMLCalendar(rs.getDate("REWARD_DATE")));
			return reward;
		}
		
		private XMLGregorianCalendar toXMLCalendar(Date date) {
			GregorianCalendar gcal = new GregorianCalendar();
			gcal.setTime(date);
			XMLGregorianCalendar cal;
			try {
				cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
			} catch (DatatypeConfigurationException e) {
				throw new RuntimeException(e);
			}
			cal.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
			return cal;
		}
	}
}
