package rewards.batch;

import java.util.Date;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;

import common.datetime.SimpleDate;
import common.money.MonetaryAmount;

import rewards.Dining;

/**
 * Simple version of {@link Dining} that returns a double for the amount and a Date for the date. 
 * To facilitate the use of a {@link BeanPropertyItemSqlParameterSourceProvider}
 *
 */
public class SimpleDining extends Dining {

	private static final long serialVersionUID = 1L;

	public SimpleDining(MonetaryAmount amount, String creditCardNumber,
			String merchantNumber, SimpleDate date) {
		super(amount, creditCardNumber, merchantNumber, date);
	}

	/**
	 * Creates a new dining, reflecting an amount that was charged to a credit card by a merchant on the date specified.
	 * A convenient static factory method.
	 * @param amount the total amount of the dining bill as a string
	 * @param creditCardNumber the number of the credit card used to pay for the dining bill
	 * @param merchantNumber the merchant number of the restaurant where the dining occurred
	 * @param month the month of the dining event
	 * @param day the day of the dining event
	 * @param year the year of the dining event
	 * @return the dining event
	 */
	public static Dining createDining(String amount, String creditCardNumber, String merchantNumber, int month,
			int day, int year) {
		return new SimpleDining(MonetaryAmount.valueOf(amount), creditCardNumber, merchantNumber, new SimpleDate(month, day,
				year));
	}

	public double getDiningAmount() {
		return super.getAmount().asDouble();
	}
	
	public Date getDiningDate() {
		return super.getDate().asDate();
	}
}
