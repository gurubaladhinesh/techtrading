package com.techguru.trading.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techguru.trading.model.FirstCandle;
import com.techguru.trading.service.FirstCandleService;

@Component

public class Cache {

	private static Cache instance;

	public static List<FirstCandle> todaysFirstCandles;

	private Cache() {
	}

	public static Cache getInstance() {
		if (instance == null) {
			synchronized (Cache.class) {
				if (instance == null) {
					instance = new Cache();
				}

			}
		}
		return instance;
	}

	public void populateTodaysFirstCandles(List<FirstCandle> todaysFirstCandles) {
		Cache.todaysFirstCandles = todaysFirstCandles;
		System.out.println(Cache.todaysFirstCandles);
	}

}