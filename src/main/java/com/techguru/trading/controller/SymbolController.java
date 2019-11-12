package com.techguru.trading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techguru.trading.model.Symbol;
import com.techguru.trading.service.SymbolService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/symbol")
public class SymbolController {

	private SymbolService symbolService;

	@Autowired
	public SymbolController(SymbolService symbolService) {
		this.symbolService = symbolService;
	}

	@ApiOperation(value = "Add a symbol", notes = "This endpoint creates a new symbol")
	@PostMapping
	public ResponseEntity<Symbol> addSymbol(@RequestBody Symbol symbol) {
		
		return new ResponseEntity<>(symbolService.addSymbol(symbol), HttpStatus.OK);
	}
}
