package com.techguru.trading.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techguru.trading.model.ContractType;
import com.techguru.trading.repository.ContractTypeRepository;
import com.techguru.trading.service.ContractTypeService;

@Service
public class ContractTypeServiceImpl implements ContractTypeService {

	private ContractTypeRepository contractTypeRepository;

	@Autowired
	public ContractTypeServiceImpl(ContractTypeRepository contractTypeRepository) {
		this.contractTypeRepository = contractTypeRepository;
	}

	@Override
	public ContractType addContractType(ContractType contractType) {
		contractType.setId(contractType.getId().toUpperCase());
		return contractTypeRepository.save(contractType);

	}

	@Override
	public List<ContractType> findAllContractTypes() {
		return contractTypeRepository.findAll();
	}

}
