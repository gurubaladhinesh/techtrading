package com.techguru.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techguru.trading.model.HeikinAshiCandle;

public interface HeikinAshiCandleRepository extends JpaRepository<HeikinAshiCandle, Long> {

	public HeikinAshiCandle findFirstByContractIdEqualsOrderByTradeDateTimeDesc(String contractId);

}
