package com.techguru.trading.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.techguru.trading.constants.TechTradingConstants;
import com.techguru.trading.model.Candle;
import com.techguru.trading.model.Contract;
import com.techguru.trading.model.Entry;
import com.techguru.trading.model.Entry.EntryType;
import com.techguru.trading.model.HeikinAshiCandle;
import com.techguru.trading.service.CandleService;
import com.techguru.trading.service.ContractService;
import com.techguru.trading.service.EntryService;
import com.techguru.trading.service.HeikinAshiCandleService;
import com.techguru.trading.util.Utils;

@Component
public class HeikinAshiCandleScheduler extends Utils {

	private ContractService contractService;

	private HeikinAshiCandleService heikinAshiCandleService;

	private CandleService candleService;

	private EntryService entryService;

	@Autowired
	public HeikinAshiCandleScheduler(ContractService contractService, HeikinAshiCandleService heikinAshiCandleService,
			CandleService candleService, EntryService entryService) {
		this.contractService = contractService;
		this.heikinAshiCandleService = heikinAshiCandleService;
		this.candleService = candleService;
		this.entryService = entryService;
	}

	// Updates heikin ashi candle value for active contracts every 15mins
	@Scheduled(cron = "20 0,15,30,45 9-23 ? * MON-FRI")
	// @Scheduled(fixedRate = 9000000)
	public void populateHeikinAshiCandles() {

		List<Contract> activeContracts = contractService.findActiveContracts();
		List<Entry> entries = new ArrayList<Entry>();
		activeContracts.forEach((contract) -> {

			JSONObject response = getInvestingApiResponse(contract);

			Candle normalCandle = getCandle(response, 2);
			LocalDateTime normalCandleDateTime = getLocalDateTimeFromResponse(response, 2);

			HeikinAshiCandle previousHeikinAshiCandle = heikinAshiCandleService.findLastHeikinAshiCandle(contract);

			HeikinAshiCandle heikinAshiCandle = getHeikinAshiCandle(previousHeikinAshiCandle, normalCandle);
			heikinAshiCandle.setTradeDateTime(normalCandleDateTime);

			heikinAshiCandleService.addHeikinAshiCandle(heikinAshiCandle);

			Double close = heikinAshiCandle.getClose();
			Optional<Candle> optionalFirstCandle = candleService.findCandle(heikinAshiCandle.getContract(),
					LocalDate.now());
			Candle firstCandle = null;
			// If First candle does not exists, it is the first heikin ashi candle for the
			// day
			if (optionalFirstCandle.isPresent()) {
				firstCandle = optionalFirstCandle.get();
			} else {
				if (heikinAshiCandle.getTradeDateTime().toLocalDate().isEqual(LocalDate.now())) {
					Candle candle = new Candle();
					candle.setContract(heikinAshiCandle.getContract());
					candle.setTradeDate(heikinAshiCandle.getTradeDateTime().toLocalDate());
					candle.setOpen(heikinAshiCandle.getOpen());
					candle.setHigh(heikinAshiCandle.getHigh());
					candle.setLow(heikinAshiCandle.getLow());
					candle.setClose(heikinAshiCandle.getClose());
					candle = candleService.addCandle(candle);
					firstCandle = candle;
				}
			}
			if (firstCandle != null) {
				Candle currentNormalCandle = getCandle(response, 1);

				if (LocalDateTime.now().getHour() < 22) {
					Optional<Entry> optionalLastEntry = entryService.findLastEntry(contract);
					if (close >= (firstCandle.getHigh() + TechTradingConstants.ENTRY_OFFSET)) {
						if ((!optionalLastEntry.isPresent()) || (optionalLastEntry.isPresent()
								&& Boolean.FALSE.equals(optionalLastEntry.get().getIsActive())
								&& EntryType.SELL.equals(optionalLastEntry.get().getEntryType()))) {
							entries.add(createEntry(contract, EntryType.BUY, currentNormalCandle.getClose(),
									roundTwoDecimalPlaces(currentNormalCandle.getClose() + PROFIT_OFFSET)));
						}
					} else if (close <= (firstCandle.getLow() - TechTradingConstants.ENTRY_OFFSET)) {
						if ((!optionalLastEntry.isPresent()) || (optionalLastEntry.isPresent()
								&& Boolean.FALSE.equals(optionalLastEntry.get().getIsActive())
								&& EntryType.BUY.equals(optionalLastEntry.get().getEntryType()))) {
							entries.add(createEntry(contract, EntryType.SELL, currentNormalCandle.getClose(),
									roundTwoDecimalPlaces(currentNormalCandle.getClose() - PROFIT_OFFSET)));
						}
					}
				}
			}

		});
		if (!entries.isEmpty()) {
			entryService.addEntry(entries);
			sendEmail(entries);
		}

	}

	private Entry createEntry(Contract contract, EntryType entryType, Double entryValue, Double exitValue) {
		Entry entry = new Entry();
		entry.setContract(contract);
		entry.setCreatedAt(LocalDateTime.now());
		entry.setUpdatedAt(LocalDateTime.now());
		entry.setEntryValue(entryValue);
		entry.setExitValue(exitValue);
		entry.setIsActive(Boolean.FALSE);
		entry.setEntryType(entryType);
		return entryService.addEntry(entry);
	}

}
