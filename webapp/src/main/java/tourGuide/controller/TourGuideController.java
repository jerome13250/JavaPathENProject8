package tourGuide.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import commons.model.ClosestAttractionsDTO;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import tourGuide.model.user.User;
import tourGuide.model.user.UserPreferencesDTO;
import tourGuide.model.user.UserReward;
import tourGuide.service.TourGuideService;
import tripPricer.Provider;
@ApiModel
@Slf4j
@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;
	
	@ApiOperation(value = "This url returns welcome message from TourGuide.")
    @GetMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }
    
    /**
     * Return the last visited VisitedLocation for a specified user name. 
     * If User.visitedLocations is not empty it is the last one, otherwise
	 * an update to GpsUtil for this User is required.
     * @param userName the name of the user
     * @return VisitedLocation json
     */
	@ApiOperation(value = "Return the last visited VisitedLocation for a specified user name.")
    @GetMapping("/getLocation") 
    public Location getLocation(
    		@ApiParam(
					value = "User name.",
					example = "internalUser1")
    		@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
		return visitedLocation.location;
    }
    
    /**
     * Return the closest five tourist attractions to the user - no matter how far away they are, with following informations:
	 * <ul>
	 * <li>The user's location lat/long</li>
	 * <li>Name of Tourist attraction</li>
	 * <li>Tourist attractions lat/long</li> 
	 * <li>The distance in miles between the user's location and each of the attractions</li>
	 * <li>The reward points for visiting each Attraction</li>
	 * Note: Attraction reward points are gathered from RewardsCentral
	 * </ul>
     * 
     * @param userName the required user name
     * @return JSON object containing all required infos
     */
	@ApiOperation(value = "Return the closest five tourist attractions to the user - no matter how far away they are -"
			+ " for a specified user name.")
    @GetMapping("/getNearbyAttractions") 
    public ClosestAttractionsDTO getNearbyAttractions(
    		@ApiParam(
					value = "User name.",
					example = "internalUser1")
    		@RequestParam String userName) {
    	
    	return tourGuideService.getNearbyAttractions(userName);
    }
    
    /**
     * Return all the Rewards for a specific user
     * @param userName the user name
     * @return List of rewards for that user
     */
	@ApiOperation(value = "Return all the Rewards for a specified user name.")
    @GetMapping("/getRewards") 
    public List<UserReward> getRewards(
    		@ApiParam(
					value = "User name.",
					example = "internalUser1")
    		@RequestParam String userName) {
    	return tourGuideService.getUserRewards(getUser(userName));
    }
    
    /**
     * Get the list of every user's most recent location as JSON.
     * <p>
     * Note: if a user's current location history is not empty then it returns the latest position, otherwise gpsUtil  
     * is used to get a valid position. This behavior is due to the provided function tourGuideService.getUserLocation().
     * </p>
     * <p>
     * Return object should be the just a JSON mapping of userId to Locations similar to:<br>
     * {
     *     "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371}
     *     ...
     * }
     * </p>
     * 
     * @return Map object with key=UUID of user and value=Location
     */
	@ApiOperation(value = "Get the list of every user's most recent location as JSON.")
    @GetMapping("/getAllCurrentLocations")
    public Map<UUID,Location> getAllCurrentLocations() {
    	    	
    	return tourGuideService.getAllCurrentLocations();
    }
    
    
    /**
     * Calculate and return a list of providers for attractions depending on user total reward points and preferences.
     * Note that in fact provider should be named trip deal...
     * 
     * @param userName
     * @return List of providers 
     */
	@ApiOperation(value = "Return all the Rewards for a specified user name.")
    @GetMapping("/getTripDeals")
    public List<Provider> getTripDeals(
    		@ApiParam(
					value = "User name.",
					example = "internalUser1")
    		@RequestParam String userName) {
    	List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
    	return providers;
    }
    
    /**
     * Patch command that allows to update user preferencies
     * @param userPreferencesDTO with username and values to modify
     * @return the userPreferencesDTO result
     */
	@ApiOperation(value = "PATCH command that allows to update user preferencies.")
	@PatchMapping(value = "/userPreferences")
	public ResponseEntity<UserPreferencesDTO> patchUserPreferences(@RequestBody UserPreferencesDTO userPreferencesDTO) {
		log.debug("PATCH /userPreferences called");
		UserPreferencesDTO userPreferencesDtoUpdated = tourGuideService.patchUserPreferences(userPreferencesDTO);
		log.debug("PATCH /userPreferences response : OK");
		return new ResponseEntity<>(userPreferencesDtoUpdated, HttpStatus.OK);
	}
    

    /**
     * Return user object from user name
     * @param userName the user name
     * @return the User object with the required user name
     */
    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }
   


}