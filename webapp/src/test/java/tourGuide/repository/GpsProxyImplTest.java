package tourGuide.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import com.fasterxml.jackson.databind.ObjectMapper;

import commons.model.AttractionDTO;
import commons.model.AttractionDistance;
import commons.model.VisitedLocationDTO;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import lombok.extern.slf4j.Slf4j;
import tourGuide.model.user.User;
import tourGuide.repository.impl.GpsProxyImpl;

/**
 * This test code using @RestClientTest comes from 
 * <a href="https://rieckpil.de/testing-your-spring-resttemplate-with-restclienttest/">
 * Testing the Spring RestTemplate With @RestClientTest
 * </a>
 * @author jerome
 *
 */
@Slf4j
@RestClientTest(GpsProxyImpl.class)
class GpsProxyImplTest {

	@Autowired
	GpsProxyImpl gpsProxyImpl;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockRestServiceServer mockRestServiceServer;

	@Value( "${gpsapi.apiUrl}" )
    private String gpsApiUrl;
	
	Date date;

	@BeforeEach
	void init() {
		Calendar c = Calendar.getInstance();
		c.set(2021, 8, 26);
		date = c.getTime();
		
		//the MockRestSerivceServer provides a way to reset all expectations/recorded requests and verify that
		//every expectation was actually used. This is quite helpful and can be integrated using the JUnit test lifecycle.
		//Without this we get error: "mockRestServiceServer Cannot add more expectations after actual requests are made"
		this.mockRestServiceServer.reset();
	}
	 
	@AfterEach
	void tearDown() {
	  this.mockRestServiceServer.verify();
	}
	
	@Test
	void getVisitedLocationTest() throws Exception {
		//ARRANGE:
		UUID userid = UUID.randomUUID();
		String json = this.objectMapper
				.writeValueAsString(new VisitedLocationDTO(userid,10.1,20.2,date));

		this.mockRestServiceServer
		.expect(requestTo(gpsApiUrl + "visitedLocation?userid=" + userid))
		.andRespond(withSuccess(json, MediaType.APPLICATION_JSON));
		
		//ACT:
		VisitedLocation result = gpsProxyImpl.getVisitedLocation(userid);

		//ASSERT:
		assertEquals(userid, result.userId);
		assertEquals(10.1, result.location.latitude);
		assertEquals(20.2, result.location.longitude);
		assertEquals(date, result.timeVisited);
	}
	
	@Test
	void getNearbyAttractionsTest() throws Exception {
		//ARRANGE:
		UUID uuid1 = UUID.randomUUID();
		User user1 = new User(uuid1, "john", "1-123", "john@mail.com");
		user1.addToVisitedLocations(new VisitedLocation(uuid1, new Location(22.2, 11.1), date));
		
		List<AttractionDistance> listAttractionDistance = new ArrayList<>();
		AttractionDistance ad1 = new AttractionDistance("attractionName1",10.1,20.2,999.9,1500);
		AttractionDistance ad2 = new AttractionDistance("attractionName2",10.2,20.3,123.23,589);
		listAttractionDistance.add(ad1);
		listAttractionDistance.add(ad2);
		
		String json = this.objectMapper
				.writeValueAsString(listAttractionDistance);

		String url = gpsApiUrl + "closestAttractions?long=11.1&lat=22.2&number=2";
				
		this.mockRestServiceServer
		.expect(requestTo(url))
		.andRespond(withSuccess(json, MediaType.APPLICATION_JSON));
		
		//ACT:
		List<AttractionDistance> result = gpsProxyImpl.getNearbyAttractions(user1,2 );

		//ASSERT:
		assertEquals(2, result.size());
		assertEquals(listAttractionDistance.get(0), result.get(0));
		assertEquals(listAttractionDistance.get(1), result.get(1));
	}

	@Test
	void getAttractionsTest() throws Exception {
		//ARRANGE:
		List<AttractionDTO> listAttractionDTO = new ArrayList<>();
		AttractionDTO adto1 = new AttractionDTO(11.1,22.2,"attractionName","city","state",UUID.randomUUID());
		AttractionDTO adto2 = new AttractionDTO(33.3,44.4,"attractionName","city","state",UUID.randomUUID());
		listAttractionDTO.add(adto1);
		listAttractionDTO.add(adto2);
		
		String json = this.objectMapper
				.writeValueAsString(listAttractionDTO);

		String url = gpsApiUrl + "attractions";
		
		
		this.mockRestServiceServer
		.expect(requestTo(url))
		.andRespond(withSuccess(json, MediaType.APPLICATION_JSON));
		
		//ACT:
		List<Attraction> result = gpsProxyImpl.getAttractions();

		//ASSERT:
		assertEquals(2, result.size());
		assertEquals(listAttractionDTO.get(0).getLongitude(), result.get(0).longitude);
		assertEquals(listAttractionDTO.get(0).getLatitude(), result.get(0).latitude);
		assertEquals(listAttractionDTO.get(1).getLongitude(), result.get(1).longitude);
		assertEquals(listAttractionDTO.get(1).getLatitude(), result.get(1).latitude);

	}
	
}