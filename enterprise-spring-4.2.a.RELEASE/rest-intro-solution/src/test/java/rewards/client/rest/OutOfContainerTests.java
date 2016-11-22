package rewards.client.rest;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(locations={"/system-test-config.xml", "file:./src/main/webapp/WEB-INF/rest-servlet.xml"} )
public class OutOfContainerTests {

	String diningXml = "<dining xmlns=\"http://www.springsource.com/dining-request\">" +
			"<amount value=\"100.00\"/>" +
			"<creditcard number=\"1234123412341234\"/>" +
			"<merchant number=\"1234567890\"/>" +
			"<timestamp> " +
			"<date>2009-04-21</date>" +
			"</timestamp>" +
			"</dining>";

	String diningJson = 
			"{ " +
			"\"amount\": {\"value\": \"100.00\"}, " +
			"\"creditcard\": {\"number\": \"1234123412341234\"}, " +
			"\"merchant\": {\"number\": \"1234567890\"}, " +
			"\"timestamp\": {\"date\": \"2009-04-21\"} " +
			"}";
	
	@Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() {
    	//	Build mockMvc before each test:
    	mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    
    public void test(String input, MediaType mediaType) throws Exception {

		String txId = UUID.randomUUID().toString();

		//	POST a Dining request:
    	MvcResult result = 
	    	mockMvc.perform(
	    		post("/dining/{transactionId}", txId)
	    		.content(input)
	    		.contentType(mediaType) 
	    		)
	    		.andExpect(status().isCreated())
	    		.andReturn( );
    	
    	//	Get the location of the newly created Reward resource:
    	String location = result.getResponse().getHeader("location");
    	assertNotNull(location);
    	
    	//	GET the Reward resource:
    	result = 
    		mockMvc.perform(
    			get(location)
    			.accept(MediaType.APPLICATION_JSON)
    		)
    			.andExpect(status().isOk())
    			
    			//	Note:  The following require jayway, slf4j, and json-smart on the classpath:
    			.andExpect(jsonPath("$.transactionId").value(txId))
    			.andExpect(jsonPath("$.accountNumber").value("123456789"))
    			.andExpect(jsonPath("$.merchantNumber").value("1234567890"))
    			
    			.andReturn();

    	String conf = result.getResponse().getContentAsString();
    	System.out.println(conf);
    	
    	assertTrue( conf.indexOf(txId)>0 );
    	assertTrue( conf.indexOf("8.0")>0 );
    	assertTrue( conf.indexOf("100.0")>0 );
    	assertTrue( conf.indexOf("1234567890")>0 );
    }

    //	Test with XML Dining input:
    @Test
    public void testXml() throws Exception {
    	test(diningXml, MediaType.APPLICATION_XML);
    }
    
    //	Test with JSON Dining input:
    @Test
    public void testJson() throws Exception {
    	test(diningJson, MediaType.APPLICATION_JSON);
    }
    
	
}
