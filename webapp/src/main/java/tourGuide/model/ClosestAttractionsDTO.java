package tourGuide.model;

import gpsUtil.location.Location;
import java.util.List;

public class ClosestAttractionsDTO {

	private Location userLocation;
	private List<AttractionDistance> attractionList;
	
	public ClosestAttractionsDTO() {
		
	}
	
	public ClosestAttractionsDTO(Location userLocation, List<AttractionDistance> attractionList) {
		this.userLocation = userLocation;
		this.attractionList = attractionList;
	}
	
	public Location getUserLocation() {
		return userLocation;
	}
	public void setUserLocation(Location userLocation) {
		this.userLocation = userLocation;
	}
	public List<AttractionDistance> getAttractionList() {
		return attractionList;
	}
	public void setAttractionList(List<AttractionDistance> attractionList) {
		this.attractionList = attractionList;
	}
	
	
	
	
}
