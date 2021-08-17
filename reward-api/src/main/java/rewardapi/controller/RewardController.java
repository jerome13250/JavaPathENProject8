package rewardapi.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import rewardapi.service.RewardService;

@RestController
@Validated
public class RewardController {

	@Autowired
	RewardService rewardService;

	@ApiOperation(value = "This url returns the number of rewards points for a required user id and attraction id.")
	@GetMapping("/attractionRewardPoints")
	public int getAttractionRewardPoints(
			@ApiParam(
					value = "Attraction ID in UUID format.",
					example = "123e4567-e89b-12d3-a456-426614174000")
			@RequestParam UUID attractionid,
			
			@ApiParam(
					value = "User ID in UUID format.",
					example = "123e4567-e89b-12d3-a456-426614174000")
			@RequestParam UUID userid) 	{
		return rewardService.getAttractionRewardPoints(attractionid, userid);
	}

}
