package tourGuide.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.user.User;
import tourGuide.user.UserReward;

@Service
public class RewardsService {
	
	private Logger logger = LoggerFactory.getLogger(RewardsService.class);
		
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	// proximity in miles
    private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	private final GpsUtil gpsUtil;
	private final RewardCentral rewardsCentral;
	
	//After tests, CachedThreadPool is the fastest.
	private final ExecutorService executorService = Executors.newCachedThreadPool();
	
	public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.rewardsCentral = rewardCentral;
	}
	
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}
	
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}
	
	/**
	 * This function gets all VisitedLocation for a User, then gets all Attractions provided by GpsUtils.
	 * Everytime we call this function, it checks on user location history (why all history ?), if it has no previous UserReward on specific attraction
	 * then if the attraction is close enough ( function nearAttraction ) we add a Reward to the user.
	 * 
	 * @param user
	 */
	public void calculateRewards(User user) {
		 
		List<VisitedLocation> userLocations = user.getVisitedLocations(); 
		List<Attraction> attractions = gpsUtil.getAttractions();
		
		//TODO: this is stupid, rewards should be calculated on the last Location only. 
		//Otherwise the more we have history the more we'll have to calculate distances.
		for(VisitedLocation visitedLocation : userLocations) {
			for(Attraction attraction : attractions) {
				if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					if(nearAttraction(visitedLocation, attraction)) {
						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
						logger.debug("user has rewards : {}", user.getUserName());
					}
				}
			}
		}
	}
	
	
	public void calculateRewardsMultiThread(List<User> userList) {

		List<Attraction> attractions = gpsUtil.getAttractions();
		List<Future> listFuture = new ArrayList<>();

		for(User user : userList) {
			Future future = executorService.submit( () -> {

				for(Attraction attraction : attractions) {
					if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
						//rewards are calculated on the last Location only:
						VisitedLocation lastVisitedLocation = user.getVisitedLocations().get(user.getVisitedLocations().size()-1);
						if(nearAttraction(lastVisitedLocation, attraction)) {
							user.addUserReward(new UserReward(lastVisitedLocation, attraction, getRewardPoints(attraction, user)));
							//logger.debug("user has rewards : {}", user.getUserName());
						}
					}
				}
			});
			listFuture.add(future);
		}
		
		listFuture.stream().forEach(f->{
			try {
				f.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
	}
		
	/**
	 * Test if distance between attraction and location is inferior to attractionProximityRange (200 miles).
	 * @param attraction required
	 * @param location required
	 * @return true if distance is inferior to attractionProximityRange, false otherwise.
	 */
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}
	
	/**
	 * Test if distance between attraction and visitedlocation is inferior to proximityBuffer.
	 * @param visitedLocation required
	 * @param attraction required
	 * @return true if distance is inferior to proximityBuffer, false otherwise.
	 */
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}
	
	private int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}
	
	public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
	}

}
