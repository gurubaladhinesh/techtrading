package com.techguru.trading.service;

import java.util.List;

import com.techguru.trading.model.Symbol;

public interface SymbolService {

	public Symbol addSymbol(Symbol symbol);
	
	public List<Symbol> findAllSymbols();

}
