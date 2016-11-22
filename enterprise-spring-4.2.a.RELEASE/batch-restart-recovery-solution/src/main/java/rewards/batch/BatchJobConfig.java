package rewards.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.SkipListenerSupport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.adapter.ItemProcessorAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.UrlResource;
import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.RewardNetwork;

import java.net.MalformedURLException;

@Configuration
@ImportResource({"classpath:app-config.xml"})
public class BatchJobConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job diningRequestsJob(Step diningRequestsStep) {
		return jobBuilderFactory.get("diningRequestsJob")
								.incrementer(new RunIdIncrementer())
								.flow(diningRequestsStep).end()

								.build();
	}

	@Bean
	public Step diningRequestsStep(ItemReader<Dining> diningRequestsReader, ItemProcessor<Dining, RewardConfirmation> processor,
								   ReportWriter reportWriter) {
		return stepBuilderFactory.get("diningRequestsStep")
								 .<Dining, RewardConfirmation>chunk(10)
				                 .reader(diningRequestsReader)
				                 .processor(processor)
	 			                 .writer(reportWriter)
								 .startLimit(3)

								 .build();
	}

	@Bean
	public Job skippingDiningRequestsJob(Step skippingDiningRequestsStep) {
		return jobBuilderFactory.get("skippingDiningRequestsJob")
								.incrementer(new RunIdIncrementer())
								.flow(skippingDiningRequestsStep).end()
								.build();
	}

	@Bean
	public Step skippingDiningRequestsStep(ItemStreamReader<Dining> diningRequestsReader, ItemProcessor<Dining, RewardConfirmation> processor,
										   ReportWriter reportWriter, SkipListenerSupport diningSkipListener) {
		return stepBuilderFactory.get("skippingDiningRequestsStep")
								 .<Dining, RewardConfirmation>chunk(10)
								 .reader(diningRequestsReader)
								 .processor(processor)
								 .writer(reportWriter)
								 .faultTolerant().skip(FlatFileParseException.class).skipLimit(1)
				                 .startLimit(3)
				                 .listener(diningSkipListener)

				                 .build();
	}

	@Bean
	public SkipListenerSupport diningSkipListener() {
		return new DiningSkipListener();
	}

	@Bean
	public ItemProcessor<Dining, RewardConfirmation> processor(RewardNetwork rewardNetwork) {
		ItemProcessorAdapter<Dining, RewardConfirmation> processorAdapter = new ItemProcessorAdapter<>();
		processorAdapter.setTargetObject(rewardNetwork);
		processorAdapter.setTargetMethod("rewardAccountFor");

		return processorAdapter;
	}

	@Bean
	@StepScope
	public ItemStreamReader<Dining> diningRequestsReader(@Value("#{jobParameters['input.resource.path']}") String inputResourcePath)
			throws MalformedURLException {
		FlatFileItemReader<Dining> reader = new FlatFileItemReader<>();
		reader.setResource(new UrlResource(inputResourcePath));
		reader.setLineMapper(new DefaultLineMapper<Dining>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames(new String[]{"creditCardNumber", "merchantNumber", "amount", "date"});
			}});
			setFieldSetMapper(new DiningFieldSetMapper());
		}});
		return reader;
	}

	@Bean
	public ReportWriter reportWriter() {
		return new ReportWriter();
	}

}
