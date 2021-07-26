package tourGuide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.dto.ClosestAttractionsDTO;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tripPricer.Provider;

public class TestTourGuideService {

	TourGuideService tourGuideService;
	RewardsService rewardsService;
	
	@Before
	public void setup() {
		//we have a bug in external jar GpsUtils due to String.format("%.6f", new Object[] { Double.valueOf(longitude) }))
		//format uses Locale.getDefault() that create string Double with "," (Locale=FR) instead of "."
		//For this reason i need to change the default Locale:
		Locale.setDefault(Locale.US);
		GpsUtil gpsUtil = new GpsUtil();
		rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		ObjectMapper mapper = new ObjectMapper();
		InternalTestHelper.setInternalUserNumber(0);
		tourGuideService = new TourGuideService(gpsUtil, rewardsService, mapper);
	}
	
	@Test
	public void getUserLocation() {		
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		tourGuideService.tracker.stopTracking();
		assertTrue(visitedLocation.userId.equals(user.getUserId()));
	}
	
	@Test
	public void addUser() {
		
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		User retrivedUser = tourGuideService.getUser(user.getUserName());
		User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

		tourGuideService.tracker.stopTracking();
		
		assertEquals(user, retrivedUser);
		assertEquals(user2, retrivedUser2);
	}
	
	@Test
	public void getAllUsers() {
		
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		List<User> allUsers = tourGuideService.getAllUsers();

		tourGuideService.tracker.stopTracking();
		
		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}
	
	@Test
	public void trackUser() {
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		
		tourGuideService.tracker.stopTracking();
		
		assertEquals(user.getUserId(), visitedLocation.userId);
	}
	
	@Ignore // Not yet implemented
	@Test
	public void getNearbyAttractions() {
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		
		List<Attraction> attractions = tourGuideService.getNearByAttractions(visitedLocation);
		
		tourGuideService.tracker.stopTracking();
		
		assertEquals(5, attractions.size());
	}
	
	
	@Test
	public void getClosest5Attractions() {
		//ARRANGE:
		tourGuideService.tracker.stopTracking();
		
		String userName = "jon";
		User user = new User(UUID.randomUUID(), userName, "000", "jon@tourGuide.com");
		
		Location centralParkNY = new Location(40.782223, -73.965279); //central park New York
		VisitedLocation fakeVisitedLocation = new VisitedLocation(user.getUserId(), centralParkNY, new Date());
		user.addToVisitedLocations(fakeVisitedLocation);
		rewardsService.calculateRewards(user);
		tourGuideService.addUser(user);
		
		//ACT:
		ClosestAttractionsDTO result = tourGuideService.getClosest5Attractions(userName);
		
		//ASSERT:
		assertEquals(40.782223d, result.getUserLocation().latitude, 0.000001);
		assertEquals(-73.965279d, result.getUserLocation().longitude, 0.000001);
		assertEquals(5, result.getAttractionList().size());
		assertEquals("Flatiron Building",result.getAttractionList().get(0).getAttractionName());
		assertEquals(3.113d, result.getAttractionList().get(0).getDistance(), 0.001d);
		
	}
	
	public void getTripDeals() {
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		List<Provider> providers = tourGuideService.getTripDeals(user);
		
		tourGuideService.tracker.stopTracking();
		
		assertEquals(10, providers.size());
	}
	
	
}
