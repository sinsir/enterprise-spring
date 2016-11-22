package rewards.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/* xTODO 10: Annotate with @ResponseStatus to return a 409 Conflict when thrown from a controller method.
			Run the test again and make sure both the test methods passes. */

@SuppressWarnings("serial")
@ResponseStatus(code=HttpStatus.CONFLICT)
public class DiningAlreadyProcessedException extends RuntimeException {

}
