package tourGuide.repository.impl;

import java.util.UUID;

import tourGuide.repository.RewardProxy;

/**
 * For integration test : dummy Implementation of communication between webapp and the REST reward-api.
 * <p>
 * reward-api is an external service, so for integration tests we create this dummy implementation that will
 * always return the same results whatever the parameters...
 *</p> 
 * 
 * @author jerome
 *
 */

public class RewardProxyDummyImpl implements RewardProxy {@Override
	
	public Integer getAttractionRewardPoints(UUID attractionid, UUID userid) {
		return 999;
	}

}