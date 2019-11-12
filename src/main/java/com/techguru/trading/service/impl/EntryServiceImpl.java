package com.techguru.trading.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techguru.trading.constants.TechTradingConstants;
import com.techguru.trading.model.Contract;
import com.techguru.trading.model.Entry;
import com.techguru.trading.repository.ContractRepository;
import com.techguru.trading.repository.EntryRepository;
import com.techguru.trading.service.EntryService;

@Service
public class EntryServiceImpl implements EntryService {

	private EntryRepository entryRepository;
	private ContractRepository contractRepository;

	@Autowired
	public EntryServiceImpl(EntryRepository entryRepository, ContractRepository contractRepository) {
		this.entryRepository = entryRepository;
		this.contractRepository = contractRepository;
	}

	@Override
	public Entry addEntry(Entry entry) {
		Contract contract = contractRepository.findById(entry.getContract().getId()).orElseThrow();
		entry.setContract(contract);
		entry.setCreatedAt(LocalDateTime.now());
		entry.setEntryValue(entry.getEntryValue());
		entry.setIsActive(entry.getIsActive());
		entry.setProfitOffset(TechTradingConstants.PROFIT_OFFSET);
		entry.setUpdatedAt(LocalDateTime.now());
		return entryRepository.save(entry);
	}

}
