package com.emranhss.HRM_system;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class HrmSystemApplication {

	
	@PostConstruct
	public void setDefaultTimezone() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Dhaka"));
	}

	public static void main(String[] args) {
		SpringApplication.run(HrmSystemApplication.class, args);
	}

}
