package com.techguru.trading.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techguru.trading.model.FirstCandle;

@Repository
public interface FirstCandleRepository extends JpaRepository<FirstCandle, Long> {

	public Optional<FirstCandle> findFirstByTradeDateEqualsAndContractIdEquals(LocalDate tradeDate, String contractId);

}
