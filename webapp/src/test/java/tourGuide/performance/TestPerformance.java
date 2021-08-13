package tourGuide.performance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.slf4j.SLF4JLoggerContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import lombok.extern.slf4j.Slf4j;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.user.User;
import tourGuide.repository.GpsProxy;
import tourGuide.repository.impl.GpsProxyImpl;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

@Slf4j
class TestPerformance {
	
	
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
		//Since we do not use @SpringBootTest, the context and application.properties are not loaded
		//we have to set log levels here otherwise we are flooded by DEBUG messages from HTTP or RestTemplate
		setLogLevel("INFO", "tourGuide.repository.impl.GpsProxyImpl");
		setLogLevel("INFO", "org.springframework.web.client.RestTemplate");
		setLogLevel("INFO", "org.springframework.web.HttpLogging");
		
		//ARRANGE:
		Locale.setDefault(Locale.US); //necessary because of bug in GpsUtil .jar
		GpsUtil gpsUtil = new GpsUtil();
		
		GpsProxyImpl gpsProxy = new GpsProxyImpl();
		//note : since spring boot is not used to create context, GpsProxyImpl.gpsApiUrl is unset.
		//we need to set it:
		gpsProxy.setGpsApiUrl("http://localhost:9001/");
		
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		// Users should be incremented up to 100,000, and test finishes within 15 minutes
		InternalTestHelper.setInternalUserNumber(100);
		//Note that Tracker Thread is directly disabled thanks to stopTrackerAtStartup = true
		TourGuideService tourGuideService = new TourGuideService(gpsProxy, gpsUtil, rewardsService, true);
		List<User> allUsers = tourGuideService.getAllUsers();
		
		//ACT:
	    StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		//Multithread:
		log.debug("Multithread test is launching user track ");
		tourGuideService.trackUserLocationMultiThread(allUsers);
		stopWatch.stop();
		log.info("highVolumeTrackLocation: Time Elapsed: {} seconds." 
				, TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())); 

		//ASSERT:
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		for(User user : allUsers) {
			assertEquals(4,user.getVisitedLocations().size(),"Internal users are created with 3 visitedLocation + 1 tracked in this test");
		}
	}
	
	
	@Test
	@Disabled
	public void highVolumeGetRewards() {
		//ARRANGE:
		Locale.setDefault(Locale.US); //necessary because of bug in GpsUtil .jar
		GpsUtil gpsUtil = new GpsUtil();
		GpsProxy gpsProxy = new GpsProxyImpl();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		InternalTestHelper.setInternalUserNumber(10);
		//Note that Tracker Thread is directly disabled thanks to stopTrackerAtStartup = true
		TourGuideService tourGuideService = new TourGuideService(gpsProxy, gpsUtil, rewardsService, true);
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
		log.info("highVolumeGetRewards: Time Elapsed: {} seconds.", TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())); 
		
	    //ASSERT:
		for(User user : allUsers) {
			assertTrue(user.getUserRewards().size() > 0);
		}
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}
	
	/**
	 * This allows to set the log level at runtime
	 * note that it is not possible to change the log level dynamically in slf4j, but backends for slf4j support it.
	 * 
	 * @param logLevel the required log level
	 * @param packageName the package on which modify log level
	 */
	private static void setLogLevel(String logLevel, String packageName) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger(packageName);
        
        System.out.println(packageName + " current logger level: " + logger.getLevel());
        System.out.println(" You entered: " + logLevel);
 
        logger.setLevel(Level.toLevel(logLevel));
    }
	
	
}
