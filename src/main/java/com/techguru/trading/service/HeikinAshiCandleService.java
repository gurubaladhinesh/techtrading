package com.techguru.trading.service;

import java.time.LocalDateTime;
import java.util.List;

import com.techguru.trading.model.Contract;
import com.techguru.trading.model.HeikinAshiCandle;

public interface HeikinAshiCandleService {

	public HeikinAshiCandle addHeikinAshiCandle(HeikinAshiCandle heikinAshiCandle);

	public List<HeikinAshiCandle> addHeikinAshiCandle(List<HeikinAshiCandle> heikinAshiCandles);

	public HeikinAshiCandle findLastHeikinAshiCandle(Contract contract);

	public Boolean findIfHeikinAshiCandleExists(Contract contract, LocalDateTime dateTime);

	public List<HeikinAshiCandle> findAllHeikinAshiCandles();

}
