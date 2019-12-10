package com.techguru.trading.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techguru.trading.model.ContractType;
import com.techguru.trading.service.ContractTypeService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/contracttype")
public class ContractTypeController {

	private ContractTypeService contractTypeService;

	@Autowired
	public ContractTypeController(ContractTypeService contractTypeService) {
		this.contractTypeService = contractTypeService;
	}

	@ApiOperation(value = "Add a contract type", notes = "This endpoint creates a new contract type")
	@PostMapping
	public ResponseEntity<ContractType> addContractType(@RequestBody ContractType contractType) {
		return new ResponseEntity<>(contractTypeService.addContractType(contractType), HttpStatus.OK);
	}

	@ApiOperation(value = "Finds all contract types", notes = "Finds all contract types")
	@PostMapping
	public ResponseEntity<List<ContractType>> findAllContractTypes() {
		return new ResponseEntity<>(contractTypeService.findAllContractTypes(), HttpStatus.OK);
	}

}
