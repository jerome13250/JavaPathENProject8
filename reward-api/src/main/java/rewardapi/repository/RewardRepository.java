package rewardapi.repository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import rewardCentral.RewardCentral;


@Repository
public class RewardRepository {
	
	@Autowired
	private RewardCentral rewardCentral;
	
	public int getAttractionRewardPoints(UUID attractionId, UUID userId) {
		return rewardCentral.getAttractionRewardPoints(attractionId, userId);
	}

}
