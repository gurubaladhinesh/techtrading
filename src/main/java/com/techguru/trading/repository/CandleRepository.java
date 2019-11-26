package com.techguru.trading.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techguru.trading.model.Candle;

@Repository
public interface CandleRepository extends JpaRepository<Candle, Long> {

	public Optional<Candle> findFirstByTradeDateEqualsAndContractIdEquals(LocalDate tradeDate, String contractId);
	
	public List<Candle> findByTradeDateEquals(LocalDate tradeDate);

}
