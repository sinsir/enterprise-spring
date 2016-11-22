package rewards.ws.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

//	TODO 09: Run this test class while the server is running.  It should pass.

//	TODO 10: (Optional) Use TCP/IP Monitor to examine request and response (See detailed instructions.)

@SpringApplicationConfiguration(locations="classpath:rewards/ws/client/client-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SoapRewardNetworkTests {
	
	static final String NAMESPACE_URI = "http://www.springsource.com/reward-network";

	@Autowired WebServiceTemplate webServiceTemplate;
	
	@Test
	public void testWebServiceWithJAXB() throws Exception {
		// TODO 11: Implement this method by using your generated JAXB2 classes.
		//	The logic should be similar to the 'testWebServiceWithXml' method below,
		//	but you will be able to work directly with Java classes rather than DOM elements:
	}
	
	@Test
	public void testWebServiceWithXml() throws Exception {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element requestElement = document.createElementNS(NAMESPACE_URI, "rewardAccountForDiningRequest");
		requestElement.setAttribute("amount", "100.00");
		requestElement.setAttribute("creditCardNumber", "1234123412341234");
		requestElement.setAttribute("merchantNumber", "1234567890");
		DOMSource source = new DOMSource(requestElement);
		DOMResult result = new DOMResult();
		webServiceTemplate.sendSourceAndReceiveToResult(source, result);
		Element responseElement = (Element) result.getNode().getFirstChild();
		
		// assert the expected reward confirmation results
		assertNotNull(responseElement);
		
		// the account number should be '123456789'
		assertEquals("123456789", responseElement.getAttribute("accountNumber"));
		
		// the total contribution amount should be 8.00 (8% of 100.00)
		assertEquals("8.00", responseElement.getAttribute("amount"));
	}
	
}
