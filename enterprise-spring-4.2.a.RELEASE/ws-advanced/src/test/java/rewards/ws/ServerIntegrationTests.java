package rewards.ws;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.test.server.MockWebServiceClient;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(locations={"file:src/main/webapp/WEB-INF/rewardNetwork-servlet-config.xml",
		              "file:src/main/webapp/WEB-INF/rewardNetwork-webapp-config.xml"})
public class ServerIntegrationTests {
    MockWebServiceClient mockClient;
    
    @Before
    public void createClient() {
       
    	/* TODO 03: Autowire the ApplicationContext into a field.  Use it
    	 * 			to create the MockWebServiceClient. */
    	
    }

    @Test
    public void validRequest() {
    
    	/* TODO 04: Create Source variables for a valid request and response pair
    	 * 			and verify that the request results in the given response.
    	 * 			Hint: the logging we added earlier provides good example 
    	 * 			XML for requests and responses.
    	 * 			Run the test and notice that it fails.  Can you tell
    	 * 			why the failure occurs?  */ 
    }
    
    @Test
    public void invalidRequestWithoutCreditCardNumber() {
        
    	/* TODO 06: Using the previous test as a guide, create a request that is
    	 * 	MISSING the creditCardNumber attribute.  After calling the server,
    	 * 	assert that the returned fault is a 'Client' or 'Sender' fault.
    	 * 	Run this test, it should fail as we have not yet implemented
    	 * 	server-side schema validation. */
    }
    
    @Test
    public void invalidRequestWithUnknownCreditCardNumber() {
        
    	/* TODO 08: Using the original test as a guide, create a test with an
    	 * 	INVALID creditCardNumber attribute value.  After calling the server,
    	 * 	assert that the returned fault is a 'Client' or 'Sender' fault.
    	 * 	Run this test, it should fail as we have not yet implemented
    	 * 	server-side exception mapping.  Note the exception thrown. */
    }
    
}