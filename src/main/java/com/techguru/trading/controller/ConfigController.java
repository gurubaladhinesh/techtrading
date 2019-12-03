package com.techguru.trading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techguru.trading.model.Config;
import com.techguru.trading.service.ConfigService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/config")
public class ConfigController {

	private ConfigService configService;

	@Autowired
	public ConfigController(ConfigService configService) {
		this.configService = configService;
	}

	@ApiOperation(value = "Add config details", notes = "This endpoint creates configuration")
	@PostMapping
	public ResponseEntity<Config> addConfig(@RequestBody Config config) {

		return new ResponseEntity<>(configService.addConfig(config), HttpStatus.OK);
	}

}
