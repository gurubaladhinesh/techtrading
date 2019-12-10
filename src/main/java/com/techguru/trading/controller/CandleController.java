package com.techguru.trading.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techguru.trading.model.Candle;
import com.techguru.trading.service.CandleService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/candle")
public class CandleController {

	private CandleService candleService;

	@Autowired
	public CandleController(CandleService candleService) {
		this.candleService = candleService;
	}

	@ApiOperation(value = "Add candle details", notes = "This endpoint creates candle details for a trade date")
	@PostMapping
	public ResponseEntity<Candle> addCandle(@RequestBody Candle candle) {

		return new ResponseEntity<>(candleService.addCandle(candle), HttpStatus.OK);
	}

	@ApiOperation(value = "Get all candle details", notes = "This endpoint gets all candle details")
	@GetMapping
	public ResponseEntity<List<Candle>> findAllCandles() {

		return new ResponseEntity<>(candleService.findAllCandles(), HttpStatus.OK);
	}
}
