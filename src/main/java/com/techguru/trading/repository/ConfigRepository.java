package com.techguru.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techguru.trading.model.Config;

@Repository
public interface ConfigRepository extends JpaRepository<Config, String> {

}
