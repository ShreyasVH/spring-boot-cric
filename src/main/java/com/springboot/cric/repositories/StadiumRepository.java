package com.springboot.cric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.cric.models.Stadium;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium, Long> {
    Stadium findByNameAndCountryId(String name, Long countryId);
}