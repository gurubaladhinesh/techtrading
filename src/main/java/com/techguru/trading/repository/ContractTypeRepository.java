package com.techguru.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techguru.trading.model.ContractType;

public interface ContractTypeRepository extends JpaRepository<ContractType, String>{

}
