package tourGuide.repository.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import commons.model.ProviderDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tourGuide.repository.TripPricerProxy;
import tripPricer.Provider;

@Slf4j
@Getter
@Setter
@Component
public class TripPricerProxyImpl implements TripPricerProxy {

	@Value( "${trippricerapi.apiUrl}" )
	private String tripPricerApiUrl;

	RestTemplate restTemplate;
    
    @Autowired
    public TripPricerProxyImpl(RestTemplate restTemplate){
    	this.restTemplate = restTemplate;
    }

	
	@Override
	public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints) {

		String url = tripPricerApiUrl 
				+ "price?"
				+ "apiKey=" + apiKey + "&"
				+ "attractionId=" + attractionId  + "&"
				+ "adults=" + adults + "&"
				+ "children=" + children + "&"
				+ "nightsStay=" + nightsStay + "&"
				+ "rewardsPoints=" + rewardsPoints;

		ResponseEntity<List<ProviderDTO>> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<ProviderDTO>>() {}
				);

		log.debug("Get VisitedLocation call " + response.getStatusCode().toString());

		return response.getBody()!=null?
				response.getBody().stream().map(providerDTO -> providerDTO.convertToProvider()).collect(Collectors.toList())
				: null ;

	}

}
