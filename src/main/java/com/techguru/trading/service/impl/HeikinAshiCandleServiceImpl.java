package com.techguru.trading.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techguru.trading.model.Contract;
import com.techguru.trading.model.HeikinAshiCandle;
import com.techguru.trading.repository.ContractRepository;
import com.techguru.trading.repository.HeikinAshiCandleRepository;
import com.techguru.trading.service.HeikinAshiCandleService;

@Service
public class HeikinAshiCandleServiceImpl implements HeikinAshiCandleService {

	private HeikinAshiCandleRepository heikinAshiCandleRepository;

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	public HeikinAshiCandleServiceImpl(HeikinAshiCandleRepository heikinAshiCandleRepository) {
		this.heikinAshiCandleRepository = heikinAshiCandleRepository;
	}

	@Override
	public HeikinAshiCandle addHeikinAshiCandle(HeikinAshiCandle heikinAshiCandle) {

		Contract contract = contractRepository.findById(heikinAshiCandle.getContract().getId()).orElseThrow();
		heikinAshiCandle.setContract(contract);

		return heikinAshiCandleRepository.save(heikinAshiCandle);
	}

	@Override
	public List<HeikinAshiCandle> addHeikinAshiCandle(List<HeikinAshiCandle> heikinAshiCandles) {
		return heikinAshiCandleRepository.saveAll(heikinAshiCandles);
	}

	@Override
	public HeikinAshiCandle findLastHeikinAshiCandle(Contract contract) {
		return heikinAshiCandleRepository.findFirstByContractIdEqualsOrderByTradeDateTimeDesc(contract.getId());
	}

	@Override
	public Boolean findIfHeikinAshiCandleExists(Contract contract, LocalDateTime dateTime) {
		return heikinAshiCandleRepository.findFirstByContractIdEqualsAndTradeDateTimeEquals(contract.getId(), dateTime)
				.isPresent();
	}

	@Override
	public List<HeikinAshiCandle> findAllHeikinAshiCandles() {
		return heikinAshiCandleRepository.findAll();
	}

}
