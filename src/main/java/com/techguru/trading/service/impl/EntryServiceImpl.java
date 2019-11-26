package com.techguru.trading.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techguru.trading.constants.TechTradingConstants;
import com.techguru.trading.model.Contract;
import com.techguru.trading.model.Entry;
import com.techguru.trading.model.Entry.EntryType;
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
		entry.setProfitOffset(TechTradingConstants.PROFIT_OFFSET);
		entry.setUpdatedAt(LocalDateTime.now());
		return entryRepository.save(entry);
	}

	@Override
	public List<Entry> addEntry(List<Entry> entries) {
		return entryRepository.saveAll(entries);
	}

	@Override
	public Optional<Entry> findIfActiveEntryExists(Contract contract, EntryType entryType) {
		return entryRepository.findFirstByContractIdEqualsAndIsActiveEqualsAndEntryTypeEquals(contract.getId(),
				Boolean.TRUE, entryType);
	}

	@Override
	public Optional<Entry> findLastEntry(Contract contract) {

		return entryRepository.findFirstByContractIdEqualsOrderByCreatedAtDesc(contract.getId());

	}

	@Override
	public List<Entry> findActiveEntries() {
		return entryRepository.findByIsActiveEquals(Boolean.TRUE);
	}

}
