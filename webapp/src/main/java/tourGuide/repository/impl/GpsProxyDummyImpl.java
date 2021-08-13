package tourGuide.repository.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import commons.model.AttractionDistance;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import lombok.extern.slf4j.Slf4j;
import tourGuide.model.user.User;
import tourGuide.repository.GpsProxy;

/**
 * For integration test : dummy Implementation of communication between webapp and the REST gps-api.
 * <p>
 * gps-api is an external service, so for integration tests we create this dummy implementation that will
 * always return the same results whatever the parameters...
 *</p> 
 * 
 * @author jerome
 *
 */
@Slf4j
public class GpsProxyDummyImpl implements GpsProxy {

	@Override
	public VisitedLocation getVisitedLocation(UUID userid) {
		Location location = new Location(11.1, 22.2);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
		String dateInString = "7-Jun-2013";
		Date date;
		try {
			date = formatter.parse(dateInString);
			VisitedLocation visitedLocation = new VisitedLocation(userid,location,date );
			return visitedLocation;
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	@Override
	public List<AttractionDistance> getNearbyAttractions(User user, Integer numberOfAttraction) {
		
		AttractionDistance atd1 = new AttractionDistance("attraction1", 10d, 10d, 100.1, 0);
		AttractionDistance atd2 = new AttractionDistance("attraction2", 20d, 20d, 200.2, 0);
		AttractionDistance atd3 = new AttractionDistance("attraction3", 30d, 30d, 300.3, 0);
		AttractionDistance atd4 = new AttractionDistance("attraction4", 40d, 40d, 400.4, 0);
		AttractionDistance atd5 = new AttractionDistance("attraction5", 50d, 50d, 500.5, 0);
		AttractionDistance[] arr = {atd1,atd2,atd3,atd4,atd5};
		List<AttractionDistance> listAttractionDistance = Arrays.asList(arr);

    	return listAttractionDistance;
    }

}