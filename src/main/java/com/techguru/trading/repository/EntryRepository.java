package com.techguru.trading.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techguru.trading.model.Entry;
import com.techguru.trading.model.Entry.EntryType;

public interface EntryRepository extends JpaRepository<Entry, Long> {

	public Optional<Entry> findFirstByContractIdEqualsAndIsActiveEqualsAndEntryTypeEquals(String contractId,
			Boolean isActive, EntryType entryType);

	public Optional<Entry> findFirstByContractIdEqualsOrderByCreatedAtDesc(String contractId);
	
	public List<Entry> findByIsActiveEquals(Boolean isActive);

}
