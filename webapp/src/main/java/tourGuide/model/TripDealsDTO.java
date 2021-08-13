package tourGuide.model;

import java.util.List;

//TODO:remove this class
public class TripDealsDTO {
	
	private int adults;
	private int children;
	private int nightsStay;
	private int rewardsPoints;
	private List<ProviderDTO> tripDeals;
	
	public TripDealsDTO(int adults, int children, int nightsStay, int rewardsPoints, List<ProviderDTO> tripDeals) {
		this.adults = adults;
		this.children = children;
		this.nightsStay = nightsStay;
		this.rewardsPoints = rewardsPoints;
		this.tripDeals = tripDeals;
	}
	
	public int getAdults() {
		return adults;
	}
	public void setAdults(int adults) {
		this.adults = adults;
	}
	public int getChildren() {
		return children;
	}
	public void setChildren(int children) {
		this.children = children;
	}
	public int getNightsStay() {
		return nightsStay;
	}
	public void setNightsStay(int nightsStay) {
		this.nightsStay = nightsStay;
	}
	public int getRewardsPoints() {
		return rewardsPoints;
	}
	public void setRewardsPoints(int rewardsPoints) {
		this.rewardsPoints = rewardsPoints;
	}
	public List<ProviderDTO> getTripDeals() {
		return tripDeals;
	}
	public void setTripDeals(List<ProviderDTO> tripDeals) {
		this.tripDeals = tripDeals;
	}

}
