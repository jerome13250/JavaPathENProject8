package commons.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tripPricer.Provider;

/**
 * This is a conversion class to ease transfer for the Provider object.
 * Since Provider does not have a default empty constructor it cannot be deserialized by Jackson.
 *
 * @author jerome
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderDTO {
	
	private String name;
	private double price;
	private UUID tripId;
	
	public void copyValuesFrom(Provider provider) {	
		this.setName(provider.name);
		this.setPrice(provider.price);
		this.setTripId(provider.tripId);
	}
	
	public Provider convertToProvider() {
		return new Provider(tripId, name, price);
	}
}
