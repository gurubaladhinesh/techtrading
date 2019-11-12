package com.techguru.trading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techguru.trading.model.Contract;
import com.techguru.trading.service.ContractService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/contract")
public class ContractController {

	private ContractService contractService;

	@Autowired
	public ContractController(ContractService contractService) {
		this.contractService = contractService;
	}

	@ApiOperation(value = "Add a contract", notes = "This endpoint creates a new contract")
	@PostMapping
	public ResponseEntity<Contract> addSymbol(@RequestBody Contract contract) {

		return new ResponseEntity<>(contractService.addContract(contract), HttpStatus.OK);
	}
}
