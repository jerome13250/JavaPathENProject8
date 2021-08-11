package gpsapi.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import commons.model.AttractionDistance;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import gpsapi.service.GpsService;

@RestController
@Validated
public class GpsController {
	
	@Autowired
	GpsService gpsService;
	
	@GetMapping("/visitedLocation")
	public VisitedLocation getVisitedLocation(@RequestParam UUID userid) {
		return gpsService.getUserLocation(userid);
	}
	
	@GetMapping("/closestAttractions")
	public List<AttractionDistance> getListClosestAttractions(
			@RequestParam("long") @Min(-180) @Max(180) Double longitude,
			@RequestParam("lat") @Min(-180) @Max(180) Double latitude,
			@RequestParam("number") @Positive Integer numberOfAttractions) {
		
		Location userLocation = new Location(latitude, longitude);
		return gpsService.getClosestAttractions(userLocation, numberOfAttractions);
	}

}
