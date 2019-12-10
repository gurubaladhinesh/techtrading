package com.techguru.trading.service;

import java.util.List;

import com.techguru.trading.model.ContractType;

public interface ContractTypeService {
	
	public ContractType addContractType(ContractType contractType);
	
	public List<ContractType> findAllContractTypes();

}
