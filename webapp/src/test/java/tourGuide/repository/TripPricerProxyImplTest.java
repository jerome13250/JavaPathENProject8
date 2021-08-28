package tourGuide.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import com.fasterxml.jackson.databind.ObjectMapper;

import commons.model.ProviderDTO;
import lombok.extern.slf4j.Slf4j;
import tourGuide.repository.impl.TripPricerProxyImpl;
import tripPricer.Provider;

/**
 * This test code using @RestClientTest comes from 
 * <a href="https://rieckpil.de/testing-your-spring-resttemplate-with-restclienttest/">
 * Testing the Spring RestTemplate With @RestClientTest
 * </a>
 * @author jerome
 *
 */
@Slf4j
@RestClientTest(TripPricerProxyImpl.class)
class TripPricerProxyImplTest {

	@Autowired
	TripPricerProxyImpl tripPricerProxyImpl;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockRestServiceServer mockRestServiceServer;

	@Value( "${trippricerapi.apiUrl}" )
    private String tripPricerApiUrl;
	
	Date date;

	@BeforeEach
	void init() {
		//the MockRestSerivceServer provides a way to reset all expectations/recorded requests and verify that
		//every expectation was actually used. This is quite helpful and can be integrated using the JUnit test lifecycle.
		//Without this we get error: "mockRestServiceServer Cannot add more expectations after actual requests are made"
		this.mockRestServiceServer.reset();
	}
	 
	@AfterEach
	void tearDown() {
	  this.mockRestServiceServer.verify();
	}
	
	@Test
	void getAttractionTripPricerPointsTest() throws Exception {
		//ARRANGE:
		UUID attractionid = UUID.randomUUID();
		List<ProviderDTO> listProviderDTO = new ArrayList<>();
		ProviderDTO p1 = new ProviderDTO("name1",111.11, UUID.randomUUID());
		ProviderDTO p2 = new ProviderDTO("name2",222.22, UUID.randomUUID());
		listProviderDTO.add(p1);
		listProviderDTO.add(p2);
		
		String json = this.objectMapper
				.writeValueAsString(listProviderDTO);

		String url = tripPricerApiUrl 
				+ "price?"
				+ "apiKey=" + "apiKey" + "&"
				+ "attractionId=" + attractionid  + "&"
				+ "adults=" + 2 + "&"
				+ "children=" + 5 + "&"
				+ "nightsStay=" + 20 + "&"
				+ "rewardsPoints=" + 999;
		
		this.mockRestServiceServer
		.expect(requestTo(url))
		.andRespond(withSuccess(json, MediaType.APPLICATION_JSON));
		
		//ACT:
		List<Provider> result = tripPricerProxyImpl.getPrice("apiKey", attractionid, 2, 5, 20, 999);

		//ASSERT:
		assertEquals(2, result.size());
	}

}