package com.techguru.trading.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.techguru.trading.model.Contract;
import com.techguru.trading.service.ContractService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ContractStatusScheduler {

	private ContractService contractService;

	@Autowired
	public ContractStatusScheduler(ContractService contractService) {
		this.contractService = contractService;
	}

	@Scheduled(cron = "0 0 6 * * MON-FRI")
	public void updateOpenContractStatus() {
		List<Contract> disabledOpenContracts = contractService.updateOpenContractStatus();
		log.info("List of disabled open contracts: ", disabledOpenContracts);
	}
}
