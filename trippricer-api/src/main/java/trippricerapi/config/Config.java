package trippricerapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import tripPricer.TripPricer;

@Slf4j
@Configuration
public class Config {

	@Bean
	public TripPricer getTripPricer() {
		log.info("@Configuration create TripPricer");
		return new TripPricer();
	}
		
}
