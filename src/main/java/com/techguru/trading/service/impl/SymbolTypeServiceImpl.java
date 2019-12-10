package com.techguru.trading.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techguru.trading.model.SymbolType;
import com.techguru.trading.repository.SymbolTypeRepository;
import com.techguru.trading.service.SymbolTypeService;

@Service
public class SymbolTypeServiceImpl implements SymbolTypeService {

	private SymbolTypeRepository symbolTypeRepository;

	@Autowired
	public SymbolTypeServiceImpl(SymbolTypeRepository symbolTypeRepository) {
		this.symbolTypeRepository = symbolTypeRepository;
	}

	public SymbolType addSymbolType(SymbolType symbolType) {
		symbolType.setId(symbolType.getId().toUpperCase());
		return symbolTypeRepository.save(symbolType);
	}

	@Override
	public List<SymbolType> findAllSymbolTypes() {
		return symbolTypeRepository.findAll();
	}

}
