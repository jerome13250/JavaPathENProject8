package rewardapi.controller.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class RewardControllerTestIT {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	
	@Test
	void testGetAttractionRewardPoints() throws Exception {
		//ARRANGE:
		UUID attractionid = UUID.randomUUID();
		UUID userid = UUID.randomUUID();
		
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
		assertNotNull(integerResult);
	}
}