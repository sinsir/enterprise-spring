package rewards.messaging;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rewards.*;

/** java configuration **/
@SpringApplicationConfiguration(classes={SpringIntegrationIdempotentReceiverConfig.class,
							   SpringIntegrationInfraConfig.class,
							   SystemTestConfig.class},
		              inheritLocations = false)
@RunWith(SpringJUnit4ClassRunner.class)
public class RewardMessagingIntegrationJavaConfigurationTests extends RewardMessagingIntegrationTests {

}
