package rewards.batch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import rewards.Dining;
import rewards.RewardNetwork;


public class DiningBatchProcessorImpl implements DiningBatchProcessor {

	private final RewardNetwork rewardNetwork;

	public DiningBatchProcessorImpl(RewardNetwork rewardNetwork) {
		this.rewardNetwork = rewardNetwork;
	}

	@Override
	public int processBatch(Resource csvInput) throws IOException {
		BufferedReader inputReader =
			new BufferedReader(new InputStreamReader(csvInput.getInputStream()));

		ExecutorService executor = createExecutorService();

		int count = 0;
		String csvRecord;
		while ((csvRecord = inputReader.readLine()) != null) {
			final Dining dining = createDiningFromCsv(csvRecord);

			executor.execute(new Runnable() {
				@Override
				public void run() {
					rewardNetwork.rewardAccountFor(dining);
				}
			});

			count++;
		}

		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		return count;
	}

	/**
	 * Creates a new cached thread pool. Can be overriden in XML using a <lookup-method>
	 */
	public ExecutorService createExecutorService() {
		return Executors.newCachedThreadPool();
	}

	private Dining createDiningFromCsv(String csvRecord) {
		String[] fields = StringUtils.commaDelimitedListToStringArray(csvRecord);
		String amount = fields[0];
		String creditCardNumber = fields[1];
		String merchantNumber = fields[2];
		String[] dateTokens = StringUtils.delimitedListToStringArray(fields[3], "/");
		int year = Integer.valueOf(dateTokens[0]);
		int month = Integer.valueOf(dateTokens[1]);
		int day = Integer.valueOf(dateTokens[2]);

		return Dining.createDining(amount, creditCardNumber, merchantNumber, month, day, year);
	}

}
