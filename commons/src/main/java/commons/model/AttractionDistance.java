package commons.model;

import gpsUtil.location.Attraction;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor

public class AttractionDistance {
	
	private String attractionName;
	private Double longitude;
	private Double latitude;
	private Double distance;
	private Integer rewardPoints;

	/**
	 * constructor for AttractionDistance
	 * @param attraction the attraction on which to calculate distance
	 * @param distance the distance to the attraction
	 */
	public AttractionDistance(Attraction attraction, Double distance) {
		this.attractionName = attraction.attractionName;
		this.latitude = attraction.latitude;
		this.longitude = attraction.longitude;
		this.distance = distance;
	}

}
