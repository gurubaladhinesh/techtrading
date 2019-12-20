package com.techguru.trading.service.impl;

import com.techguru.trading.model.Candle.CandlePosition;
import com.techguru.trading.model.Entry;
import com.techguru.trading.model.Entry.EntryType;
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
	public Optional<Candle> findFirstCandle(Contract contract, LocalDate tradeDate) {
		return candleRepository.findFirstByTradeDateEqualsAndContractIdEqualsAndCandlePositionEquals(tradeDate, contract.getId(),
				CandlePosition.FIRST);
	}

	@Override
	public Optional<Candle> findLastCandle(Contract contract, LocalDate tradeDate) {
		return candleRepository.findFirstByTradeDateEqualsAndContractIdEqualsAndCandlePositionEquals(tradeDate, contract.getId(),
				CandlePosition.LAST);
	}

	@Override
	public Optional<Candle> findLastCandle(Contract contract) {
		return candleRepository.findFirstByContractIdEqualsOrderByTradeDateDesc(contract.getId());
	}

	@Override
	public List<Candle> findAllCandles() {
		return candleRepository.findAll();
	}
}
