package com.springboot.cric.repositories;

import com.springboot.cric.models.ManOfTheSeries;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManOfTheSeriesRepository extends JpaRepository<ManOfTheSeries, Long> {
    List<ManOfTheSeries> findAllBySeriesIdIn(List<Long> seriesIds);
    List<ManOfTheSeries> findAllBySeriesIdAndPlayerIdIn(Long seriesId, List<Long> playerIds);
}
