package rewards.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.RewardNetwork;

/**
 * Interactive {@link Dining} entry object.
 */

//	TODO 05: Add Spring's @ManagedResource annotation 
//	to enable instances of this class to be registered as JMX MBeans

//  TODO 06: Use Spring's @ManagedAttribute annotation to enable read only access for the property diningEntryCount
//  TODO 07: Use Spring's @ManagedAttribute annotation to enable read only access for the property diningEntryErrorCount
public class DiningEntry {

	private final RewardNetwork rewardNetwork;
	private BufferedReader inputReader;
	private InputStream inputStream = System.in;
	private PrintStream outputStream = System.out;
	private PrintStream errorStream = System.err;
	private int diningEntryCount;
	private int diningEntryErrorCount;

	/**
	 * Create a new {@link DiningEntry} object.
	 * 
	 * @param rewardNetwork service layer to which new {@link Dining} transactions will be passed
	 * @param inputStream stream from which to read user input
	 * @param outputStream stream to which prompts and feedback will be written
	 */
	public DiningEntry(RewardNetwork rewardNetwork) {
		this.rewardNetwork = rewardNetwork;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setOutputStream(PrintStream outputStream) {
		this.outputStream = outputStream;
	}

	public void setErrorStream(PrintStream errorStream) {
		this.errorStream = errorStream;
	}

	public int getDiningEntryCount() {
		return diningEntryCount;
	}

	public int getDiningEntryErrorCount() {
		return diningEntryErrorCount;
	}

	public void start() throws IOException {
		if (this.inputReader == null)
			this.inputReader = new BufferedReader(new InputStreamReader(inputStream));

		acceptDiningInput();
	}


	/**
	 * Start accepting user input for creating new {@link Dining} transactions.
	 * Loops until user specifies 'n' when prompted about entering another transaction.
	 */
	public void acceptDiningInput() throws IOException {

		String enterDining;

		outputStream.printf("Welcome to the Reward Network dining entry UI\n\n");
		outputStream.printf("Please enter the following information to record and process a new dining transaction\n");

		do {
			try {
				outputStream.printf("\n");

				String amount = prompt("Dining amount");
				String creditCardNumber = prompt("Member credit card number");
				String merchantNumber = prompt("Merchant number");

				Dining dining = Dining.createDining(amount, creditCardNumber, merchantNumber);
				outputStream.printf("\n%s\n", dining);

				outputStream.printf("\nRewarding account for dining via RewardNetwork...\n");

				
				// TODO 04: Modify the line of code below
				//	to submit the 'dining' object to the RewardNetwork service.
				//	Assign the results to the confirmation object.  
				//	When complete, re-run DiningEntryTests.  It should now pass.
				RewardConfirmation confirmation = null;

				outputStream.printf("Result: %s\n\n", confirmation);

				diningEntryCount++;

			} catch (Exception ex) {
				errorStream.printf("\ndining entry error! message was: %s\n\n", ex.getMessage());
				diningEntryErrorCount++;
			}

			do {
				enterDining = prompt("Would you like to enter another Dining transaction? [y/n]");
			} while (!enterDining.equalsIgnoreCase("y") && !enterDining.equalsIgnoreCase("n"));

		} while (enterDining.equalsIgnoreCase("y"));

		outputStream.printf("\nThank you, goodbye.\n");
	}

	/**
	 * Provide a user prompt; accept and return user input.
	 */
	private String prompt(String desiredInput) throws IOException {
		outputStream.print(desiredInput + ": ");
		return inputReader.readLine();
	}

}
