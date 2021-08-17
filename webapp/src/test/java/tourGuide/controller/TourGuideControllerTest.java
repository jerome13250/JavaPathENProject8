package tourGuide.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import commons.model.AttractionDistance;
import commons.model.ClosestAttractionsDTO;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import lombok.extern.slf4j.Slf4j;
import commons.exception.BusinessResourceException;
import tourGuide.model.user.User;
import tourGuide.model.user.UserPreferencesDTO;
import tourGuide.model.user.UserReward;
import tourGuide.service.TourGuideService;
import tripPricer.Provider;

@Slf4j
//@WebMvcTest tells Spring Boot to instantiate only the web layer and not the entire context
@WebMvcTest(controllers = TourGuideController.class) 
class TourGuideControllerTest {

	@Autowired
	private MockMvc mockMvc;
	//i reuse the existing bean ObjectMapper as it has a specific Date format, to compare json dates i need the same config
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private TourGuideService tourGuideService;

	UUID uuid1;
	User user1;
	Location location1;
	Location location2;
	VisitedLocation visitedLocation1;
	VisitedLocation visitedLocation2;
	Attraction at1;
	Attraction at2;
	Attraction at3;
	Attraction at4;
	Attraction at5;
	AttractionDistance atd1;
	AttractionDistance atd2;
	AttractionDistance atd3;
	AttractionDistance atd4;
	AttractionDistance atd5;
	UserReward userReward1;
	UserReward userReward2;

	@BeforeEach
	void setup() {

		uuid1 = UUID.randomUUID();
		user1 = new User(uuid1, "john", "1-123", "john@mail.com");
		location1 = new Location (11.1,22.2);
		visitedLocation1 = new VisitedLocation(uuid1, location1, new Date());
		location2 = new Location (33.3,44.4);
		visitedLocation2 = new VisitedLocation(uuid1, location2, new Date());

		at1 = new Attraction("attraction1", "city1", "state1", 10d, 10d);
		at2 = new Attraction("attraction2", "city2", "state2", 20d, 20d);
		at3 = new Attraction("attraction3", "city3", "state3", 30d, 30d);
		at4 = new Attraction("attraction4", "city4", "state4", 40d, 40d);
		at5 = new Attraction("attraction5", "city5", "state5", 50d, 50d);

		atd1 = new AttractionDistance("attraction1", 10d, 10d , 111.11, 1);
		atd2 = new AttractionDistance("attraction2", 20d, 20d, 222.22, 2);
		atd3 = new AttractionDistance("attraction3", 30d, 30d, 333.33, 3);
		atd4 = new AttractionDistance("attraction4", 40d, 40d, 444.44, 4);
		atd5 = new AttractionDistance("attraction5", 50d, 50d, 555.55, 6);

		userReward1 = new UserReward(visitedLocation1, at1);
		userReward2 = new UserReward(visitedLocation2, at2);
	}

	@Test
	void GET_index_shouldSucceed() throws Exception {

		//ACT + ASSERT
		mockMvc
		.perform(get("/"))
		.andExpect(status().isOk());

	}

	@Test
	void GET_getLocation_shouldSucceed() throws Exception {

		//ARRANGE
		when(tourGuideService.getUser("john")).thenReturn(user1);
		when(tourGuideService.getUserLocation(user1)).thenReturn(visitedLocation1);

		//ACT
		MvcResult result = mockMvc
				.perform(get("/getLocation?userName=john"))
				.andExpect(status().isOk())
				.andReturn();

		//ASSERT
		//Note: Location has no default empty constructor so cannot use objectMapper to deserialize it. 
		//See https://stackoverflow.com/questions/53191468/no-creators-like-default-construct-exist-cannot-deserialize-from-object-valu
		//So i just do a json String comparison.
		String resultLocationAsString = result.getResponse().getContentAsString();
		assertNotNull(resultLocationAsString);
		assertEquals(objectMapper.writeValueAsString(location1), resultLocationAsString);

	}
	
	@Test
	void GET_getLocation_UnknownUser_shouldFail() throws Exception {

		//ARRANGE
		when(tourGuideService.getUser("john")).thenThrow(new BusinessResourceException(
				"patchUserPreferencesError",
				"User is unknown: "+ "john",
				HttpStatus.NOT_FOUND));

		//ACT
		MvcResult result = mockMvc
				.perform(get("/getLocation?userName=john"))
				.andExpect(status().isNotFound())
				.andReturn();

		//ASSERT
		String responseAsString = result.getResponse().getContentAsString();
		assertTrue(responseAsString.contains("\"errorMessage\":\"User is unknown: john\""));

	}

	@Test
	void GET_getNearbyAttractions_shouldSucceed() throws Exception {

		//ARRANGE
		ClosestAttractionsDTO closestAttractionsDTO = new ClosestAttractionsDTO();
		closestAttractionsDTO.setUserLocation(location1);
		AttractionDistance[] arr = {atd1,atd2,atd3,atd4,atd5};
		List<AttractionDistance> listAttractionDistance = Arrays.asList(arr);
		closestAttractionsDTO.setAttractionList(listAttractionDistance);

		when(tourGuideService.getNearbyAttractions("john")).thenReturn(closestAttractionsDTO);

		//ACT
		MvcResult result = mockMvc
				.perform(get("/getNearbyAttractions?userName=john"))
				.andExpect(status().isOk())
				.andReturn();

		//ASSERT
		//Note: Location has no default empty constructor so cannot use objectMapper to deserialize closestAttractionsDTO. 
		//See https://stackoverflow.com/questions/53191468/no-creators-like-default-construct-exist-cannot-deserialize-from-object-valu
		//So i just do a String comparison.
		String resultAsString = result.getResponse().getContentAsString();
		assertNotNull(resultAsString);
		assertEquals(objectMapper.writeValueAsString(closestAttractionsDTO), resultAsString);
	}

	@Test
	void GET_getRewards_shouldSucceed() throws Exception {

		//ARRANGE
		UserReward[] arr = {userReward1,userReward2};
		List<UserReward> listUserReward = Arrays.asList(arr);
		when(tourGuideService.getUser("john")).thenReturn(user1);
		when(tourGuideService.getUserRewards(user1)).thenReturn(listUserReward);

		//ACT
		MvcResult result = mockMvc
				.perform(get("/getRewards?userName=john"))
				.andExpect(status().isOk())
				.andReturn();

		//ASSERT
		//Note: Location has no default empty constructor so cannot use objectMapper to deserialize closestAttractionsDTO. 
		//See https://stackoverflow.com/questions/53191468/no-creators-like-default-construct-exist-cannot-deserialize-from-object-valu
		//So i just do a String comparison.
		String resultAsString = result.getResponse().getContentAsString();
		assertNotNull(resultAsString);
		assertEquals(objectMapper.writeValueAsString(listUserReward), resultAsString);
	}

	@Test
	void GET_getAllCurrentLocations_shouldSucceed() throws Exception {

		//ARRANGE
		UUID uuid2 = UUID.randomUUID();
		Map<UUID,Location> map = new HashMap<>();
		map.put(uuid1, location1);
		map.put(uuid2, location2);
		when(tourGuideService.getAllCurrentLocations()).thenReturn(map);

		//ACT
		MvcResult result = mockMvc
				.perform(get("/getAllCurrentLocations"))
				.andExpect(status().isOk())
				.andReturn();

		//ASSERT
		//Note: Location has no default empty constructor so cannot use objectMapper to deserialize closestAttractionsDTO. 
		//See https://stackoverflow.com/questions/53191468/no-creators-like-default-construct-exist-cannot-deserialize-from-object-valu
		//So i just do a String comparison.
		String resultAsString = result.getResponse().getContentAsString();
		assertNotNull(resultAsString);
		assertEquals(objectMapper.writeValueAsString(map), resultAsString);
	}



	@Test
	void GET_getTripDeals_shouldSucceed() throws Exception {

		//ARRANGE
		Provider provider1 = new Provider(uuid1, "company1", 111.11);
		Provider provider2 = new Provider(uuid1, "company2", 222.22);
		Provider[] arr = {provider1,provider2};
		List<Provider> listProvider = Arrays.asList(arr);

		when(tourGuideService.getUser("john")).thenReturn(user1);
		when(tourGuideService.getTripDeals(user1)).thenReturn(listProvider);

		//ACT
		MvcResult result = mockMvc
				.perform(get("/getTripDeals?userName=john"))
				.andExpect(status().isOk())
				.andReturn();

		//ASSERT
		String resultAsString = result.getResponse().getContentAsString();
		assertNotNull(resultAsString);
		assertEquals(objectMapper.writeValueAsString(listProvider), resultAsString);
	}


	@Test
	void PATCH_userPreferences_shouldSucceed() throws Exception {
		//ARRANGE:
		UserPreferencesDTO userPreferencesDtoToPatch = new UserPreferencesDTO();
		userPreferencesDtoToPatch.setUserName("john");
		userPreferencesDtoToPatch.setNumberOfAdults(3);
		userPreferencesDtoToPatch.setNumberOfChildren(10);
		userPreferencesDtoToPatch.setTripDuration(55);

		String jsonContent = objectMapper.writeValueAsString(userPreferencesDtoToPatch);
		when(tourGuideService.patchUserPreferences(any(UserPreferencesDTO.class))).thenReturn(userPreferencesDtoToPatch);

		//Act
		MvcResult result = mockMvc
				.perform(patch("/userPreferences").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
				.andExpect(status().isOk())
				.andReturn();

		//Assert
		verify(tourGuideService).patchUserPreferences(any(UserPreferencesDTO.class));
		String UserPreferencesDTOReturnedJson = result.getResponse().getContentAsString();
		assertEquals(jsonContent, UserPreferencesDTOReturnedJson);

	}

	@Test
	void PATCH_userPreferences_shouldFail() throws Exception {
		//ARRANGE:
		UserPreferencesDTO userPreferencesDtoToPatch = new UserPreferencesDTO();
		userPreferencesDtoToPatch.setUserName("john");
		userPreferencesDtoToPatch.setNumberOfAdults(3);
		userPreferencesDtoToPatch.setNumberOfChildren(10);
		userPreferencesDtoToPatch.setTripDuration(55);

		String jsonContent = objectMapper.writeValueAsString(userPreferencesDtoToPatch);
		when(tourGuideService.patchUserPreferences(any(UserPreferencesDTO.class)))
		.thenThrow(new BusinessResourceException(
				"patchUserPreferencesError",
				"User is unknown: "+ userPreferencesDtoToPatch.getUserName(),
				HttpStatus.NOT_FOUND));

		//Act
		MvcResult result = mockMvc
				.perform(patch("/userPreferences").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
				.andExpect(status().isNotFound())
				.andReturn();

		//Assert
		verify(tourGuideService).patchUserPreferences(any(UserPreferencesDTO.class));
		String UserPreferencesDTOJsonString = result.getResponse().getContentAsString();
		assertTrue(UserPreferencesDTOJsonString.contains("\"errorMessage\":\"User is unknown: john\""));

	}


}
