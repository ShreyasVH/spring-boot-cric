package com.springboot.cric.repositories;

import com.springboot.cric.models.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Integer> {
    Series findByNameAndTourIdAndGameTypeId(String name, Long tourId, Integer gameTypeId);
    List<Series> findAllByTourIdOrderByStartTimeDesc(Long tourId);
}
