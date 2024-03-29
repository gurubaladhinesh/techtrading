package com.techguru.trading.service;

import java.util.List;

import com.techguru.trading.model.Contract;

public interface ContractService {

	public Contract addContract(Contract contract);
	
	public List<Contract> findActiveContracts();
	
	public List<Contract> findAllContracts();

}
