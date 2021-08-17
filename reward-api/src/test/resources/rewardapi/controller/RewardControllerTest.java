package rewardapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import rewardapi.service.RewardService;

//@WebMvcTest annotation is used for Spring MVC tests. 
//It disables full auto-configuration and instead apply only configuration relevant to MVC tests.
//It auto-configures MockMvc instance as well.
@WebMvcTest(controllers = RewardController.class)  // we are asking to initialize only one web controller
class RewardControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RewardService RewardService;


	@Test
	void testGetAttractionRewardPoints() throws Exception {
		//ARRANGE:
		UUID attractionid = UUID.randomUUID();
		UUID userid = UUID.randomUUID();
		when(RewardService.getAttractionRewardPoints(attractionid, userid)).thenReturn(999);
		
		//ACT:
		MvcResult result = 
				mockMvc.perform(get("/attractionRewardPoints?"
						+ "attractionid="+attractionid.toString()
						+ "&userid="+userid.toString()
						))
				.andExpect(status().isOk())
				.andReturn(); //this allows to get the MvcResult

		//ASSERT:
		Integer integerResult = 
				objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Integer>() {});
		assertEquals(999, integerResult);
	}
}