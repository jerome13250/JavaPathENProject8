package gpsapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import commons.model.AttractionDTO;
import commons.model.AttractionDistance;
import commons.model.VisitedLocationDTO;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import gpsapi.service.GpsService;

//@WebMvcTest annotation is used for Spring MVC tests. 
//It disables full auto-configuration and instead apply only configuration relevant to MVC tests.
//It auto-configures MockMvc instance as well.
@WebMvcTest(controllers = GpsController.class)  // we are asking to initialize only one web controller
class GpsControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GpsService gpsService;


	@Test
	void testGetVisitedLocation() throws Exception {
		//ARRANGE:
		UUID uuid = UUID.randomUUID();
		Location location = new Location(11.11,22.22);
		VisitedLocation visitedLocation = new VisitedLocation(uuid, location, new Date());
		when(gpsService.getUserLocation(uuid)).thenReturn(visitedLocation);
		
		
		//ACT:
		MvcResult result = 
				mockMvc.perform(get("/visitedLocation?userid="+uuid.toString()))
				.andExpect(status().isOk())
				.andReturn(); //this allows to get the MvcResult

		//ASSERT:
		VisitedLocationDTO visitedLocationDTO = 
				objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<VisitedLocationDTO>() {});
		assertNotNull(visitedLocationDTO);
		assertEquals(visitedLocationDTO.getUserId(), visitedLocation.userId);
		assertEquals(visitedLocationDTO.getLocation_latitude(),visitedLocation.location.latitude);
		assertEquals(visitedLocationDTO.getLocation_longitude(),visitedLocation.location.longitude);
		assertEquals(visitedLocationDTO.getTimeVisited(),visitedLocation.timeVisited);		
	}

	@Test
	void testGetListClosestAttractions() throws Exception {
		//ARRANGE:
		AttractionDistance ad1 = new AttractionDistance();
		AttractionDistance ad2 = new AttractionDistance();
		List<AttractionDistance> listAttractionDistance = new ArrayList<>();
		listAttractionDistance.add(ad1);
		listAttractionDistance.add(ad2);

		when(gpsService.getClosestAttractions(any(Location.class), any(Integer.class))).thenReturn(listAttractionDistance);

		//ACT:
		MvcResult result = 
				mockMvc.perform(get("/closestAttractions?long=10.1&lat=20.2&number=2"))
				.andExpect(status().isOk())
				.andReturn(); //this allows to get the MvcResult

		//ASSERT:
		verify(gpsService).getClosestAttractions(any(Location.class), any(Integer.class));
		List<AttractionDistance> resultListAttractionDistance = 
				objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<AttractionDistance>>() {});
		assertNotNull(resultListAttractionDistance);
		assertEquals(2, resultListAttractionDistance.size());

	}

	@Test
	void testGetAttractions() throws Exception {
		//ARRANGE:
		Attraction at1 = new Attraction("attractionName1", "city1", "state1", 10, 10);
		Attraction at2 = new Attraction("attractionName2", "city2", "state2", 20, 20);
		List<Attraction> listAttraction = new ArrayList<>();
		listAttraction.add(at1);
		listAttraction.add(at2);

		when(gpsService.getAttractions()).thenReturn(listAttraction);

		//ACT:
		MvcResult result = 
				mockMvc.perform(get("/attractions"))
				.andExpect(status().isOk())
				.andReturn(); //this allows to get the MvcResult

		//ASSERT:
		List<AttractionDTO> resultListAttractionDTO = 
				objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<AttractionDTO>>() {});
		assertEquals(2, resultListAttractionDTO.size());
		assertEquals("attractionName1", resultListAttractionDTO.get(0).getAttractionName());
		assertEquals("attractionName2", resultListAttractionDTO.get(1).getAttractionName());

	}


}