package com.springboot.cric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import com.springboot.cric.models.Tour;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    Tour findByNameAndStartTime(String name, LocalDateTime startTime);
}