package rewards.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.CONFLICT, reason="Dining has already been processed")
public class DiningAlreadyProcessedException extends RuntimeException {

}
