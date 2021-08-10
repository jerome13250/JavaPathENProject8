package gpsapi.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

@Repository
public class GpsRepository {

	@Autowired
	private GpsUtil gpsUtil;
	
	public VisitedLocation getUserLocation(UUID userId) {
		return gpsUtil.getUserLocation(userId);
	}
	
	public List<Attraction> getAttractions() {
		return gpsUtil.getAttractions();
	}
	
}
