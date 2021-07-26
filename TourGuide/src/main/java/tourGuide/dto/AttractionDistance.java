package tourGuide.dto;

import gpsUtil.location.Attraction;

/**
 * This class is introduced only to facilitate calculation of distances between a user location and attractions.
 * But it also contains only the required informations for the DTO : ClosestAttractionsDTO.
 * 
 * @author jerome
 *
 */
public class AttractionDistance {
	
	private String attractionName;
	private Double longitude;
	private Double latitude;
	private Double distance;
	private Integer rewardPoints;

	
	public AttractionDistance() {
		
	}
	
	public AttractionDistance(Attraction attraction, Double distance) {
		this.attractionName = attraction.attractionName;
		this.latitude = attraction.latitude;
		this.longitude = attraction.longitude;
		this.distance = distance;
	}

	public String getAttractionName() {
		return attractionName;
	}

	public void setAttractionName(String attractionName) {
		this.attractionName = attractionName;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Integer getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(Integer rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

		
	
}
