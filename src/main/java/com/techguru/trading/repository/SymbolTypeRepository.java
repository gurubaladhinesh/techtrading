package com.techguru.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techguru.trading.model.SymbolType;

@Repository
public interface SymbolTypeRepository extends JpaRepository<SymbolType, String> {

}
