package gpsapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gpsUtil.GpsUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class Config {

	@Bean
	public GpsUtil getGpsUtil() {
		log.info("@Configuration create GpsUtil");
		return new GpsUtil();
	}
		
}
