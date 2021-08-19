package trippricerapi.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.PositiveOrZero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import tripPricer.Provider;
import trippricerapi.service.TripPricerService;

@RestController
@Validated
public class TripPricerController {

	@Autowired
	TripPricerService tripPricerService;

	@ApiOperation(value = "This url returns the list of price proposals by different operators.")
	@GetMapping("/price")
	public List<Provider> getPrice(
			@ApiParam(
					value = "Key to get access to the api.",
					example = "test-server-api-key")
			@RequestParam String apiKey,
			@ApiParam(
					value = "User ID in UUID format.",
					example = "123e4567-e89b-12d3-a456-426614174000")
			@RequestParam UUID attractionId,
			@ApiParam(
					value = "Number of adults.",
					example = "2")
			@RequestParam @PositiveOrZero Integer adults,
			@ApiParam(
					value = "Number of children.",
					example = "3")
			@RequestParam @PositiveOrZero Integer children,
			@ApiParam(
					value = "Number of night stay.",
					example = "10")
			@RequestParam @PositiveOrZero Integer nightsStay,
			@ApiParam(
					value = "Number of rewards points.",
					example = "489")
			@RequestParam @PositiveOrZero Integer rewardsPoints	) 	{
		
		return tripPricerService.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
	}

}
