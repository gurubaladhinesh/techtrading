package com.techguru.trading.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techguru.trading.model.Contract;

public interface ContractRepository extends JpaRepository<Contract, String> {
	
	public List<Contract> findByIsActiveEquals(Boolean isActive);
	
	public List<Contract> findByIsActiveEqualsAndEndDateLessThan(Boolean isActive, LocalDate date);

}
