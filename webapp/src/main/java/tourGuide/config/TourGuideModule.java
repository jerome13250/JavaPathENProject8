package tourGuide.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gpsUtil.GpsUtil;
import rewardCentral.RewardCentral;
import tourGuide.repository.GpsProxy;
import tourGuide.service.RewardsService;

@Configuration
public class TourGuideModule {
	
	@Autowired
	GpsProxy gpsProxy;
	
	@Bean
	public RewardsService getRewardsService() {
		return new RewardsService(gpsProxy, getRewardCentral());
	}
	
	@Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}

	
}
