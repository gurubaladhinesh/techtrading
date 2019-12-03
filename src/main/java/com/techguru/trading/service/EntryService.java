package com.techguru.trading.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.techguru.trading.model.Contract;
import com.techguru.trading.model.Entry;
import com.techguru.trading.model.Entry.EntryType;

public interface EntryService {

	public Entry addEntry(Entry entry);

	public List<Entry> addEntry(List<Entry> entries);

	public Optional<Entry> findIfActiveEntryExists(Contract contract, EntryType entryType);

	public Optional<Entry> findLastEntry(Contract contract, LocalDate date);
	
	public List<Entry> findActiveEntries();

}
