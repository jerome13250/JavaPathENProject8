package tourGuide.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commons.exception.BusinessResourceException;
import commons.model.ClosestAttractionsDTO;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.user.User;
import tourGuide.model.user.UserPreferencesDTO;
import tourGuide.model.user.UserReward;
import tourGuide.repository.GpsProxy;
import tourGuide.repository.RewardProxy;
import tourGuide.repository.TripPricerProxy;
import tourGuide.repository.impl.GpsProxyDummyImpl;
import tourGuide.repository.impl.RewardProxyDummyImpl;
import tourGuide.repository.impl.TripPricerProxyDummyImpl;
import tourGuide.service.RewardService;
import tourGuide.service.TourGuideService;
import tripPricer.Provider;

class TestTourGuideService {

	TourGuideService tourGuideService;
	RewardService rewardsService;
	
	//reference to objects used in different tests:
	User user1;
	User user2;
	Location userLocation;
	Location userLocation2;
	Location userLocation3;
	Location userLocation4;
	VisitedLocation visitedLocation;
	VisitedLocation visitedLocation2;
	VisitedLocation visitedLocation3;
	VisitedLocation visitedLocation4;
	Attraction attraction1;
	Attraction attraction2;
	UserReward userReward1;
	UserReward userReward2;
	
	
	@BeforeEach
	public void setup() {
		
		//We create dummy implementations for external apis:
		GpsProxy gpsProxy = new GpsProxyDummyImpl();
		RewardProxy rewardProxy = new RewardProxyDummyImpl();
		rewardsService = new RewardService(gpsProxy, rewardProxy);
		TripPricerProxy tripPricerProxy = new TripPricerProxyDummyImpl();
		
		InternalTestHelper.setInternalUserNumber(0);
		//Note that Tracker Thread is directly disabled thanks to stopTrackerAtStartup = true
		tourGuideService = new TourGuideService(gpsProxy, rewardsService, tripPricerProxy, false, true);

		//objects for tests:
		user1 = new User(UUID.randomUUID(), "john", "000", "john@tourGuide.com");
		user2 = new User(UUID.randomUUID(), "john2", "000", "john2@tourGuide.com");
		userLocation = new Location(5.1, 6.2);
		userLocation2 = new Location(20, 20);
		userLocation3 = new Location(30, 30);
		userLocation4 = new Location(40, 40);
		visitedLocation = new VisitedLocation(user1.getUserId(), userLocation, new Date());
		visitedLocation2 = new VisitedLocation(user1.getUserId(), userLocation2, new Date());
		visitedLocation3 = new VisitedLocation(user2.getUserId(), userLocation3, new Date());
		visitedLocation4 = new VisitedLocation(user2.getUserId(), userLocation4, new Date());
		attraction1 = new Attraction("attractionName1", "city1", "state1", 11.1, 22.2);
		attraction2 = new Attraction("attractionName2", "city2", "state2", 33.3, 44.4);
		userReward1 = new UserReward(visitedLocation, attraction1, 111);
		userReward2 = new UserReward(visitedLocation2, attraction2, 222);
		
	}
	
	@Test
	void getUserTest_userUnknown() {
		//ARRANGE:
		//ACT+ASSERT:
		assertThrows(BusinessResourceException.class,()->tourGuideService.getUser("unknown"));
		
	}
	
	@Test
	void getUserLocation() {		
		//ARRANGE:
		//ACT:
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user1);
		//ASSERT:
		assertEquals(visitedLocation.userId,user1.getUserId());
	}
	
	@Test
	void addUser() {
		//ARRANGE:
		//ACT:
		tourGuideService.addUser(user1);
		tourGuideService.addUser(user2);
		//ASSERT:
		User retrivedUser = tourGuideService.getUser(user1.getUserName());
		User retrivedUser2 = tourGuideService.getUser(user2.getUserName());
		assertEquals(user1, retrivedUser);
		assertEquals(user2, retrivedUser2);
	}
	
	@Test
	void getAllUsers() {
		//ARRANGE:
		tourGuideService.addUser(user1);
		tourGuideService.addUser(user2);
		//ACT:
		List<User> allUsers = tourGuideService.getAllUsers();
		//ASSERT:
		assertTrue(allUsers.contains(user1));
		assertTrue(allUsers.contains(user2));
	}
	
	@Test
	void trackUser() {
		//ARRANGE:
		//ACT:
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user1);
		//ASSERT:
		assertEquals(user1.getUserId(), visitedLocation.userId);
	}
	
	@Test
	void getNearbyAttractions() {
		//ARRANGE:
		user1.addToVisitedLocations(visitedLocation);
		tourGuideService.addUser(user1);
		rewardsService.calculateRewardsMultiThread(tourGuideService.getAllUsers());
		
		//ACT:
		ClosestAttractionsDTO result = tourGuideService.getNearbyAttractions("john");
		
		//ASSERT:
		assertEquals(5.1, result.getUserLocation().latitude, 0.000001);
		assertEquals(6.2, result.getUserLocation().longitude, 0.000001);
		assertEquals(5, result.getAttractionList().size());
		assertEquals("attraction1",result.getAttractionList().get(0).getAttractionName());
		assertEquals(100.1d, result.getAttractionList().get(0).getDistance(), 0.001d);
		
	}
	
	@Test
	void getAllCurrentLocations() {
		//ARRANGE:
		user1.addToVisitedLocations(visitedLocation);
		user1.addToVisitedLocations(visitedLocation2);
		
		user2.addToVisitedLocations(visitedLocation3);
		user2.addToVisitedLocations(visitedLocation4);
				
		tourGuideService.addUser(user1);
		tourGuideService.addUser(user2);
		
		//ACT:
		Map<UUID, Location> result = tourGuideService.getAllCurrentLocations();

		//ASSERT:
		assertEquals(2,result.size());
		assertTrue(result.containsKey(user1.getUserId()));
		assertEquals(userLocation2, result.get(user1.getUserId()));
		assertTrue(result.containsKey(user2.getUserId()));
		assertEquals(userLocation4, result.get(user2.getUserId()));
		
	}
	
	@Test
	void getTripDeals() {
		//ARRANGE:
		user1.addUserReward(userReward1);
		user1.addUserReward(userReward2);
		tourGuideService.addUser(user1);

		//ACT:
		List<Provider> listProvider = tourGuideService.getTripDeals(user1);
		
		//ASSERT:
		assertEquals(2, listProvider.size());
		assertEquals("123e4567-e89b-12d3-a456-426614174000", listProvider.get(0).tripId.toString());
		assertEquals("123e4567-e89b-12d3-a456-426614174000", listProvider.get(1).tripId.toString());
	}
	
	@Test 
	void patchUserPreferencies() {
		//ARRANGE:
		tourGuideService.addUser(user1);
		UserPreferencesDTO inputUserPreferencesDTO = new UserPreferencesDTO();
		inputUserPreferencesDTO.setNumberOfAdults(3);
		inputUserPreferencesDTO.setNumberOfChildren(10);
		inputUserPreferencesDTO.setTripDuration(258);
		inputUserPreferencesDTO.setUserName("john");
		
		//ACT:
		UserPreferencesDTO returnedUserPreferencesDTO = tourGuideService.patchUserPreferences(inputUserPreferencesDTO);
		
		//ASSERT:
		assertEquals(inputUserPreferencesDTO, returnedUserPreferencesDTO);

	}
	
	@Test 
	void patchUserPreferencies_nullInfos() {
		//ARRANGE:
		tourGuideService.addUser(user1);
		UserPreferencesDTO inputUserPreferencesDTO = new UserPreferencesDTO();
		inputUserPreferencesDTO.setNumberOfAdults(null);
		inputUserPreferencesDTO.setNumberOfChildren(null);
		inputUserPreferencesDTO.setTripDuration(null);
		inputUserPreferencesDTO.setUserName("john");
		
		//ACT:
		UserPreferencesDTO returnedUserPreferencesDTO = tourGuideService.patchUserPreferences(inputUserPreferencesDTO);
		
		//ASSERT:
		assertEquals(inputUserPreferencesDTO.getUserName(), returnedUserPreferencesDTO.getUserName());
		assertEquals(1, returnedUserPreferencesDTO.getNumberOfAdults(), "unchanged => equals default 1");
		assertEquals(0, returnedUserPreferencesDTO.getNumberOfChildren(), "unchanged => equals default 0");
		assertEquals(1, returnedUserPreferencesDTO.getTripDuration(), "unchanged => equals default 1");

	}
	
}
