package com.springboot.cric.repositories;

import com.springboot.cric.models.MatchPlayerMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchPlayerMapRepository extends JpaRepository<MatchPlayerMap, Integer> {
    List<MatchPlayerMap> findAllByMatchId(Integer matchId);
}
