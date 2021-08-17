package commons.test;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
public class testController {

	testController(){
		log.info("Inside constructor testController");
	}
	
	


}
