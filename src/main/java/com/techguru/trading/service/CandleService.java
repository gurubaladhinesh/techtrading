package com.techguru.trading.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.techguru.trading.model.Candle;
import com.techguru.trading.model.Contract;

public interface CandleService {

	public Candle addCandle(Candle candle);
	
	public Optional<Candle> findFirstCandle(Contract contract, LocalDate tradeDate);

	public Optional<Candle> findLastCandle(Contract contract, LocalDate tradeDate);

	public Optional<Candle> findLastCandle(Contract contract);
	
	public List<Candle> findAllCandles();

}
