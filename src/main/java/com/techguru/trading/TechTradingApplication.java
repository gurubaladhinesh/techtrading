package com.techguru.trading;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.techguru.trading.cache.Cache;
import com.techguru.trading.model.FirstCandle;
import com.techguru.trading.service.FirstCandleService;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class TechTradingApplication {

	public static Set<FirstCandle> todaysFirstCandleSet;

	private FirstCandleService firstCandleService;

	@Autowired
	public TechTradingApplication(FirstCandleService firstCandleService) {
		this.firstCandleService = firstCandleService;
	}

	public static void main(String[] args) {
		SpringApplication.run(TechTradingApplication.class, args);
	}

	@PostConstruct
	public void init() {
		Cache.getInstance().populateTodaysFirstCandles(firstCandleService.findTodaysFirstCandles());
	}

}
