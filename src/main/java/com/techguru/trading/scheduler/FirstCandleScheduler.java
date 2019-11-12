package com.techguru.trading.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.techguru.trading.model.Contract;
import com.techguru.trading.model.FirstCandle;
import com.techguru.trading.service.ContractService;
import com.techguru.trading.service.FirstCandleService;
import com.techguru.trading.util.Utils;

@Component
public class FirstCandleScheduler extends Utils {

	private FirstCandleService firstCandleService;
	private ContractService contractService;

	@Autowired
	public FirstCandleScheduler(FirstCandleService firstCandleService, ContractService contractService) {
		this.firstCandleService = firstCandleService;
		this.contractService = contractService;
	}

	@Scheduled(cron = "1 6 9,17 * * MON-FRI")
	public void getFirstCandle() {
		List<Contract> activeContracts = contractService.findActiveContracts();

		activeContracts.stream().forEach((contract) -> {
			if (!firstCandleService.findIfFirstCandleExists(contract, LocalDate.now())) {
				FirstCandle firstCandle = getFirstCandle(contract);
				firstCandleService.addFirstCandle(firstCandle);
			}
		}
		);

	}

}
