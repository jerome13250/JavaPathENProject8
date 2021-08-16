package gpsapi.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import commons.model.AttractionDTO;
import commons.model.AttractionDistance;
import commons.model.VisitedLocationDTO;
import gpsUtil.location.Location;
import gpsapi.service.GpsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Validated
public class GpsController {

	@Autowired
	GpsService gpsService;

	@ApiOperation(value = "This url returns a VisitedLocation for a required user id(UUID format).")
	@GetMapping("/visitedLocation")
	public VisitedLocationDTO getVisitedLocation(
			@ApiParam(
					value = "User ID in UUID format.",
					example = "123e4567-e89b-12d3-a456-426614174000")
			@RequestParam UUID userid) {
		VisitedLocationDTO visitedLocationDTO = new VisitedLocationDTO();
		visitedLocationDTO.copyValuesFrom(gpsService.getUserLocation(userid));
		return visitedLocationDTO;
	}

	@ApiOperation(value = "This url returns the list of closest attractions to the place with required latitude/longitude."
			+ "The number of returned attractions depends on required numberOfAttractions.")
	@GetMapping("/closestAttractions")
	public List<AttractionDistance> getListClosestAttractions(
			@ApiParam(
					value = "Longitude of the place.",
					example = "-73.968849182")
			@RequestParam("long") @Min(-180) @Max(180) Double longitude,
			
			@ApiParam(
					value = "Latitude of the place.",
					example = "40.78037261962")
			@RequestParam("lat") @Min(-180) @Max(180) Double latitude,
			
			@ApiParam(
					value = "Number of attractions to return.",
					example = "4")
			@RequestParam("number") @Positive Integer numberOfAttractions) {

		Location userLocation = new Location(latitude, longitude);
		return gpsService.getClosestAttractions(userLocation, numberOfAttractions);
	}

	@ApiOperation(value = "This url returns the list of all attractions of the gpsUtil.")
	@GetMapping("/attractions")
	public List<AttractionDTO> getAttractions() {

		//convert List<Attraction> to List<AttractionDTO>
		return gpsService.getAttractions().stream().map(at -> new AttractionDTO(at)).collect(Collectors.toList());

	}




}
