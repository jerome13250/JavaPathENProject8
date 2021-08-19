package trippricerapi.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tripPricer.Provider;
import trippricerapi.repository.TripPricerRepository;

@Service
public class TripPricerService {

	@Autowired
	private TripPricerRepository tripPricerRepository;
	
	public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints) {
		return tripPricerRepository.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
	}
	
}
