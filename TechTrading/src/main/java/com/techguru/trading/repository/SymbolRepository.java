package com.techguru.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techguru.trading.model.Symbol;

@Repository
public interface SymbolRepository  extends JpaRepository<Symbol, String>{

}
