package com.techguru.trading.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@Table(name = "contract_type")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ContractType {

	@Id
	@Column(name = "id")
	@NonNull
	private String id; // Futures, Options

	@Column(name = "description")
	private String description;

}
