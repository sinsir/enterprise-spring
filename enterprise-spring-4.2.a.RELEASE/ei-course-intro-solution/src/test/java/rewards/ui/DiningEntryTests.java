package rewards.ui;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import rewards.AccountContribution;
import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.RewardNetwork;
import rewards.AccountContribution.Distribution;

import common.money.MonetaryAmount;

/**
 * Unit tests for {@link DiningEntry}.
 */
public class DiningEntryTests {

	private static final PrintStream OUTPUT_STREAM = new PrintStream(new ByteArrayOutputStream());
	private static final PrintStream ERROR_STREAM = new PrintStream(new ByteArrayOutputStream());

	private static final String VALID_AMOUNT = "50";
	private static final String VALID_CC = "1234123412341234";
	private static final String VALID_MERCHANT = "1234567890";

	private static final String INVALID_AMOUNT = "-1";
	private static final String INVALID_CC = "123";
	private static final String INVALID_MERCHANT = "456";

	private static final String RETURN = "\n";

	private static final String DO_NOT_ENTER_ANOTHER = "n" + RETURN;
	private static final String DO_ENTER_ANOTHER = "y" + RETURN;

	private static final String VALID_USER_INPUT =
		VALID_AMOUNT + RETURN +
		VALID_CC + RETURN +
		VALID_MERCHANT + RETURN;

	private static final Dining VALID_DINING =
		Dining.createDining(VALID_AMOUNT, VALID_CC, VALID_MERCHANT);

	private static final String INVALID_USER_INPUT =
		INVALID_AMOUNT + RETURN +
		INVALID_CC + RETURN +
		INVALID_MERCHANT + RETURN;

	private static final Dining INVALID_DINING =
		Dining.createDining(INVALID_AMOUNT, INVALID_CC, INVALID_MERCHANT);


	private static final RewardConfirmation DUMMY_CONFIRMATION =
		new RewardConfirmation("1",
				new AccountContribution("123",
						MonetaryAmount.valueOf("20"), new HashSet<Distribution>()));


	/**
	 * Tests that valid input produces a valid {@link Dining} object and that the Dining
	 * object gets passed as a parameter to the {@link RewardNetwork} service.
	 */
	@Test
	public void testValidDiningEntry() throws IOException {

		// create a mock implementation of the RewardNetwork interface
		RewardNetwork rewardNetwork = mock(RewardNetwork.class);

		// tell the mock to return a dummy RewardConfirmation whenever rewardAccountFor() is called
		when(rewardNetwork.rewardAccountFor(any(Dining.class))).thenReturn(DUMMY_CONFIRMATION);

		// now create an instance of the class we want to test, and provide the mock dependency
		DiningEntry diningEntry = new DiningEntry(rewardNetwork);

		// configure the DiningEntry instance with a predefined stream of valid user input
		diningEntry.setInputStream(new ByteArrayInputStream(
				(VALID_USER_INPUT + DO_NOT_ENTER_ANOTHER).getBytes()));

		// provide byte-array backed output and error streams to avoid console output during testing
		diningEntry.setOutputStream(OUTPUT_STREAM);
		diningEntry.setErrorStream(ERROR_STREAM);

		// emulate the callback that will be received from the Spring container
		diningEntry.start();

		// finally, verify that rewardAccountFor was called, and called with the expected Dining object
		verify(rewardNetwork).rewardAccountFor(VALID_DINING);
	}

	/**
	 * Tests that invalid input is handled gracefully by allowing the user to optionally
	 * re-enter the data and try again.
	 *
	 * Also tests that the 'diningEntryCount' and 'diningErrorEntryCount' properties
	 * are incremented correctly.
	 */
	@Test
	public void testInvalidDiningEntryFollowedByValidDiningEntry() throws IOException {
		RewardNetwork rewardNetwork = mock(RewardNetwork.class);

		when(rewardNetwork.rewardAccountFor(INVALID_DINING)).thenThrow(new EmptyResultDataAccessException(1));
		when(rewardNetwork.rewardAccountFor(VALID_DINING)).thenReturn(DUMMY_CONFIRMATION);

		DiningEntry diningEntry = new DiningEntry(rewardNetwork);
		diningEntry.setInputStream(new ByteArrayInputStream(
				(INVALID_USER_INPUT + DO_ENTER_ANOTHER +
				 VALID_USER_INPUT + DO_NOT_ENTER_ANOTHER).getBytes()));
		diningEntry.setOutputStream(OUTPUT_STREAM);
		diningEntry.setErrorStream(ERROR_STREAM);

		assertThat(diningEntry.getDiningEntryCount(), CoreMatchers.equalTo(0));
		assertThat(diningEntry.getDiningEntryErrorCount(), CoreMatchers.equalTo(0));

		diningEntry.start();

		verify(rewardNetwork, times(1)).rewardAccountFor(INVALID_DINING);
		verify(rewardNetwork, times(1)).rewardAccountFor(VALID_DINING);

		assertThat("diningEntryCount was not incremented",
				diningEntry.getDiningEntryCount(), CoreMatchers.equalTo(1));
		assertThat("diningEntryErrorCount was not incremented",
				diningEntry.getDiningEntryErrorCount(), CoreMatchers.equalTo(1));
	}

}
