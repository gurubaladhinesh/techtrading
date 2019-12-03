package com.techguru.trading.service.impl;

import org.springframework.stereotype.Service;

import com.techguru.trading.model.Config;
import com.techguru.trading.repository.ConfigRepository;
import com.techguru.trading.service.ConfigService;

@Service
public class ConfigServiceImpl implements ConfigService {

	private ConfigRepository configRepository;

	public ConfigServiceImpl(ConfigRepository configRepository) {
		this.configRepository = configRepository;
	}

	@Override
	public Config addConfig(Config config) {

		return configRepository.save(config);

	}

}
