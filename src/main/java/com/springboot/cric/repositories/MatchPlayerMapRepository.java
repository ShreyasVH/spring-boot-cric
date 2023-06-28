package com.springboot.cric.repositories;

import com.springboot.cric.models.MatchPlayerMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchPlayerMapRepository extends JpaRepository<MatchPlayerMap, Integer> {
}
