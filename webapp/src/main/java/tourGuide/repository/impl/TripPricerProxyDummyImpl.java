package tourGuide.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import tourGuide.repository.TripPricerProxy;
import tripPricer.Provider;

/**
 * For integration test : dummy Implementation of communication between webapp and the REST trippricer-api.
 * <p>
 * trippricer-api is an external service, so for integration tests we create this dummy implementation that will
 * always return the same results whatever the parameters...
 *</p> 
 * 
 * @author jerome
 *
 */
public class TripPricerProxyDummyImpl implements TripPricerProxy  {

	@Override
	public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay,
			int rewardsPoints) {

		UUID attractionid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
		Provider provider1 = new Provider(attractionid, "provider1", 100);
		Provider provider2 = new Provider(attractionid, "provider2", 200);
		List<Provider> listProvider = new ArrayList<>();
		listProvider.add(provider1);
		listProvider.add(provider2);
		
		return listProvider;
	}

	
	
}
