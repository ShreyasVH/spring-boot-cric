package com.springboot.cric.repositories;

import com.springboot.cric.models.BattingScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BattingScoreRepository extends JpaRepository<BattingScore, Integer> {
    List<BattingScore> findAllByMatchPlayerIdIn(List<Integer> matchPlayerIds);
}
