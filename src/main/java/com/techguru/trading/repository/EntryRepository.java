package com.techguru.trading.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techguru.trading.model.Entry;
import com.techguru.trading.model.Entry.EntryType;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {

	public Optional<Entry> findFirstByContractIdEqualsAndIsActiveEqualsAndEntryTypeEquals(String contractId,
			Boolean isActive, EntryType entryType);

	public Optional<Entry> findFirstByContractIdEqualsAndTradeDateEqualsOrderByCreatedAtDesc(String contractId,
			LocalDate date);

	public List<Entry> findByIsActiveEquals(Boolean isActive);

}
