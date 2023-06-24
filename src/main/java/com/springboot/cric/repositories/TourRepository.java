package com.springboot.cric.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import com.springboot.cric.models.Tour;

@Repository
public interface TourRepository extends PagingAndSortingRepository<Tour, Long> {
    Tour findByNameAndStartTime(String name, LocalDateTime startTime);
    Page<Tour> findAllByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    int countAllByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}