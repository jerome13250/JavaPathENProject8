package trippricerapi.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import rewardCentral.RewardCentral;
import tripPricer.Provider;
import tripPricer.TripPricer;


@Repository
public class TripPricerRepository {
	
	@Autowired
	private TripPricer tripPricer;
	
	public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints) {
		return tripPricer.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
	}

}
