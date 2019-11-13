package com.techguru.trading.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techguru.trading.model.Contract;
import com.techguru.trading.model.FirstCandle;
import com.techguru.trading.repository.FirstCandleRepository;
import com.techguru.trading.service.FirstCandleService;

@Service
public class FirstCandleServiceImpl implements FirstCandleService {

	private FirstCandleRepository firstCandleRepository;

	@Autowired
	public FirstCandleServiceImpl(FirstCandleRepository firstCandleRepository) {
		this.firstCandleRepository = firstCandleRepository;
	}

	@Override
	public FirstCandle addFirstCandle(FirstCandle firstCandle) {

		return firstCandleRepository.save(firstCandle);

	}

	@Override
	public Boolean findIfFirstCandleExists(Contract contract, LocalDate tradeDate) {
		return firstCandleRepository.findFirstByTradeDateEqualsAndContractIdEquals(tradeDate, contract.getId())
				.isPresent();
	}

	@Override
	public List<FirstCandle> findTodaysFirstCandles() {
		LocalDate todaysDate = LocalDate.now();
		return firstCandleRepository.findByTradeDateEquals(todaysDate);
	}
}
