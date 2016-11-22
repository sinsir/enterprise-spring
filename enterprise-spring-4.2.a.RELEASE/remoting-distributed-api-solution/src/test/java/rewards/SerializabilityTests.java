package rewards;

import static org.hamcrest.CoreMatchers.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import rewards.AccountContribution.Distribution;

import common.money.MonetaryAmount;
import common.money.Percentage;

/**
 * Unit tests that prove distributed types in the rewards.* and common.*
 * packages are {@link Serializable} and ready for remoting.
 */
public class SerializabilityTests extends junit.framework.TestCase {

	/**
	 * Asserts that <var>object</var> is serializable by fully serializing and
	 * deserializing it using {@link ObjectOutputStream} and {@link ObjectInputStream}.
	 * 
	 * <p>If any part of <var>object</var> is not properly set up for serialization,
	 * i.e.: does not implement {@link java.io.Serializable}, a {@link NotSerializableException}
	 * will be thrown.
	 * 
	 * @param object
	 * @throws NotSerializableException if any node in the graph of objects under
	 *         <var>object</var> is not serializable.
	 */
	private void assertSerializable(Object object) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ObjectOutputStream outStream = new ObjectOutputStream(output);

		outStream.writeObject(object);

		InputStream input = new ByteArrayInputStream(output.toByteArray());
		ObjectInputStream inStream = new ObjectInputStream(input);

		Object object2 = inStream.readObject();

		assertEquals(object, object2);
		assertNotSame(object, sameInstance(object2));
	}

	/**
	 * Tests that {@link Dining} and all related objects implement {@link Serializable}.
	 * Dining objects are sent from the client to the server as the parameter
	 * to {@link RewardNetwork#rewardAccountFor(Dining)}.
	 */
	public void testDiningIsSerializable() throws IOException, ClassNotFoundException {
		Dining dining = Dining.createDining("50.00", "1234123412341234", "1234567890");
		assertSerializable(dining);
	}

	/**
	 * Tests that {@link RewardConfirmation} and all related objects implement {@link Serializable}.
	 * RewardConfirmation objects are sent from the server to the client in response to
	 * calls to {@link RewardNetwork#rewardAccountFor(Dining)}.
	 */
	public void testRewardConfirmationIsSerializable() throws IOException, ClassNotFoundException {
		Set<Distribution> distributions = new LinkedHashSet<Distribution>();
		distributions.add(new AccountContribution.Distribution("Annabelle", MonetaryAmount.valueOf("4.00"), Percentage.valueOf("50"), MonetaryAmount.valueOf("4.00")));
		distributions.add(new AccountContribution.Distribution("Corgan", MonetaryAmount.valueOf("4.00"), Percentage.valueOf("50"), MonetaryAmount.valueOf("4.00")));
		AccountContribution contribution = new AccountContribution("12345", MonetaryAmount.valueOf("8.00"), distributions);
		RewardConfirmation confirmation = new RewardConfirmation("1", contribution);

		assertSerializable(confirmation);
	}

}
