package com.springboot.cric.repositories;

import com.springboot.cric.models.BowlingFigure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BowlingFigureRepository extends JpaRepository<BowlingFigure, Integer> {
}
