package rewards.ws;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import rewards.RewardNetwork;
import rewards.ws.types.RewardAccountForDiningRequest;
import rewards.ws.types.RewardAccountForDiningResponse;

@SpringApplicationConfiguration(locations="file:src/main/webapp/WEB-INF/rewardNetwork-webapp-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RewardNetworkEndpointTests {

	@Autowired
	RewardNetwork rewardNetwork;
	
	@Test
	public void test() {
		RewardNetworkEndpoint endpoint = new RewardNetworkEndpoint(rewardNetwork);
		
		RewardAccountForDiningRequest request = new RewardAccountForDiningRequest();
		request.setAmount(new BigDecimal("100.00"));
		request.setCreditCardNumber("1234123412341234");
		request.setMerchantNumber("1234567890");
		
		RewardAccountForDiningResponse response = endpoint.reward(request);
		
		// assert the expected reward confirmation results
		assertNotNull(response);
		
		// the account number should be '123456789'
		assertEquals("123456789", response.getAccountNumber());
		
		// the total contribution amount should be 8.00 (8% of 100.00)
		assertEquals(new BigDecimal("8.00"), response.getAmount());

	}

}
