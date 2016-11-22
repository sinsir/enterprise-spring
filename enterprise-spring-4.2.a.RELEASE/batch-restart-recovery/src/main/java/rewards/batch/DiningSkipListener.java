package rewards.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.core.listener.SkipListenerSupport;

//	xTODO 09a: Note the implementation of this DiningSkipListener.  
@SuppressWarnings("rawtypes")
public class DiningSkipListener extends SkipListenerSupport {

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public void onSkipInRead(Throwable t) {
		logger.warn("Skipped item because of " + t.getMessage());
	}

}
