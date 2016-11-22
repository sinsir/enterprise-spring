package rewards.internal.reward;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import rewards.AccountContribution;
import rewards.Dining;
import rewards.RewardConfirmation;

/**
 * JDBC implementation of a reward repository that records the result of a reward transaction by inserting a reward
 * confirmation record.
 */
public class JdbcRewardRepository extends NamedParameterJdbcDaoSupport implements RewardRepository {

	public RewardConfirmation confirmReward(final AccountContribution contribution, final Dining dining) {
		final String sql = "insert into T_REWARD (REWARD_AMOUNT, REWARD_DATE, ACCOUNT_NUMBER, DINING_MERCHANT_NUMBER, DINING_DATE, DINING_AMOUNT) values (?, ?, ?, ?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().getJdbcOperations().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setBigDecimal(1, contribution.getAmount().asBigDecimal());
				ps.setTimestamp(2, new Timestamp(new Date().getTime()));
				ps.setString(3, contribution.getAccountNumber());
				ps.setString(4, dining.getMerchantNumber());
				ps.setTimestamp(5, new Timestamp(dining.getDate().asDate().getTime()));
				ps.setBigDecimal(6, dining.getAmount().asBigDecimal());
				return ps;
			}
		}, keyHolder);

		return new RewardConfirmation(keyHolder.getKey().toString(), contribution);
	}

}