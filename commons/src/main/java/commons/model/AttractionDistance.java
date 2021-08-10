package commons.model;

import gpsUtil.location.Attraction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class is introduced to keep calculation of distances between a user location and an attraction.
 * It is part of the DTO : ClosestAttractionsDTO.
 * 
 * @author jerome
 *
 */

//Lombok:
@Getter
@Setter
@NoArgsConstructor

public class AttractionDistance {
	
	private String attractionName;
	private Double longitude;
	private Double latitude;
	private Double distance;
	private Integer rewardPoints;

	
	public AttractionDistance(Attraction attraction, Double distance) {
		this.attractionName = attraction.attractionName;
		this.latitude = attraction.latitude;
		this.longitude = attraction.longitude;
		this.distance = distance;
	}

}
