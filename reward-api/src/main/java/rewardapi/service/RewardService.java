package rewardapi.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rewardapi.repository.RewardRepository;

@Service
public class RewardService {

	@Autowired
	private RewardRepository rewardRepository;
	
	public Integer getAttractionRewardPoints(UUID attractionId, UUID userId) {
		return rewardRepository.getAttractionRewardPoints(attractionId, userId);
	}
	
}
