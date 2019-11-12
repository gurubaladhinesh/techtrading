package com.techguru.trading.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name="symbol")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Symbol {
	
	@Id
	@Column(name="id")
	@NonNull
	private String id; //INFY,IDFC
	
	@Column(name="company")
	private String company;

	@Column(name="description")
	private String description;
	
	@ManyToOne
	@JoinColumn(name="symbol_type", referencedColumnName="id")
	private SymbolType symbolType;
}
