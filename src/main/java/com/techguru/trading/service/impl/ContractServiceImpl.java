package com.techguru.trading.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techguru.trading.model.Contract;
import com.techguru.trading.model.ContractType;
import com.techguru.trading.model.Symbol;
import com.techguru.trading.repository.ContractRepository;
import com.techguru.trading.repository.ContractTypeRepository;
import com.techguru.trading.repository.SymbolRepository;
import com.techguru.trading.service.ContractService;

@Service
public class ContractServiceImpl implements ContractService {

	private SymbolRepository symbolRepository;

	private ContractTypeRepository contractTypeRepository;

	private ContractRepository contractRepository;

	@Autowired
	public ContractServiceImpl(SymbolRepository symbolRepository, ContractTypeRepository contractTypeRepository,
			ContractRepository contractRepository) {
		this.symbolRepository = symbolRepository;
		this.contractTypeRepository = contractTypeRepository;
		this.contractRepository = contractRepository;
	}

	@Override
	public Contract addContract(Contract contract) {
		Symbol symbol = symbolRepository.findById(contract.getSymbol().getId().toUpperCase())
				.orElseThrow(NoSuchElementException::new);
		ContractType contractType = contractTypeRepository.findById(contract.getContractType().getId().toUpperCase())
				.orElseThrow(NoSuchElementException::new);
		contract.setSymbol(symbol);
		contract.setContractType(contractType);
		return contractRepository.save(contract);
	}

	@Override
	public List<Contract> findActiveContracts() {
		return contractRepository.findByIsActiveEquals(Boolean.TRUE);
	}

	@Override
	public List<Contract> findAllContracts() {
		return contractRepository.findAll();
	}

}
