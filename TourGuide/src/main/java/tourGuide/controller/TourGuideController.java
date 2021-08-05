package tourGuide.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import tourGuide.model.TripDealsDTO;
import tourGuide.model.user.User;
import tourGuide.service.TourGuideService;
import tripPricer.Provider;

@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;
	
    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }
    
    @RequestMapping("/getLocation") 
    public String getLocation(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
		return JsonStream.serialize(visitedLocation.location);
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
    @RequestMapping("/getNearbyAttractions") 
    public String getNearbyAttractions(@RequestParam String userName) {
    	
    	return JsonStream.serialize(tourGuideService.getNearbyAttractions(userName));
    }
    
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
    	return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
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
     * @return
     */
    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
    	    	
    	return JsonStream.serialize(tourGuideService.getAllCurrentLocations());
    }
    
    @RequestMapping("/getTripDeals")
    public TripDealsDTO getTripDeals(@RequestParam String userName) {
    	TripDealsDTO tripDealsDTO = tourGuideService.getTripDeals(getUser(userName));
    	return tripDealsDTO;
    }
    
    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }
   

}