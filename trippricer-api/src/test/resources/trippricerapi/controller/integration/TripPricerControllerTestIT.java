package trippricerapi.controller.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import commons.model.ProviderDTO;

@SpringBootTest
@AutoConfigureMockMvc
class TripPricerControllerTestIT {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;


	@Test
	void testGetPrice() throws Exception {
		//ARRANGE:
		UUID attractionid = UUID.randomUUID();

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
		assertEquals(5, listProviderResult.size());
	}
}