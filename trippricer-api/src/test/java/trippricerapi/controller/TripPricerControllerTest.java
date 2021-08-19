package trippricerapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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

import commons.model.ProviderDTO;
import tripPricer.Provider;
import trippricerapi.service.TripPricerService;

//@WebMvcTest annotation is used for Spring MVC tests. 
//It disables full auto-configuration and instead apply only configuration relevant to MVC tests.
//It auto-configures MockMvc instance as well.
@WebMvcTest(controllers = TripPricerController.class)  // we are asking to initialize only one web controller
class TripPricerControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TripPricerService TripPricerService;


	@Test
	void testGetPrice() throws Exception {
		//ARRANGE:
		UUID attractionid = UUID.randomUUID();
		Provider provider1 = new Provider(attractionid, "provider1", 100);
		Provider provider2 = new Provider(attractionid, "provider2", 200);
		List<Provider> listProvider = new ArrayList<>();
		listProvider.add(provider1);
		listProvider.add(provider2);
		when(TripPricerService.getPrice("apiKey", attractionid, 2, 5, 10, 384)).thenReturn(listProvider);
		
		//ACT:
		MvcResult result = 
				mockMvc.perform(get("/price?"
						+ "apiKey=" + "apiKey" + "&"
						+ "attractionId=" + attractionid.toString() + "&"
						+ "adults=2&"
						+ "children=5&"
						+ "nightsStay=10&"
						+ "rewardsPoints=384"
						))
				.andExpect(status().isOk())
				.andReturn(); //this allows to get the MvcResult

		//ASSERT:
		List<ProviderDTO> listProviderResult = 
				objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<ProviderDTO>>() {});
		assertEquals(2, listProviderResult.size());
		
	}
}