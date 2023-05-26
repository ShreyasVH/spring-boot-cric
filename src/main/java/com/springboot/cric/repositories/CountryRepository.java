package com.springboot.cric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.springboot.cric.models.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
	Country findByName(String name);
	List<Country> findByNameContainingIgnoringCase(String name);
	List<Country> findByIdIn(List<Long> ids);
}