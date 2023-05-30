package com.springboot.cric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.cric.models.TeamType;

@Repository
public interface TeamTypeRepository extends JpaRepository<TeamType, Integer> {
}
