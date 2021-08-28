package tourGuide.repository.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tourGuide.repository.RewardProxy;

@Slf4j
@Getter
@Setter
@Component
public class RewardProxyImpl implements RewardProxy {

	
	@Value( "${rewardapi.apiUrl}" )
    private String rewardApiUrl;
	
	RestTemplate restTemplate;
    
    @Autowired
    public RewardProxyImpl(RestTemplate restTemplate){
    	this.restTemplate = restTemplate;
    }
	
	@Override
	public Integer getAttractionRewardPoints(UUID attractionid, UUID userid) {
	
    	String url = rewardApiUrl 
    			+ "attractionRewardPoints?"
    			+ "attractionid=" + attractionid + "&"
    			+ "userid=" + userid;
    	
    	ResponseEntity<Integer> response = restTemplate.exchange(
    			url,
    			HttpMethod.GET,
    			null,
    			new ParameterizedTypeReference<Integer>() {}
    			);

    	log.debug("Get VisitedLocation call " + response.getStatusCode().toString());

    	return response.getBody();

	}

}
