package tourGuide.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.AttractionDistance;
import tourGuide.model.ClosestAttractionsDTO;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserReward;
import tripPricer.Provider;
import tripPricer.TripPricer;

@Service
public class TourGuideService {

	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);

	private final GpsUtil gpsUtil;
	private final RewardsService rewardsService; //protected since need to access in test 
	private final TripPricer tripPricer = new TripPricer();
	public final Tracker tracker;
	boolean testMode = true;

	/**
	 * Constructor for TourGuideService, this is the default constructor for Spring thanks to @Autowired. In this constructor 
	 * the Tracker Thread is active by default.
	 * 
	 * @param gpsUtil the reference to bean GpsUtil.jar
	 * @param rewardsService the reference to bean RewardsService.jar
	 */
	@Autowired //this defines the default constructor for Spring
	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService) {
		this.gpsUtil = gpsUtil;
		this.rewardsService = rewardsService;

		if(testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		
		tracker = new Tracker(this, false); //by default Tracker is not stopped
		addShutDownHook();
	}
	
	/**
	 * Constructor for TourGuideService, this version has a boolean stopTrackerAtStartup that allows Tracker to be directly stopped,
	 * this is for test purpose as Tracker conflicts with tests by running permanent updates on users.
	 * 
	 * @param gpsUtil the reference to bean GpsUtil.jar
	 * @param rewardsService the reference to bean RewardsService.jar
	 * @param stopTrackerAtStartup  boolean that allows Tracker to be directly stopped when true, this is for test only.
	 */
	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService, boolean stopTrackerAtStartup) {
		this.gpsUtil = gpsUtil;
		this.rewardsService = rewardsService;

		if(testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		
		tracker = new Tracker(this, stopTrackerAtStartup); //Tracker can be stopped at startup
		addShutDownHook();
	}

	/**
	 * Simply returns a reference to List User.userRewards 
	 * @param user the current user
	 * @return List User.userRewards
	 */
	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	/**
	 * Return the last visited VisitedLocation if User.visitedLocations is not empty, otherwise
	 * ask an update to GpsUtil of the User.
	 * 
	 * @param user the current user
	 * @return last VisitedLocation
	 */	
	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
				user.getLastVisitedLocation() :
					trackUserLocation(user);
		return visitedLocation;
	}

	/**
	 * return a User from internalUserMap by userName
	 * @param userName, name of the user required
	 * @return User object
	 */
	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}

	/**
	 * return all users from internalUserMap converted from HashMap to List
	 * @return List of all users
	 */
	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	/**
	 * Insert a new User in internalUserMap if username not already exists
	 * @param user to add
	 */
	public void addUser(User user) {
		if(!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	/**
	 * Calculate the total sum of Rewards points for a specified User.
	 * Then get the List of providers (always 5) for a specific attraction and set it to user.tripDeals ArrayList.
	 * Return the List of providers.
	 * 
	 * @param user from which we need to fill list of providers thanks to his rewards points
	 * @return the list of providers
	 */
	//TODO: inconsistency in original project, tripPricer.getPrice uses user.getUserId() instead of UUID attractionId 
	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(), 
				user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	/**
	 * Call to the GpsUtil to provide a VisitedLocation with random coordinates in WGS84 decimal format.
	 * Then saves the VisitedLocation in User.visitedLocations ArrayList.
	 * Then uses rewardsService to add new Rewards to the User.
	 * At the end return the VisitedLocation object
	 * 
	 * @param user from which we need to fill data thanks to his coordinates
	 * @return the visitedLocation generated by GpsUtil
	 */
	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		rewardsService.calculateRewards(user);
		return visitedLocation;
	}

	/**
	 * Get the closest five tourist attractions to the user - no matter how far away they are.
	 * <p>
	 * Return a DTO object that contains following informations:
	 * <ul>
	 * <li>The user's location lat/long</li>
	 * <li>Name of Tourist attraction</li>
	 * <li>Tourist attractions lat/long</li> 
	 * <li>The distance in miles between the user's location and each of the attractions</li>
	 * <li>The reward points for visiting each Attraction</li>
	 * Note: Attraction reward points are gathered from RewardsCentral
	 * </ul>
	 * </p>
	 * 
	 * @param userName the user name
	 * @return ClosestAttractionsDTO with all required infos
	 */
	public ClosestAttractionsDTO getNearbyAttractions (String userName) {

		//This DTO will contain the required data:
		ClosestAttractionsDTO closestAttractionsDTO = new ClosestAttractionsDTO();

		//get user:
		User user = getUser(userName);
		VisitedLocation visitedLocation = user.getLastVisitedLocation();
		closestAttractionsDTO.setUserLocation(visitedLocation.location);

		//Get list of Attractions from GpsUtils:
		List<Attraction> listAttraction = gpsUtil.getAttractions();
		//AttractionDistance will contain attraction + distance: it allows storage of distance calculation result.
		List<AttractionDistance> listAttractionDistance = new ArrayList<>();
		//calculate distance and store in a AttractionDistance object:
		for(Attraction attraction : listAttraction) {
			listAttractionDistance.add(
					new AttractionDistance(
							attraction,
							rewardsService.getDistance(
									attraction, visitedLocation.location
									)
							)
					);
		}

		//this Stream sorts our list and limit it to 5 lowest distance
		List<AttractionDistance> listClosest5AttractionsDistances = listAttractionDistance
				.stream()
				.sorted(Comparator.comparingDouble(AttractionDistance::getDistance))
				.limit(5)
				.collect(Collectors.toList());

		//set rewardspoints on each attraction:
		List<UserReward> userRewards = user.getUserRewards();
		listClosest5AttractionsDistances.stream().forEach(a -> {
			for (UserReward u: userRewards ) {
				if (u.attraction.attractionName.equals(a.getAttractionName())) 
					a.setRewardPoints(u.getRewardPoints());
			}
		});

		//add attractions to dto:
		closestAttractionsDTO.setAttractionList(listClosest5AttractionsDistances);

		return closestAttractionsDTO;

	}


	/**
	 * Return the list of every user's most recent location stored in internalUserMap.
	 * 
	 * <p>
	 * Note: if a user's current location history is not empty then it returns the latest position, otherwise gpsUtil  
	 * is used to get a valid position. This behavior is due to the provided function tourGuideService.getUserLocation().
	 * </p>
	 * 
	 * @return
	 */
	public Map<UUID, Location> getAllCurrentLocations() {

		Map<UUID, Location> mapUserUuidLocation = new HashMap<UUID, Location>();
		internalUserMap.forEach((id, user) -> {
			mapUserUuidLocation.put(user.getUserId(), getUserLocation(user).location);
		});

		return mapUserUuidLocation;

	}

	//TODO: A INVESTIGUER
	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() { 
			public void run() {
				logger.debug("TourGuideService ShutDownHook invoked");
				tracker.stopTracking();
			} 
		}); 
	}

	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();
	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);

			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i-> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}

	private double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
		double rightLimit = 85.05112878;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}


}







