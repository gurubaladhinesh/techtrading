package com.techguru.trading.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
@Table(name = "entry")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Entry {

	public enum EntryType {
		BUY, SELL;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "contract", referencedColumnName = "id")
	private Contract contract;

	@Column(name = "created_at")
	@JsonFormat(pattern = TechTradingConstants.DATETIME_FORMAT)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	@JsonFormat(pattern = TechTradingConstants.DATETIME_FORMAT)
	private LocalDateTime updatedAt;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "entry_value")
	private Double entryValue;

	@Column(name = "exit_value")
	private Double exitValue;

	@Column(name = "profit_offset")
	@NonNull
	private Double profitOffset;

	@Column(name = "entry_type")
	@NonNull
	@Enumerated(EnumType.STRING)
	private EntryType entryType;

	@NonNull
	@Column(name = "trade_date")
	@JsonFormat(pattern = TechTradingConstants.DATE_FORMAT)
	private LocalDate tradeDate;

	@Column(name = "stop_loss")
	private Double stopLoss;

}
