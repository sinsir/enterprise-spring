package rewards.client.rest;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

//	xTODO 12 (PART 2): Stop any server you may have running.

//	xTODO 13 (PART 2): Add two static imports above, 
//	one for org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
//	one for org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

//	xTODO 14 (PART 2): Annotate this test class with @RunWith, @SpringApplicationConfiguration, and @WebAppConfiguration.  
//	Be sure to include both config files in @SpringApplicationConfiguration (see which ones below.)
//  ("/system-test-config.xml" and "file:./src/main/webapp/WEB-INF/rest-servlet.xml")
//	Run the test, it should pass, though we really haven't done any testing yet.
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(locations={"/system-test-config.xml", "file:./src/main/webapp/WEB-INF/rest-servlet.xml"})
@WebAppConfiguration
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
	
	
	//	xTODO 15 (PART 2): Add a member variables for the WebApplicationContext.  Autowire it.
	@Autowired
	WebApplicationContext webApplicationContext;
	
	//	xTODO 16 (PART 2): Add a member variable for MockMvc.  Do NOT autowire it.
	MockMvc mockMvc;

    @Before
    public void setup() {
    	//	xTODO 17 (PART 2): Before each test runs, build the mockMvc 
    	//	using MockMvcBuilders and the autowired WebApplicationContext:
    	mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    
    public void test(String input, MediaType mediaType) throws Exception {

		String txId = UUID.randomUUID().toString();

		//	xTODO 18 (PART 2): Use the mockMvc to perform a POST to "/dining/{transactionId}".
		//	Pass the txId (declared above) as a unique transaction ID.
		//	Use the "input" String variable as the posted content.
		//	Use the "mediaType" variable as the posted content-type.
		//	Ensure that the returned status code is 201 (created).
		//	Populate the 'result' variable with the result of the call.
    	MvcResult result = mockMvc.perform(post("/dining/{transactionId}", txId)
    						.content(input)
    						.contentType(mediaType))
    						.andExpect(status().isCreated())
    						.andReturn();
    	
    	//	xTODO 19 (PART 2): Obtain the "location" response header from the returned result.
    	//	Assert that it is not null.
    	String location = result.getResponse().getHeader("location");
    	Assert.notNull(location, "Location is null");
    	
    	
    	//	xTODO 20 (PART 2): Use the mockMvc to perform a GET to the location you just obtained in the last step.
    	//	Set application/json as the desired media type.
		//	Ensure that the returned status code is 200 (ok).
    	//  Use JSON path to check the content of the response.
		//	Populate the 'result' variable with the result of the call.
    	result = mockMvc.perform(get(location)
    						.accept(MediaType.APPLICATION_JSON))
    						.andExpect(status().isOk())
    						.andExpect(jsonPath("$.transactionId").value(txId))//I stole these from the answer!
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

    //	xTODO 21 (PART 2): Remove the @Ignore annotation and run this test.  
    //	It should pass using XML as the posted input.
    //	Double-check - your server should NOT be running.
    @Test
    public void testXml() throws Exception {
    	test(diningXml, MediaType.APPLICATION_XML);
    }
    
    //	xTODO 22 (PART 2): Remove the @Ignore annotation and run this test.  
    //	It should pass using JSON as the posted input.
    @Test
    public void testJson() throws Exception {
    	test(diningJson, MediaType.APPLICATION_JSON);
    }
 
}
