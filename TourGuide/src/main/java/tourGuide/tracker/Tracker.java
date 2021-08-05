package tourGuide.tracker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tourGuide.model.user.User;
import tourGuide.service.TourGuideService;

/**
 * Tracker class extends Thread, when this class is created it first creates an ExecutorService then in its constructor 
 * it submits itself to ExecutorService to get executed as a Thread.
 * @author jerome
 *
 */
public class Tracker extends Thread {
	private Logger logger = LoggerFactory.getLogger(Tracker.class);
	private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final TourGuideService tourGuideService;
	private boolean stop = false;

	/**
	 * Constructor for Tracker, submits itself to ExecutorService to get executed as a Thread.
	 * 
	 * @param tourGuideService that requires to create the Tracker.
	 * @param stop boolean used to disable Tracker at creation, this allows to make 
	 * tests without tracker thread running.
	 */
	public Tracker(TourGuideService tourGuideService, boolean stopTrackerAtStartup) {
		this.tourGuideService = tourGuideService;
		
		if (stopTrackerAtStartup) {
			this.stop = true;
			executorService.shutdownNow();
		}
		else {	
			executorService.submit(this);
		}
	}
	
	/**
	 * Assures to shut down the Tracker thread
	 */
	public void stopTracking() {
		logger.debug("Tracker is required to stop in function Tracker.stopTracking()");
		stop = true;
		executorService.shutdownNow();
	}
	
	@Override
	public void run() {
		StopWatch stopWatch = new StopWatch(); //apache.commons convenient API for timings
		while(true) {
			if(Thread.currentThread().isInterrupted() || stop) {
				logger.debug("Tracker stopping");
				break;
			}
			
			List<User> users = tourGuideService.getAllUsers();
			logger.debug("Begin Tracker. Tracking " + users.size() + " users.");
			stopWatch.start();
			
			//loop through all users and launch trackUserLocation, sequential execution 
			//old monothread:
			/*
			users.forEach(u -> {
				logger.debug("Tracker is launching user track ");
				tourGuideService.trackUserLocation(u);
				});
			*/
			
			//multithread:
			tourGuideService.trackUserLocationMultiThread(users);
			
			
			stopWatch.stop();
			logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
			stopWatch.reset();
			try {
				logger.debug("Tracker sleeping");
				TimeUnit.SECONDS.sleep(trackingPollingInterval); //Pause for 5 minutes
			} catch (InterruptedException e) {
				break;
			}
		}
		
	}
}
