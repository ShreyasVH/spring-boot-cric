package com.springboot.cric.repositories;

import com.springboot.cric.models.BattingScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BattingScoreRepository extends JpaRepository<BattingScore, Integer> {
}
