package com.techguru.trading.service;

import java.util.List;

import com.techguru.trading.model.SymbolType;

public interface SymbolTypeService {

	public SymbolType addSymbolType(SymbolType symbolType);

	public List<SymbolType> findAllSymbolTypes();

}
