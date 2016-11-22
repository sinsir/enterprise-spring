package rewards.testdb;

import org.springframework.dao.EmptyResultDataAccessException;

import rewards.internal.account.Account;
import rewards.internal.account.AccountRepository;

import common.money.Percentage;

/**
 * A dummy account repository implementation. Has a single Account "Keith and Keri Donald" with two beneficiaries
 * "Annabelle" (50% allocation) and "Corgan" (50% allocation) associated with credit card "1234123412341234".
 * 
 * Stubs facilitate unit testing. An object needing an AccountRepository can work with this stub and not have to bring
 * in expensive and/or complex dependencies such as a Database. Simple unit tests can then verify object behavior by
 * considering the state of this stub.
 */
public class StubAccountRepository implements AccountRepository {

	private Account account;

	public StubAccountRepository() {
		Account account = new Account("123456789", "Keith and Keri Donald");
		account.addBeneficiary("Annabelle", Percentage.valueOf("50%"));
		account.addBeneficiary("Corgan", Percentage.valueOf("50%"));
		this.account = account;
	}

	@Override
	public Account findByCreditCard(String creditCardNumber) {
		if ("1234123412341234".equals(creditCardNumber)) {
			return this.account;
		}
		throw new EmptyResultDataAccessException(1);
	}
	
	@Override
	public Account findByAccountNumber(String accountNumber) {
		if ("123456789".equals(accountNumber)) {
			return this.account;
		}
		throw new EmptyResultDataAccessException(1);
	}

	@Override
	public void updateBeneficiaries(Account account) {
		// nothing to do, everything is in memory
	}
}