package commons.model;

import java.util.Date;
import java.util.UUID;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import lombok.Getter;
import lombok.Setter;


/**
 * This is a conversion class to ease transfert for the VisitedLocation object.
 * Since VisitedLocation does not have a default empty constructor it cannot be deserialized by Jackson.
 *
 * @author jerome
 *
 */
@Getter
@Setter
public class VisitedLocationDTO {

	private UUID userId;
	private double location_latitude;
	private double location_longitude;
	private Date timeVisited;
	
	public void copyValuesFrom(VisitedLocation visitedLocation) {	
		this.setUserId(visitedLocation.userId);
		this.setLocation_latitude(visitedLocation.location.latitude);
		this.setLocation_longitude(visitedLocation.location.longitude);
		this.setTimeVisited(visitedLocation.timeVisited);		
	}
	
	public VisitedLocation convertToVisitedLocation() {
		Location location = new Location(this.getLocation_latitude(),this.getLocation_longitude());
		VisitedLocation visitedLocation = new VisitedLocation(this.getUserId(),location,this.getTimeVisited());
		return visitedLocation;
	}
	
}
