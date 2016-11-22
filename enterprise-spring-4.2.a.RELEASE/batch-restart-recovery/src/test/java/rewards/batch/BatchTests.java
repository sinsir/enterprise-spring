package rewards.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StartLimitExceededException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

// TODO 14 (BONUS): Use the @SystemTestConfig class for the test
// hint: @SpringApplicationConfiguration(classes = SystemTestConfig.class)
// TODO 21 (BONUS): Run the test, it should pass. It now uses the Java configuration
@SpringApplicationConfiguration(locations="classpath:system-test-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class BatchTests {
	static final String INPUT_RESOURCE_PATH = "input.resource.path";
	
	static final String INPUT_VALID = "file:diningRequests.csv";
	static final String INPUT_INVALID = "file:diningRequests-broken.csv";
	
	// Ignore errors from the expected FlatFileParseExceptions and
	// JobExecutionExceptions. Otherwise AbstractStep logs a full
	// stack-trace making the test look like it has failed.
	// xTODO 13 - if all is running nicely, set this to true for nicer output
	static final boolean SUPRRESS_EXPECTED_ERRORS = true;

	@Autowired JobLauncher launcher;
	
	@Autowired Job diningRequestsJob;

	// xTODO 07: add an @Autowired annotation to the skippingDiningRequestsJob
	@Autowired
	Job skippingDiningRequestsJob;
	
	@Before
	public void setupLogging() {
		// Setup a custom log4j Appender to ignore the FlatFileParseExceptions
		// that these tests expect to produce (otherwise a long, ugly and
		// confusing stack trace appears in the output). Similarly for the
		// JobExecutionException. A simple warning appears instead.
		TestConsoleAppender.setupAsDefault(getClass());

		System.out.println("-----"); // Separate output of each test
	}

	@Test
	//@Ignore // xTODO 04: remove this line and run the test, it should pass.
	public void regularJobSucceedsWithValidInput() throws Exception {
		JobParameters params = new JobParametersBuilder()
			.addDate("start-time", new Date())
			.addString(INPUT_RESOURCE_PATH, INPUT_VALID)
			.toJobParameters();
		JobExecution execution = launcher.run(diningRequestsJob, params);
		assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
	}

	@Test
	//@Ignore // xTODO 05: remove this line and run the test, it should pass.
			//	Examine the input file for this test and note which line contains bad input.
	public void regularJobFailsWithInvalidInput() throws Exception {
		JobParameters params = new JobParametersBuilder()
			.addDate("start-time", new Date())
			.addString(INPUT_RESOURCE_PATH, INPUT_INVALID)
			.toJobParameters();
		JobExecution execution = launcher.run(diningRequestsJob, params);
		assertEquals(ExitStatus.FAILED, execution.getExitStatus());
	}

	@Test
	//@Ignore // TODO 08: remove this line and run the test, it should pass.
	public void skippingJobSucceedsWithInvalidInput() throws Exception {
		JobParameters params = new JobParametersBuilder()
			.addDate("start-time", new Date())
			.addString(INPUT_RESOURCE_PATH, INPUT_INVALID)
			.toJobParameters();
		JobExecution execution = launcher.run(skippingDiningRequestsJob, params);
		assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
	}

	@Test
	//@Ignore // xTODO 10: remove this line and run the test, it should pass.
			//	Examine the logic in this test; it simulates running the job with 
			//	invalid input, then restarting with valid input.
	public void restartRegularJobAfterFixingInputSucceeds() throws Exception {
		final Date now = new Date();
		JobParameters invalidParams = new JobParametersBuilder()
			.addDate("start-time", now)
			.addString(INPUT_RESOURCE_PATH, INPUT_INVALID)
			.toJobParameters();
		JobParameters params1 = new ResourcePathHidingJobParameters(invalidParams.getParameters());
		JobExecution execution1 = launcher.run(diningRequestsJob, params1);
		assertEquals(ExitStatus.FAILED, execution1.getExitStatus());
		
		// re-run with valid input, pretending it's the same JobParameters to cause restart:
		JobParameters validParams = new JobParametersBuilder()
			.addDate("start-time", now)
			.addString(INPUT_RESOURCE_PATH, INPUT_VALID)
			.toJobParameters();
		JobParameters params2 = new ResourcePathHidingJobParameters(validParams.getParameters());
		JobExecution execution2 = launcher.run(diningRequestsJob, params2);
		// make sure we were indeed restarting the existing job instance, not starting a new one:
		assertEquals(execution2.getJobInstance(), execution1.getJobInstance());
		assertEquals(ExitStatus.COMPLETED, execution2.getExitStatus());
	}
	
	@Test
	//@Ignore // TODO 12: remove this line and run the test, it should pass.
			//	Examine the test logic, it verifies the restart limit behavior.
	public void exceedingRestartLimitPreventJobFromRunningAgain() throws Exception {
		JobParameters params = new JobParametersBuilder()
			.addDate("start-time", new Date())
			.addString(INPUT_RESOURCE_PATH, INPUT_INVALID)
			.toJobParameters();
		final int startLimit = 3;
		for (int i = 0; i < startLimit; i++) {
			JobExecution execution = launcher.run(diningRequestsJob, params);
			assertEquals(ExitStatus.FAILED, execution.getExitStatus());
		}
		// should exceed start limit now:
		JobExecution execution = launcher.run(diningRequestsJob, params);
		List<Throwable> failureExceptions = execution.getFailureExceptions();
		assertFalse(failureExceptions.isEmpty());
		Throwable cause = failureExceptions.get(0).getCause().getCause();
		assertEquals(StartLimitExceededException.class, cause.getClass());
	}
	
	/** 
	 * 	Tricks the JdbcJobInstanceDao into thinking we're
	 *	running the same job instance twice as it iterates the Map entries
	 *  via the keySet, since we're not using the same resource path twice for 
	 *  both runs like we would in a real-life scenario.
	 *  Direct access to get the resource path out still works, so the ItemReader
	 *  still knows what file to use.
	 */
	@SuppressWarnings("serial")
	private static class ResourcePathHidingJobParameters extends JobParameters {
		public ResourcePathHidingJobParameters(Map<String,JobParameter> parameters) {
			super(parameters);
		}
		@Override
		public Map<String, JobParameter> getParameters() {
			return new LinkedHashMap<String,JobParameter>(super.getParameters()) {
				@Override
				public Set<String> keySet() {
					Set<String> keySet = super.keySet();
					keySet.remove(INPUT_RESOURCE_PATH);
					return keySet;
				}
			};
		};
	}
}
