package tourGuide.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Date;
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

import tourGuide.repository.impl.RewardProxyImpl;

/**
 * This test code using @RestClientTest comes from 
 * <a href="https://rieckpil.de/testing-your-spring-resttemplate-with-restclienttest/">
 * Testing the Spring RestTemplate With @RestClientTest
 * </a>
 * @author jerome
 *
 */
@RestClientTest(RewardProxyImpl.class)
class RewardProxyImplTest {

	@Autowired
	RewardProxyImpl rewardProxyImpl;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockRestServiceServer mockRestServiceServer;

	@Value( "${rewardapi.apiUrl}" )
    private String rewardApiUrl;
	
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
	void getAttractionRewardPointsTest() throws Exception {
		//ARRANGE:
		UUID userid = UUID.randomUUID();
		UUID attractionid = UUID.randomUUID();
		
		String json = this.objectMapper
				.writeValueAsString(new Integer(999));

		String url = rewardApiUrl 
    			+ "attractionRewardPoints?"
    			+ "attractionid=" + attractionid + "&"
    			+ "userid=" + userid;
		
		this.mockRestServiceServer
		.expect(requestTo(url))
		.andRespond(withSuccess(json, MediaType.APPLICATION_JSON));
		
		//ACT:
		Integer result = rewardProxyImpl.getAttractionRewardPoints(attractionid, userid);

		//ASSERT:
		assertEquals(999, result);
	}

}