package com.techguru.trading.service;

import java.time.LocalDate;

import com.techguru.trading.model.Contract;
import com.techguru.trading.model.FirstCandle;

public interface FirstCandleService {

	public FirstCandle addFirstCandle(FirstCandle firstCandle);

	public Boolean findIfFirstCandleExists(Contract contract, LocalDate tradeDate);

}
