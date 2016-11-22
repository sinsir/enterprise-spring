package rewards.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;

import rewards.Dining;
import rewards.RewardNetwork;

@Endpoint
public class RewardNetworkEndpoint {

	private static final String NAMESPACE_URI = "http://www.springsource.com/reward-network";

	private RewardNetwork rewardNetwork;

	@Autowired
	public RewardNetworkEndpoint(RewardNetwork rewardNetwork) {
		this.rewardNetwork = rewardNetwork;
	}
	
	// TODO 06: Create a new method to handle the SOAP request payload.
	//	Use an annotation to cause Spring WS to route dining requests to this method.
	//	The method should take a RewardAccountForDiningRequest parameter.
	//	Use an annotation to instruct Spring WS to unmarshalled the request payload.
	//	Processes the request:
	//		Create a Dining object (hint: see Dining.createDining())
	//		Call rewardNetwork.rewardAccountFor() with this dining object.
	//		Assign the return value to local variable.  
	//		Create a new RewardAccountForDiningResponse with this variable
	//		Set the account number and confirmation number properties on this response object.
	//		Return the response object.  Use an annotation to instruct Spring WS to marshall the response payload.


}