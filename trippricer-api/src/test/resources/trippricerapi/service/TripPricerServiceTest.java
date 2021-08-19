package trippricerapi.service;

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
import trippricerapi.repository.TripPricerRepository;

@ExtendWith(MockitoExtension.class)
class TripPricerServiceTest {

	@InjectMocks
	TripPricerService tripPricerService;

	@Mock
	TripPricerRepository tripPricerRepository;

	@Test
	void test_getUserLocation() {
		//ARRANGE:
		UUID attractionid = UUID.randomUUID();
		Provider provider1 = new Provider(attractionid, "provider1", 100);
		Provider provider2 = new Provider(attractionid, "provider2", 200);
		List<Provider> listProvider = new ArrayList<>();
		listProvider.add(provider1);
		listProvider.add(provider2);
		when(tripPricerRepository.getPrice("apiKey", attractionid, 2, 5, 10, 384)).thenReturn(listProvider);

		//ACT:
		List<Provider> listProviderResult = tripPricerService.getPrice("apiKey", attractionid, 2, 5, 10, 384);

		//ASSERT:
		assertEquals(2,listProviderResult.size());
	}

}
