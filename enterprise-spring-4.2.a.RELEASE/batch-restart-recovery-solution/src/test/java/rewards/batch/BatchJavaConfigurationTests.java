package rewards.batch;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringApplicationConfiguration(classes=SystemTestConfig.class,inheritLocations=false)
@RunWith(SpringJUnit4ClassRunner.class)
public class BatchJavaConfigurationTests extends BatchTests {
}
