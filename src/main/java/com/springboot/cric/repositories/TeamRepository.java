package com.springboot.cric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.cric.models.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByNameAndCountryIdAndTypeId(String name, Long countryId, Integer typeId);
}
