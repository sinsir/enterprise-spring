package rewards.rest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.net.URI;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import common.datetime.SimpleDate;
import common.money.MonetaryAmount;
import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.RewardNetwork;

@Controller
public class DiningController {
	
	@Autowired
	private RewardNetwork rewardNetwork;
	
	private JdbcTemplate jdbcTemplate;  // query directly from controller
	
	@Autowired
	public void initJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@RequestMapping(value="/dining/{txId}", method=POST)
	public ResponseEntity<Void> reward(@RequestBody rewards.oxm.Dining diningRequest, @PathVariable String txId, 
					   UriComponentsBuilder uriComponentsBuilder) {
		if (rewardExists(txId)) {
			throw new DiningAlreadyProcessedException();
		}
		Dining dining = createDining(diningRequest);
		RewardConfirmation confirmation = rewardNetwork.rewardAccountFor(dining, txId);
		
		String pathTemplate = "/rewards/{confirmationNumber}";
		URI location = uriComponentsBuilder.path(pathTemplate)
							.buildAndExpand(confirmation.getConfirmationNumber())
							.toUri();
		
		return ResponseEntity.created(location).build();
	}

	private boolean rewardExists(String txId) {
		int count = jdbcTemplate.queryForObject("select count(*) from T_REWARD where TRANSACTION_ID = ?", Integer.class, txId);
		return count == 1;
	}

	private Dining createDining(rewards.oxm.Dining diningRequest) {
		return new Dining(new MonetaryAmount(diningRequest.getAmount().getValue()),
						  diningRequest.getCreditcard().getNumber(),
						  diningRequest.getMerchant().getNumber(),
						  SimpleDate.valueOf(diningRequest.getTimestamp().getDate().toGregorianCalendar().getTime())
		);
	}
}
