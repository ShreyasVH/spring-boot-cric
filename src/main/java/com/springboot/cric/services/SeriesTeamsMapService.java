package com.springboot.cric.services;

import com.springboot.cric.models.SeriesTeamsMap;
import com.springboot.cric.repositories.SeriesTeamsMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeriesTeamsMapService {
    @Autowired
    private SeriesTeamsMapRepository seriesTeamsMapRepository;

    public void create(Long seriesId, List<Long> teamIds) {
        List<SeriesTeamsMap> seriesTeamsMaps = teamIds.stream().map(teamId -> new SeriesTeamsMap(null, seriesId, teamId)).collect(Collectors.toList());
        seriesTeamsMapRepository.saveAll(seriesTeamsMaps);
    }

    public List<SeriesTeamsMap> getBySeriesIds(List<Long> seriesIds) {
        return seriesTeamsMapRepository.findAllBySeriesIdIn(seriesIds);
    }
}
