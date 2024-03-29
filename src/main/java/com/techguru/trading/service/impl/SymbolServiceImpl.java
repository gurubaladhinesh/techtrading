package com.techguru.trading.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techguru.trading.model.Symbol;
import com.techguru.trading.model.SymbolType;
import com.techguru.trading.repository.SymbolRepository;
import com.techguru.trading.repository.SymbolTypeRepository;
import com.techguru.trading.service.SymbolService;

@Service
public class SymbolServiceImpl implements SymbolService {

	private SymbolRepository symbolRepository;

	private SymbolTypeRepository symbolTypeRepository;

	@Autowired
	public SymbolServiceImpl(SymbolRepository symbolRepository, SymbolTypeRepository symbolTypeRepository) {
		this.symbolRepository = symbolRepository;
		this.symbolTypeRepository = symbolTypeRepository;
	}

	@Override
	public Symbol addSymbol(Symbol symbol) {
		SymbolType symbolType = symbolTypeRepository.findById(symbol.getSymbolType().getId().toUpperCase())
				.orElseThrow(NoSuchElementException::new);
		symbol.setId(symbol.getId().toUpperCase());
		symbol.setSymbolType(symbolType);
		return symbolRepository.save(symbol);
	}

	@Override
	public List<Symbol> findAllSymbols() {
		return symbolRepository.findAll();
	}

}
