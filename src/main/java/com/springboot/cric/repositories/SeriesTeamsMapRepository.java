package com.springboot.cric.repositories;

import com.springboot.cric.models.SeriesTeamsMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeriesTeamsMapRepository extends JpaRepository<SeriesTeamsMap, Long> {
    List<SeriesTeamsMap> findAllBySeriesIdIn(List<Long> seriesIds);
    List<SeriesTeamsMap> findAllBySeriesIdAndTeamIdIn(Long seriesId, List<Long> teamIds);
}
