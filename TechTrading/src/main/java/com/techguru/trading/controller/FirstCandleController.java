package com.techguru.trading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techguru.trading.model.FirstCandle;
import com.techguru.trading.service.FirstCandleService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/firstcandle")
public class FirstCandleController {

	private FirstCandleService firstCandleService;

	@Autowired
	public FirstCandleController(FirstCandleService firstCandleService) {
		this.firstCandleService = firstCandleService;
	}

	@ApiOperation(value = "Add first candle details", notes = "This endpoint creates first candle details for a trade day")
	@PostMapping
	public ResponseEntity<FirstCandle> addSymbol(@RequestBody FirstCandle firstCandle) {

		return new ResponseEntity<>(firstCandleService.addFirstCandle(firstCandle), HttpStatus.OK);
	}

}
