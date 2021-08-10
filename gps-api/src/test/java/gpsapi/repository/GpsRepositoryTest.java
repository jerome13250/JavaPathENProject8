package gpsapi.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;

@ExtendWith(MockitoExtension.class)
class GpsRepositoryTest {
	
	@InjectMocks
	private GpsRepository gpsRepository;

	@Mock
	private GpsUtil gpsUtil;
	
	@Test
	void testGetUserLocation()  throws Exception {
		//ARRANGE:
		UUID userId = UUID.randomUUID();
		Location location = new Location(10.1d, 20.2d);
		Date date = new Date();
		VisitedLocation expectedVisitedLocation = new VisitedLocation(userId, location, date);
		when(gpsUtil.getUserLocation(userId)).thenReturn(expectedVisitedLocation);

		//ACT:
		VisitedLocation resultVisitedLocation = gpsRepository.getUserLocation(userId);

		//ASSERT:
		assertEquals(expectedVisitedLocation,resultVisitedLocation,"Returned VisitedLocation must be same as gpsUtils");
	}

	@Test
	void testGetAttractions()  throws Exception {
		//ARRANGE:
		Attraction attraction = new Attraction("Disney", "Miami", "Florida", 10.1d, 20.2d);
		List<Attraction> expectedListAttraction = new ArrayList<>();
		expectedListAttraction.add(attraction);
		when(gpsUtil.getAttractions()).thenReturn(expectedListAttraction);

		//ACT:
		List<Attraction> resultListAttraction = gpsRepository.getAttractions();

		//ASSERT:
		assertEquals(expectedListAttraction,resultListAttraction,"Returned ListAttraction must be same as gpsUtils");
	}
	

}
