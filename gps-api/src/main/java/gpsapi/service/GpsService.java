package gpsapi.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import commons.model.AttractionDistance;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import gpsapi.repository.GpsRepository;
import commons.model.ClosestAttractionsDTO;

@Service
public class GpsService {

	private Logger logger = LoggerFactory.getLogger(GpsService.class);
	
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
	
	@Autowired
	private GpsRepository gpsRepository;

	public VisitedLocation getUserLocation(UUID userId) {
		return gpsRepository.getUserLocation(userId);
	}

	public List<Attraction> getAttractions() {
		return gpsRepository.getAttractions();
	}

	public List<AttractionDistance> getClosestAttractions(Location userLocation, Integer numberOfAttractions) {

		//This DTO will contain the required data:
		ClosestAttractionsDTO closestAttractionsDTO = new ClosestAttractionsDTO();
		closestAttractionsDTO.setUserLocation(userLocation);

		//Get list of all Attractions:
		List<Attraction> listAttraction = gpsRepository.getAttractions();
		//AttractionDistance will contain attraction + distance: it allows storage of distance calculation result.
		List<AttractionDistance> listAttractionDistance = new ArrayList<>();
		//calculate distance and store in a AttractionDistance object:
		for(Attraction attraction : listAttraction) {
			listAttractionDistance.add(
					new AttractionDistance(
							attraction,
							getDistance(
									attraction, userLocation
									)
							)
					);
		}

		//this Stream sorts our list and limit it to 5 lowest distance
		return listAttractionDistance
				.stream()
				.sorted(Comparator.comparingDouble(AttractionDistance::getDistance))
				.limit(numberOfAttractions)
				.collect(Collectors.toList());
	}
	
	/**
	 * Calculates distance between two locations in nautical miles.
	 * @param loc1 the first location
	 * @param loc2 the second location
	 * @return distance in nautical miles
	 */
	private double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
	}
	

}
