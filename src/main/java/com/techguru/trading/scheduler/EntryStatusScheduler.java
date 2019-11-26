package com.techguru.trading.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techguru.trading.model.Candle;
import com.techguru.trading.model.Entry;
import com.techguru.trading.model.Entry.EntryType;
import com.techguru.trading.service.EntryService;
import com.techguru.trading.util.Utils;

@Component
public class EntryStatusScheduler extends Utils {

	private EntryService entryService;

	@Autowired
	public EntryStatusScheduler(EntryService entryService) {
		this.entryService = entryService;
	}

	// @Scheduled(fixedDelay = 10000)
	public void updateEntryStatus() {
		List<Entry> activeEntries = entryService.findActiveEntries();
		System.out.println("activeEntries::" + activeEntries.size());
		List<Entry> targetReachedEntries = new ArrayList<Entry>();
		activeEntries.forEach((entry) -> {
			JSONObject response = getInvestingApiResponse(entry.getContract());
			Candle candle = getCandle(response, 1);

			if ((EntryType.BUY.equals(entry.getEntryType()) && entry.getExitValue() <= candle.getClose())
					|| (EntryType.SELL.equals(entry.getEntryType()) && entry.getExitValue() >= candle.getClose())) {
				entry.setIsActive(Boolean.FALSE);
				entryService.addEntry(entry);
				targetReachedEntries.add(entry);
			}
		});
	}

}
