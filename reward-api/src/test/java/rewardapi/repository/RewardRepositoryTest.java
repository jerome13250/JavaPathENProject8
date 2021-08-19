package rewardapi.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import rewardCentral.RewardCentral;

@ExtendWith(MockitoExtension.class)
class RewardRepositoryTest {
	
	@InjectMocks
	private RewardRepository gpsRepository;

	@Mock
	private RewardCentral rewardCentral;
	
	@Test
	void testGetUserLocation()  throws Exception {
		//ARRANGE:
		UUID attractionid = UUID.randomUUID();
		UUID userid = UUID.randomUUID();
		when(rewardCentral.getAttractionRewardPoints(attractionid, userid)).thenReturn(999);

		//ACT:
		Integer result = gpsRepository.getAttractionRewardPoints(attractionid, userid);

		//ASSERT:
		assertEquals(999,result);
	}



}
