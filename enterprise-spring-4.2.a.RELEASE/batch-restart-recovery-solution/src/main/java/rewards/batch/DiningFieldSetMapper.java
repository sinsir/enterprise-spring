package rewards.batch;

import java.util.Date;

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
		Date date = fieldSet.readDate("date", "yyyy-MM-dd");
		return Dining.createDining(amount, creditCardNumber, merchantNumber, date);
	}

}
