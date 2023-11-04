package com.springboot.cric.repositories;

import com.springboot.cric.models.BowlingFigure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BowlingFigureRepository extends JpaRepository<BowlingFigure, Integer> {
    List<BowlingFigure> findAllByMatchPlayerIdIn(List<Integer> matchPlayerIds);
}
