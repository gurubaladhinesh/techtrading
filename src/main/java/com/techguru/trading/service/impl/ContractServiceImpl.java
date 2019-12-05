package com.techguru.trading.service.impl;

import java.time.LocalDate;
import java.util.List;

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
		Symbol symbol = symbolRepository.findById(contract.getSymbol().getId().toUpperCase()).get();
		ContractType contractType = contractTypeRepository.findById(contract.getContractType().getId().toUpperCase())
				.get();
		contract.setSymbol(symbol);
		contract.setContractType(contractType);
		return contractRepository.save(contract);
	}

	@Override
	public List<Contract> updateOpenContractStatus() {
		List<Contract> expiredContracts = contractRepository.findByIsActiveEqualsAndEndDateLessThan(Boolean.TRUE,
				LocalDate.now());
		expiredContracts.stream().forEach((contract) -> {
			contract.setIsActive(Boolean.FALSE);
		});

		return contractRepository.saveAll(expiredContracts);
	}

	@Override
	public List<Contract> findActiveContracts() {
		return contractRepository.findByIsActiveEquals(Boolean.TRUE);
	}

}
