package com.techguru.trading.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techguru.trading.model.HeikinAshiCandle;

public interface HeikinAshiCandleRepository extends JpaRepository<HeikinAshiCandle, Long> {

	public HeikinAshiCandle findFirstByContractIdEqualsOrderByTradeDateTimeDesc(String contractId);
	
	public Optional<HeikinAshiCandle> findFirstByContractIdEqualsAndTradeDateTimeEquals(String contractId, LocalDateTime dateTime);

}
