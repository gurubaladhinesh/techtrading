package com.techguru.trading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techguru.trading.model.HeikinAshiCandle;
import com.techguru.trading.service.HeikinAshiCandleService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/hkcandle")
public class HeikinAshiCandleController {

	private HeikinAshiCandleService heikinAshiCandleService;

	@Autowired
	public HeikinAshiCandleController(HeikinAshiCandleService heikinAshiCandleService) {
		this.heikinAshiCandleService = heikinAshiCandleService;
	}

	@ApiOperation(value = "Add eikin ashi candle details", notes = "This endpoint creates heikin ashi candle details for a trade date time")
	@PostMapping
	public ResponseEntity<HeikinAshiCandle> addCandle(@RequestBody HeikinAshiCandle candle) {

		return new ResponseEntity<>(heikinAshiCandleService.addHeikinAshiCandle(candle), HttpStatus.OK);
	}

}
