package rewardapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import rewardCentral.RewardCentral;

@Slf4j
@Configuration
public class Config {

	@Bean
	public RewardCentral getRewardCentral() {
		log.info("@Configuration create RewardCentral");
		return new RewardCentral();
	}
		
}
