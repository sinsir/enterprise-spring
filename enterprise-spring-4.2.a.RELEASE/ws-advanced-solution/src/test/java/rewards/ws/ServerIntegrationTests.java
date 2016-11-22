package rewards.ws;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.clientOrSenderFault;
import static org.springframework.ws.test.server.ResponseMatchers.payload;

import javax.xml.transform.Source;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(locations={"file:src/main/webapp/WEB-INF/rewardNetwork-servlet-config.xml",
		              " file:src/main/webapp/WEB-INF/rewardNetwork-webapp-config.xml"})
public class ServerIntegrationTests {
    @Autowired ApplicationContext applicationContext;
    MockWebServiceClient mockClient;
    
    @Before
    public void createClient() {
      mockClient = MockWebServiceClient.createClient(applicationContext);
    }

    @Test
    public void validRequest() {
        Source requestPayload = new StringSource(
          "<ns2:rewardAccountForDiningRequest xmlns:ns2=\"http://www.springsource.com/reward-network\" amount=\"100.00\" creditCardNumber=\"1234123412341234\" merchantNumber=\"1234567890\"/>");
        Source responsePayload = new StringSource(
          "<ns2:rewardAccountForDiningResponse xmlns:ns2=\"http://www.springsource.com/reward-network\" accountNumber=\"123456789\" amount=\"8.00\" confirmationNumber=\"1\"/>");
        mockClient.sendRequest(withPayload(requestPayload))
          .andExpect(payload(responsePayload));
    }
    
    @Test
    public void invalidRequestWithoutCreditCardNumber() {
        Source requestPayload = new StringSource(
          "<ns2:rewardAccountForDiningRequest xmlns:ns2=\"http://www.springsource.com/reward-network\" amount=\"100.00\" merchantNumber=\"1234567890\"/>");
        mockClient.sendRequest(withPayload(requestPayload))
          .andExpect(clientOrSenderFault());
    }
    
    @Test
    public void invalidRequestWithUnknownCreditCardNumber() {    	
        Source requestPayload = new StringSource(
          "<ns2:rewardAccountForDiningRequest xmlns:ns2=\"http://www.springsource.com/reward-network\" creditCardNumber=\"1234123412341235\" amount=\"100.00\" merchantNumber=\"1234567890\"/>");
        mockClient.sendRequest(withPayload(requestPayload))
          .andExpect(clientOrSenderFault());
    }
    
}