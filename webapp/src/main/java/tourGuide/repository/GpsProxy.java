package tourGuide.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import commons.model.AttractionDistance;
import lombok.extern.slf4j.Slf4j;
import tourGuide.model.user.User;

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
@Component
public class GpsProxy {
    
    @Value( "${gpsapi.apiUrl}" )
    private String gpsApiUrl;
    
    /**
     * Get nearby attractions for a specific user
     * @return An List of nearby Attractions
     */
	public List<AttractionDistance> getNearbyAttractions(User user, Integer numberOfAttraction) {
		
    	String getNearbyAttractionsUrl = gpsApiUrl 
    			+ "/closestAttractions?long="
    			+ user.getLastVisitedLocation().location.longitude
    			+ "&lat="
    			+ user.getLastVisitedLocation().location.latitude
    			+ "&number="
    			+ numberOfAttraction;


    	RestTemplate restTemplate = new RestTemplate();
    	ResponseEntity<List<AttractionDistance>> response = restTemplate.exchange(
    			getNearbyAttractionsUrl,
    			HttpMethod.GET,
    			null,
    			new ParameterizedTypeReference<List<AttractionDistance>>() {}
    			);

    	log.debug("Get NearbyAttractions call " + response.getStatusCode().toString());

    	return response.getBody();
    }

}