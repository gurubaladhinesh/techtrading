package com.techguru.trading.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.techguru.trading.model.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {

	@Transactional
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("UPDATE Contract set is_active = false where end_date < current_timestamp and is_active = true")
	public List<Contract> updateOpenContractStatus();
	
	public List<Contract> findByIsActiveEquals(Boolean isActive);

}
