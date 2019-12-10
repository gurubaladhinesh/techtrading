package com.techguru.trading.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.techguru.trading.model.Candle;
import com.techguru.trading.model.Contract;

public interface CandleService {

	public Candle addCandle(Candle candle);

	public Boolean findIfCandleExists(Contract contract, LocalDate tradeDate);

	public List<Candle> findCandles(LocalDate tradeDate);
	
	public Optional<Candle> findCandle(Contract contract, LocalDate tradeDate);
	
	public List<Candle> findAllCandles();

}
