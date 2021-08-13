package tourGuide.repository;

import java.util.List;
import java.util.UUID;

import commons.model.AttractionDistance;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tourGuide.model.user.User;

public interface GpsProxy {

	/**
	 * Get a VisitedLocation for a specific user id.
	 * @param userid the UUID of the user
	 * @return a VisitedLocation for this user
	 */
	VisitedLocation getVisitedLocation(UUID userid);
	
	
	/**
	 * Get nearby attractions for a specific user
	 *
	 * @param user the user
	 * @param numberOfAttraction the number of nearest attractions to return
	 * @return List of nearby Attractions for a specified user
	 */
	List<AttractionDistance> getNearbyAttractions(User user, Integer numberOfAttraction);

	/**
	 * Get all attractions from gps service.
	 * @return the list of all attractions
	 */
	List<Attraction> getAttractions();

}