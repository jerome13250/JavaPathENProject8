package tourGuide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.ClosestAttractionsDTO;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tripPricer.Provider;

public class TestTourGuideService {

	TourGuideService tourGuideService;
	RewardsService rewardsService;
	
	@Before
	public void setup() {
		//we have a bug in external jar GpsUtils due to String.format("%.6f", new Object[] { Double.valueOf(longitude) })),
		//format uses Locale.getDefault() that create string Double with "," (when Locale=FR) instead of "."
		//For this reason i need to change the default Locale:
		Locale.setDefault(Locale.US);
		
		GpsUtil gpsUtil = new GpsUtil();
		rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		InternalTestHelper.setInternalUserNumber(0);
		//Note that Tracker Thread is directly disabled thanks to stopTrackerAtStartup = true
		tourGuideService = new TourGuideService(gpsUtil, rewardsService, true);

	}
	
	@Test
	public void getUserLocation() {		
		//ARRANGE:
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		//ACT:
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		//ASSERT:
		assertTrue(visitedLocation.userId.equals(user.getUserId()));
	}
	
	@Test
	public void addUser() {
		//ARRANGE:
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");
		//ACT:
		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		//ASSERT:
		User retrivedUser = tourGuideService.getUser(user.getUserName());
		User retrivedUser2 = tourGuideService.getUser(user2.getUserName());
		assertEquals(user, retrivedUser);
		assertEquals(user2, retrivedUser2);
	}
	
	@Test
	public void getAllUsers() {
		//ARRANGE:
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");
		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		//ACT:
		List<User> allUsers = tourGuideService.getAllUsers();
		//ASSERT:
		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}
	
	@Test
	public void trackUser() {
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		
		assertEquals(user.getUserId(), visitedLocation.userId);
	}
	
	@Test
	public void getNearbyAttractions() {
		//ARRANGE:
		String userName = "jon";
		User user = new User(UUID.randomUUID(), userName, "000", "jon@tourGuide.com");
		
		Location centralParkNY = new Location(40.782223, -73.965279); //central park New York
		VisitedLocation fakeVisitedLocation = new VisitedLocation(user.getUserId(), centralParkNY, new Date());
		user.addToVisitedLocations(fakeVisitedLocation);
		rewardsService.calculateRewards(user);
		tourGuideService.addUser(user);
		
		//ACT:
		ClosestAttractionsDTO result = tourGuideService.getNearbyAttractions(userName);
		
		//ASSERT:
		assertEquals(40.782223d, result.getUserLocation().latitude, 0.000001);
		assertEquals(-73.965279d, result.getUserLocation().longitude, 0.000001);
		assertEquals(5, result.getAttractionList().size());
		assertEquals("Flatiron Building",result.getAttractionList().get(0).getAttractionName());
		assertEquals(3.113d, result.getAttractionList().get(0).getDistance(), 0.001d);
		
	}
	
	@Test
	public void getAllCurrentLocations() {
		//ARRANGE:
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Location location1 = new Location(10, 10);
		VisitedLocation visitedLocation1 = new VisitedLocation(user.getUserId(), location1, new Date());
		Location location2 = new Location(20, 20);
		VisitedLocation visitedLocation2 = new VisitedLocation(user.getUserId(), location2, new Date());
		user.addToVisitedLocations(visitedLocation1);
		user.addToVisitedLocations(visitedLocation2);
		
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");
		Location location3 = new Location(30, 30);
		VisitedLocation visitedLocation3 = new VisitedLocation(user2.getUserId(), location3, new Date());
		Location location4 = new Location(40, 40);
		VisitedLocation visitedLocation4 = new VisitedLocation(user2.getUserId(), location4, new Date());
		user2.addToVisitedLocations(visitedLocation3);
		user2.addToVisitedLocations(visitedLocation4);
				
		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		//ACT:
		Map<UUID, Location> result = tourGuideService.getAllCurrentLocations();

		//ASSERT:
		assertEquals(2,result.size());
		assertTrue(result.containsKey(user.getUserId()));
		assertEquals(location2, result.get(user.getUserId()));
		assertTrue(result.containsKey(user2.getUserId()));
		assertEquals(location4, result.get(user2.getUserId()));
		
	}
	
	
	public void getTripDeals() {
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		List<Provider> providers = tourGuideService.getTripDeals(user);
		
		
		assertEquals(10, providers.size());
	}
	
	
}
