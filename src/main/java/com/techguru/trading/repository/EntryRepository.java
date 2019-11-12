package com.techguru.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techguru.trading.model.Entry;

public interface EntryRepository extends JpaRepository<Entry, Long>{

}
