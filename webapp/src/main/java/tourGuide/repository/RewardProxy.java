package tourGuide.repository;

import java.util.UUID;

public interface RewardProxy {

	/**
	 * Returns the rewards points calculated by the reward api for the attraction id / user id provided.
	 * @param attractionid UUID of the attraction 
	 * @param userid UUID of the user
	 * @return the rewards points for the user 
	 */
	Integer getAttractionRewardPoints(UUID attractionid, UUID userid);


}
