package tourGuide.repository;

import java.util.List;
import java.util.UUID;

import tripPricer.Provider;

public interface TripPricerProxy {

	/**
	 * Get a list of 5 providers. 
	 * @param apiKey the key to the API
	 * @param attractionId attraction id
	 * @param adults number of adults
	 * @param children number of children
	 * @param nightsStay number of night stay
	 * @param rewardsPoints number of rewards points
	 * @return list of providers 
	 */
	List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints);
	
}
