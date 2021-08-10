package tourGuide.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.user.User;
import tourGuide.model.user.UserReward;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

public class TestRewardsService {

	TourGuideService tourGuideService;
	RewardsService rewardsService;
	GpsUtil gpsUtil;
	
	@BeforeEach
	public void setup() {
		//we have a bug in external jar GpsUtils due to String.format("%.6f", new Object[] { Double.valueOf(longitude) })),
		//format uses Locale.getDefault() that create string Double with "," (when Locale=FR) instead of "."
		//For this reason i need to change the default Locale:
		Locale.setDefault(Locale.US);
		
		gpsUtil = new GpsUtil();
		rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		InternalTestHelper.setInternalUserNumber(0);
		//Note that Tracker Thread is directly disabled thanks to stopTrackerAtStartup = true
		tourGuideService = new TourGuideService(gpsUtil, rewardsService, true);

	}
	
	
	@Test
	public void userGetRewards() {
		//ARRANGE:		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtil.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));

		//ACT:
		List<User> userList = new ArrayList<>();
		userList.add(user);
		rewardsService.calculateRewardsMultiThread(userList);

		//ASSERT:
		assertEquals(1, user.getUserRewards().size());
	}
	
	@Test
	public void isWithinAttractionProximity() {
		Attraction attraction = gpsUtil.getAttractions().get(0);
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
	public void nearAllAttractions() {
		//ARRANGE:
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);
		//to stay consistent with this existing test, tourGuideService is specific to this function with setInternalUserNumber(1):
		InternalTestHelper.setInternalUserNumber(1);
		//Note that Tracker Thread is directly disabled thanks to stopTrackerAtStartup = true
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, true);
		
		//ACT:
		rewardsService.calculateRewardsMultiThread(tourGuideService.getAllUsers());
		
		//ASSERT:
		List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
		assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
	}
	
}
