package gpsapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gpsUtil.GpsUtil;

@Configuration
public class Config {

	@Bean
	public GpsUtil getGpsUtil() {
		return new GpsUtil();
	}
	
}
