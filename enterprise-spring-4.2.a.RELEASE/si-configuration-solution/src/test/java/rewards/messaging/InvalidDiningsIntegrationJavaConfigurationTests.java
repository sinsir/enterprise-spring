package rewards.messaging;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rewards.TestContextConfig;

/** java configuration **/
@SpringApplicationConfiguration(classes={TestContextConfig.class}, inheritLocations = false)
@RunWith(SpringJUnit4ClassRunner.class)
public class InvalidDiningsIntegrationJavaConfigurationTests extends InvalidDiningsIntegrationTests {

}
