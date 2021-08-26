package commons.model;

import java.util.UUID;

import gpsUtil.location.Attraction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is a conversion class to ease transfer for the Attraction object.
 * Since Attraction does not have a default empty constructor it cannot be deserialized by Jackson.
 *
 * @author jerome
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttractionDTO {

	private Double latitude;
	private Double longitude;
	private String attractionName;
	private String city;
	private String state;
	private UUID attractionId;
	
	public AttractionDTO (Attraction attraction) {
		this.setLatitude(attraction.latitude);
		this.setLongitude(attraction.longitude);
		this.setAttractionId(attraction.attractionId);
		this.setAttractionName(attraction.attractionName);
		this.setCity(attraction.city);
		this.setState(attraction.state);
	}
	
	public Attraction convertToAttraction() {
		return new Attraction(attractionName, city, state, latitude, longitude);
		
	}
	
	
	
}
