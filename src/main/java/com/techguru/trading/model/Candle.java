package com.techguru.trading.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.techguru.trading.constants.TechTradingConstants;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "candle")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Builder
public class Candle {

  public enum CandlePosition {
    FIRST, LAST;
  }

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "contract", referencedColumnName = "id")
  @NonNull
  private Contract contract;

  @NonNull
  @Column(name = "trade_date")
  @JsonFormat(pattern = TechTradingConstants.DATE_FORMAT)
  private LocalDate tradeDate;

  @Column(name = "open")
  private Double open;

  @Column(name = "high")
  private Double high;

  @Column(name = "low")
  private Double low;

  @Column(name = "close")
  private Double close;

  @Column(name = "candle_position")
  @NonNull
  @Enumerated(EnumType.STRING)
  private Candle.CandlePosition candlePosition;

}
