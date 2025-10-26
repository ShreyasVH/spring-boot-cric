package com.springboot.cric.repositories;

import com.springboot.cric.models.Total;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TotalsRepository extends JpaRepository<Total, Integer> {
    List<Total> findAllByMatchId(Integer matchId);
}
