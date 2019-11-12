package com.techguru.trading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techguru.trading.model.Entry;
import com.techguru.trading.service.EntryService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/entry")
public class EntryController {

	private EntryService entryService;

	@Autowired
	public EntryController(EntryService entryService) {
		this.entryService = entryService;
	}

	@ApiOperation(value = "Add entry details", notes = "This endpoint creates entry details for a contract")
	@PostMapping
	public ResponseEntity<Entry> addSymbol(@RequestBody Entry entry) {

		return new ResponseEntity<>(entryService.addEntry(entry), HttpStatus.OK);
	}

}
