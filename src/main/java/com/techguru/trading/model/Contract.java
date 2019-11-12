package com.techguru.trading.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "contract")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Contract {

	@Id
	@NonNull
	@Column(name = "id")
	private String id;

	@Column(name = "description")
	private String description;

	@Column(name = "start_date")
	@JsonFormat(pattern = TechTradingConstants.DATETIME_FORMAT)
	private LocalDateTime startDate;

	@NonNull
	@Column(name = "end_date")
	@JsonFormat(pattern = TechTradingConstants.DATETIME_FORMAT)
	private LocalDateTime endDate;

	@ManyToOne
	@JoinColumn(name = "contract_type", referencedColumnName = "id")
	@NonNull
	private ContractType contractType;

	@ManyToOne
	@JoinColumn(name = "symbol", referencedColumnName = "id")
	@NonNull
	private Symbol symbol;

	@Column(name = "is_active", columnDefinition = "boolean default true")
	private Boolean isActive;

	@NonNull
	@Column(name = "pair_id")
	private String pairId;

	@NonNull
	@Column(name = "referer_url")
	private String refererUrl;

}
