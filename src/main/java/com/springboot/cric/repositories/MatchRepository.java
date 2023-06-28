package com.springboot.cric.repositories;

import com.springboot.cric.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface MatchRepository extends JpaRepository<Match, Integer> {
    Match findByStadiumIdAndStartTime(Long stadiumId, LocalDateTime startTime);
}
