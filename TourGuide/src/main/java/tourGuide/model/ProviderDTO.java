package tourGuide.model;

import tripPricer.Provider;

/**
 * This DTO is introduced to return same informations as a Provider object but without UUID tripId. 
 * @author jerome
 *
 */


public class ProviderDTO {
	
	private String name;
	private double price;
		
	public ProviderDTO(Provider provider) {
	this.name = provider.name;
	this.price = provider.price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
