package rewardapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import rewardapi.repository.RewardRepository;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

	@InjectMocks
	RewardService rewardService;

	@Mock
	RewardRepository rewardRepository;

	@Test
	void test_getUserLocation() {
		//ARRANGE:
		UUID attractionid = UUID.randomUUID();
		UUID userid = UUID.randomUUID();
		when(rewardRepository.getAttractionRewardPoints(attractionid, userid)).thenReturn(999);

		//ACT:
		Integer result = rewardService.getAttractionRewardPoints(attractionid, userid);

		//ASSERT:
		assertEquals(999,result);
	}

}
