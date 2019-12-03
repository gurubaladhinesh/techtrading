package com.techguru.trading.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "config")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Config {

	@Id
	@Column(name = "key")
	private String key;

	@Column(name = "value")
	private String value;

}
