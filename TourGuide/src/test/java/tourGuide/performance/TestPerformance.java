package tourGuide.performance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gpsUtil.GpsUtil;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.user.User;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

public class TestPerformance {
	
	private Logger logger = LoggerFactory.getLogger(TestPerformance.class);
	
	
	/*
	 * A note on performance improvements:
	 *     
	 *     The number of users generated for the high volume tests can be easily adjusted via this method:
	 *     
	 *     		InternalTestHelper.setInternalUserNumber(100000);
	 *     
	 *     
	 *     These tests can be modified to suit new solutions, just as long as the performance metrics
	 *     at the end of the tests remains consistent. 
	 * 
	 *     These are performance metrics that we are trying to hit:
	 *     
	 *     highVolumeTrackLocation: 100,000 users within 15 minutes:
	 *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
	 *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 */
	

	@Test
	public void highVolumeTrackLocation() {
		//ARRANGE:
		Locale.setDefault(Locale.US); //necessary because of bug in GpsUtil .jar
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		// Users should be incremented up to 100,000, and test finishes within 15 minutes
		InternalTestHelper.setInternalUserNumber(1000);
		//Note that Tracker Thread is directly disabled thanks to stopTrackerAtStartup = true
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, true);
		List<User> allUsers = tourGuideService.getAllUsers();
		
		//ACT:
	    StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		//Multithread:
		logger.debug("Multithread test is launching user track ");
		tourGuideService.trackUserLocationMultiThread(allUsers);
		stopWatch.stop();
		logger.info("highVolumeTrackLocation: Time Elapsed: {} seconds." , TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())); 

		//ASSERT:
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		for(User user : allUsers) {
			assertEquals(4,user.getVisitedLocations().size());
		}
	}
	
	/*
	@Test
	public void highVolumeGetRewards() {
		//ARRANGE:
		Locale.setDefault(Locale.US); //necessary because of bug in GpsUtil .jar
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		InternalTestHelper.setInternalUserNumber(100000);
		//Note that Tracker Thread is directly disabled thanks to stopTrackerAtStartup = true
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, true);
		//Add the first attraction in GpsUtils internal list to all users:
		Attraction attraction = gpsUtil.getAttractions().get(0);
		List<User> allUsers = tourGuideService.getAllUsers();
		allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));
		
		//ACT:
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		//multithread:
		rewardsService.calculateRewardsMultiThread(allUsers);
		stopWatch.stop();
		logger.info("highVolumeGetRewards: Time Elapsed: {} seconds.", TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())); 
		
	    //ASSERT:
		for(User user : allUsers) {
			assertTrue(user.getUserRewards().size() > 0);
		}
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}
	*/
}
