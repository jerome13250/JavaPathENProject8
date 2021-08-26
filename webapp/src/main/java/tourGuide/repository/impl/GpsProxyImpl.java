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

import commons.model.AttractionDTO;
import commons.model.AttractionDistance;
import commons.model.VisitedLocationDTO;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tourGuide.model.user.User;
import tourGuide.repository.GpsProxy;

/**
 * Implement communication between webapp and the REST gps-api
 * <p>
 * Original code from : <a href="https://openclassrooms.com/fr/courses/6900101-creez-une-application-java-avec-spring-boot/7078034-ecrivez-votre-code#r-7079491">
 *  Openclassrooms.com </a>
 *</p> 
 * 
 * @author jerome
 *
 */
@Slf4j
@Getter
@Setter
@Component
public class GpsProxyImpl implements GpsProxy {
    
    @Value( "${gpsapi.apiUrl}" )
    private String gpsApiUrl;
    
    RestTemplate restTemplate;
    
    @Autowired
    public GpsProxyImpl(RestTemplate restTemplate){
    	this.restTemplate = restTemplate;
    }
    
    @Override
	public VisitedLocation getVisitedLocation(UUID userid) {

    	String getVisitedLocationUrl = gpsApiUrl 
    			+ "visitedLocation?userid="
    			+ userid;
    	
    	
    	ResponseEntity<VisitedLocationDTO> response = restTemplate.exchange(
    			getVisitedLocationUrl,
    			HttpMethod.GET,
    			null,
    			new ParameterizedTypeReference<VisitedLocationDTO>() {}
    			);

    	log.debug("Get VisitedLocation call " + response.getStatusCode().toString());

    	return response.getBody()!=null ? response.getBody().convertToVisitedLocation() : null;

	}
    
    
	@Override
	public List<AttractionDistance> getNearbyAttractions(User user, Integer numberOfAttraction) {
		
    	String getNearbyAttractionsUrl = gpsApiUrl 
    			+ "closestAttractions?long="
    			+ user.getLastVisitedLocation().location.longitude
    			+ "&lat="
    			+ user.getLastVisitedLocation().location.latitude
    			+ "&number="
    			+ numberOfAttraction;

    	log.info(getNearbyAttractionsUrl);
    	
    	ResponseEntity<List<AttractionDistance>> response = restTemplate.exchange(
    			getNearbyAttractionsUrl,
    			HttpMethod.GET,
    			null,
    			new ParameterizedTypeReference<List<AttractionDistance>>() {}
    			);

    	log.debug("Get NearbyAttractions call " + response.getStatusCode().toString());

    	return response.getBody();
    }


	@Override
	public List<Attraction> getAttractions() {

		String getNearbyAttractionsUrl = gpsApiUrl + "attractions";

    	ResponseEntity<List<AttractionDTO>> response = restTemplate.exchange(
    			getNearbyAttractionsUrl,
    			HttpMethod.GET,
    			null,
    			new ParameterizedTypeReference<List<AttractionDTO>>() {}
    			);

    	log.debug("Get Attractions call " + response.getStatusCode().toString());

    	//convert List<AttractionDTO> to List<Attraction>
    	return response.getBody()!=null ?
    			response.getBody().stream().map(atdto -> atdto.convertToAttraction()).collect(Collectors.toList())
    			: null;
		
	}

	

}