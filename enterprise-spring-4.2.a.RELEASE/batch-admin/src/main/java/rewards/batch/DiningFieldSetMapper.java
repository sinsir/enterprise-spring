package rewards.batch;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.util.Calendar;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import rewards.Dining;

public class DiningFieldSetMapper implements FieldSetMapper<Dining> {

	@Override
	public Dining mapFieldSet(FieldSet fieldSet) throws BindException {
		String creditCardNumber = fieldSet.readString("creditCardNumber");
		String merchantNumber = fieldSet.readString("merchantNumber");
		String amount = fieldSet.readString("amount");
		Calendar cal = Calendar.getInstance();
		cal.setTime(fieldSet.readDate("date", "yyyy-MM-dd"));
		return SimpleDining.createDining(amount, creditCardNumber, merchantNumber, 
				cal.get(MONTH) + 1, cal.get(DAY_OF_MONTH), cal.get(YEAR));
	}

}
