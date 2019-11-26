package com.techguru.trading.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techguru.trading.model.Candle;
import com.techguru.trading.model.Contract;
import com.techguru.trading.repository.CandleRepository;
import com.techguru.trading.repository.ContractRepository;
import com.techguru.trading.service.CandleService;

@Service
public class CandleServiceImpl implements CandleService {

	private CandleRepository candleRepository;

	private ContractRepository contractRepository;

	@Autowired
	public CandleServiceImpl(CandleRepository candleRepository, ContractRepository contractRepository) {
		this.candleRepository = candleRepository;
		this.contractRepository = contractRepository;
	}

	@Override
	public Candle addCandle(Candle candle) {

		Contract contract = contractRepository.findById(candle.getContract().getId()).orElseThrow();
		candle.setContract(contract);

		return candleRepository.save(candle);

	}

	@Override
	public Boolean findIfCandleExists(Contract contract, LocalDate tradeDate) {
		return candleRepository.findFirstByTradeDateEqualsAndContractIdEquals(tradeDate, contract.getId()).isPresent();
	}

	@Override
	public List<Candle> findCandles(LocalDate tradeDate) {
		return candleRepository.findByTradeDateEquals(tradeDate);
	}

	@Override
	public Optional<Candle> findCandle(Contract contract, LocalDate tradeDate) {
		return candleRepository.findFirstByTradeDateEqualsAndContractIdEquals(tradeDate, contract.getId());
	}
}
