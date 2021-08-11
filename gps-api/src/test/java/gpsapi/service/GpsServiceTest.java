package gpsapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import commons.model.AttractionDistance;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import gpsapi.repository.GpsRepository;

@ExtendWith(MockitoExtension.class)
class GpsServiceTest {

	@InjectMocks
	GpsService gsService;

	@Mock
	GpsRepository gpsRepository;

	@Test
	void test_getUserLocation() {
		//ARRANGE:
		UUID userId = UUID.randomUUID();
		Location location = new Location(10.1d, 20.2d);
		Date date = new Date();
		VisitedLocation expectedVisitedLocation = new VisitedLocation(userId, location, date);
		when(gpsRepository.getUserLocation(userId)).thenReturn(expectedVisitedLocation);

		//ACT:
		VisitedLocation resultVisitedLocation = gsService.getUserLocation(userId);

		//ASSERT:
		assertEquals(expectedVisitedLocation,resultVisitedLocation,"Returned VisitedLocation must be same as gpsUtils");
	}

	
	@Test
	void test_getClosestAttractions() {
		//ARRANGE:
		Attraction at1 = new Attraction("attraction1", "city1", "state1", 10, 10);
		Attraction at2 = new Attraction("attraction2", "city2", "state2", 20, 20);
		Attraction at3 = new Attraction("attraction3", "city3", "state3", 30, 30);
		Attraction at4 = new Attraction("attraction4", "city4", "state4", 40, 40);
		Attraction at5 = new Attraction("attraction5", "city5", "state5", 50, 50);
		Attraction[] arr = {at5,at4,at3,at2,at1};
		List<Attraction> listAttraction = Arrays.asList(arr);
		when(gpsRepository.getAttractions()).thenReturn(listAttraction);
		Location userLocation = new Location(5, 5);
		
		//ACT:
		List<AttractionDistance> resultListAttractionDistance = gsService.getClosestAttractions(userLocation, 3);

		//ASSERT:
		assertEquals(3,resultListAttractionDistance.size(),"Returned List is expected to have size=3");
		assertEquals("attraction1",resultListAttractionDistance.get(0).getAttractionName());
		assertEquals(10,resultListAttractionDistance.get(0).getLatitude());
		assertEquals(10,resultListAttractionDistance.get(0).getLongitude());
		assertEquals(486,resultListAttractionDistance.get(0).getDistance(),1);
		assertEquals("attraction2",resultListAttractionDistance.get(1).getAttractionName());
		assertEquals(20,resultListAttractionDistance.get(1).getLatitude());
		assertEquals(20,resultListAttractionDistance.get(1).getLongitude());
		assertEquals(1445,resultListAttractionDistance.get(1).getDistance(),1);
		assertEquals("attraction3",resultListAttractionDistance.get(2).getAttractionName());
		assertEquals(30,resultListAttractionDistance.get(2).getLatitude());
		assertEquals(30,resultListAttractionDistance.get(2).getLongitude());
		assertEquals(2372,resultListAttractionDistance.get(2).getDistance(),1);
		
	}

}
