package com.techguru.trading.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techguru.trading.service.ContractService;

@Component
public class ContractStatusScheduler {

	private ContractService contractService;

	@Autowired
	public ContractStatusScheduler(ContractService contractService) {
		this.contractService = contractService;
	}

	// @Scheduled(cron = "0 48 8/16 * * MON-FRI")
	public void updateOpenContractStatus() {
		contractService.updateOpenContractStatus();
	}
}
