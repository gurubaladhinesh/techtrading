package com.techguru.trading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TechTradingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechTradingApplication.class, args);
	}

}
