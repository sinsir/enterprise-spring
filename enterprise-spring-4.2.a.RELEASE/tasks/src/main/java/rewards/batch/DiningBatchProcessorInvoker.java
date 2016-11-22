package rewards.batch;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

public class DiningBatchProcessorInvoker {
	private final Resource batchInput;
	private final DiningBatchProcessor batchProcessor;
	private static final Logger log = Logger.getLogger(DiningBatchProcessorImpl.class);

	public DiningBatchProcessorInvoker(DiningBatchProcessor batchProcessor, Resource batchInput) {
		this.batchProcessor = batchProcessor;
		this.batchInput = batchInput;
	}

	public void invoke() throws Exception {
		int count = batchProcessor.processBatch(batchInput);
		log.info("processed " + count + " batch records");
	}
}
