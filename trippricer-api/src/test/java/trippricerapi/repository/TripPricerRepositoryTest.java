package trippricerapi.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tripPricer.Provider;
import tripPricer.TripPricer;


@ExtendWith(MockitoExtension.class)
class TripPricerRepositoryTest {
	
	@InjectMocks
	private TripPricerRepository tripPricerRepository;

	@Mock
	private TripPricer tripPricer;
	
	@Test
	void testGetUserLocation()  throws Exception {
		//ARRANGE:
		UUID attractionid = UUID.randomUUID();
		Provider provider1 = new Provider(attractionid, "provider1", 100);
		Provider provider2 = new Provider(attractionid, "provider2", 200);
		List<Provider> listProvider = new ArrayList<>();
		listProvider.add(provider1);
		listProvider.add(provider2);
		when(tripPricer.getPrice("apiKey", attractionid, 2, 5, 10, 384)).thenReturn(listProvider);
		
		//ACT:
		List<Provider> listProviderResult = tripPricerRepository.getPrice("apiKey", attractionid, 2, 5, 10, 384);

		//ASSERT:
		assertEquals(2,listProviderResult.size());
	}
	
}
