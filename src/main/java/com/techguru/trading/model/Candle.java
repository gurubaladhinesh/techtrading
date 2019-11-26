package com.techguru.trading.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.techguru.trading.constants.TechTradingConstants;

import lombok.AllArgsConstructor;
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
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Candle {

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
	@JsonFormat(pattern=TechTradingConstants.DATE_FORMAT)
	private LocalDate tradeDate;
	
	@Column(name = "open")
	@NonNull
	private Double open;

	@Column(name = "high")
	@NonNull
	private Double high;

	@Column(name = "low")
	@NonNull
	private Double low;
	
	@Column(name = "close")
	@NonNull
	private Double close;

}
