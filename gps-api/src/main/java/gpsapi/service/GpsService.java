package gpsapi.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import commons.model.AttractionDistance;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import gpsapi.repository.GpsRepository;

@Service
public class GpsService {
	
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
	
	@Autowired
	private GpsRepository gpsRepository;

	public VisitedLocation getUserLocation(UUID userId) {
		return gpsRepository.getUserLocation(userId);
	}

	public List<Attraction> getAttractions() {
		return gpsRepository.getAttractions();
	}
	
	/**
	 * Calculates distances from a user location to all Attractions. Returns only the "numberOfAttractions" closest.
	 * @param userLocation the location of the user
	 * @param numberOfAttractions the number of attractions to return
	 * @return a list of AttractionDistance that contains some attraction infos + the distance with user location.
	 */
	public List<AttractionDistance> getClosestAttractions(Location userLocation, Integer numberOfAttractions) {

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
	
	
	//TODO: place this in a common calculationService
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
