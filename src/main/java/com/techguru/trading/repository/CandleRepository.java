package com.techguru.trading.repository;

import com.techguru.trading.model.Candle;
import com.techguru.trading.model.Candle.CandlePosition;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandleRepository extends JpaRepository<Candle, Long> {

  public Optional<Candle> findFirstByTradeDateEqualsAndContractIdEqualsAndCandlePositionEquals(
      LocalDate tradeDate, String contractId, CandlePosition candlePosition);

  public Optional<Candle> findFirstByContractIdEqualsOrderByTradeDateDesc(String contractId);

}
