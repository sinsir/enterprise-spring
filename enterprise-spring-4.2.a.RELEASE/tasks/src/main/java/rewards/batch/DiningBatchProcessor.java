package rewards.batch;
import java.io.IOException;

import org.springframework.core.io.Resource;


public interface DiningBatchProcessor {

	int processBatch(Resource batchInput) throws IOException;

}
