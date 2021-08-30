package tourGuide.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.user.User;
import tourGuide.model.user.UserReward;
import tourGuide.repository.GpsProxy;
import tourGuide.repository.RewardProxy;
import tourGuide.repository.TripPricerProxy;
import tourGuide.repository.impl.GpsProxyDummyImpl;
import tourGuide.repository.impl.RewardProxyDummyImpl;
import tourGuide.repository.impl.TripPricerProxyDummyImpl;
import tourGuide.service.RewardService;
import tourGuide.service.TourGuideService;

class TestRewardService {

	TourGuideService tourGuideService;
	RewardProxy rewardProxy;
	RewardService rewardsService;
	GpsProxy gpsProxy;
	TripPricerProxy tripPricerProxy;
	
	@BeforeEach
	public void setup() {
		
		//We create dummy implementations for external apis:
		gpsProxy = new GpsProxyDummyImpl();
		rewardProxy = new RewardProxyDummyImpl();
		rewardsService = new RewardService(gpsProxy, rewardProxy);
		tripPricerProxy = new TripPricerProxyDummyImpl();
		
		InternalTestHelper.setInternalUserNumber(0);
		//Note that Tracker Thread is directly disabled thanks to stopTrackerAtStartup = true
		tourGuideService = new TourGuideService(gpsProxy, rewardsService, tripPricerProxy, false, true);

	}
	
	
	@Test
	void userGetRewards() {
		//ARRANGE:		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsProxy.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));

		//ACT:
		List<User> userList = new ArrayList<>();
		userList.add(user);
		rewardsService.calculateRewardsMultiThread(userList);

		//ASSERT:
		assertEquals(1, user.getUserRewards().size());
	}
	
	@Test
	void isWithinAttractionProximity() {
		Attraction attraction = gpsProxy.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}
	
	/**
	 * Note on this test: this test is slow (around 12s) because the 26 attractions are now considered 
	 * near to our unique user since we set ProximityBuffer = Integer.MAX_VALUE.
	 * The problem is that it computes 26 rewards in the same Thread, hence doing a sequential execution.
	 * To change this behavior we might modify the threading and use a thread per user-Attraction couple instead
	 * of a thread per user, however this would add complexity for nothing since this case should never happen
	 * in real application.
	 * 
	 */
	@Test
	void nearAllAttractions() {
		//ARRANGE:
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);
		//Note that Tracker Thread is directly disabled thanks to stopTrackerAtStartup = true
		TourGuideService tourGuideService = new TourGuideService(gpsProxy, rewardsService, tripPricerProxy, false, true);
		//Create 1 user:
		UUID userid = UUID.randomUUID();
		User user = new User(userid, "userName", "phone", "mail@email.com");
		user.addToVisitedLocations(new VisitedLocation(userid, new Location(11.1,22.2), new Date()));
		tourGuideService.addUser(user);
		
		//ACT:
		rewardsService.calculateRewardsMultiThread(tourGuideService.getAllUsers());
		
		//ASSERT:
		List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
		assertEquals(gpsProxy.getAttractions().size(), userRewards.size());
	}
	
}
