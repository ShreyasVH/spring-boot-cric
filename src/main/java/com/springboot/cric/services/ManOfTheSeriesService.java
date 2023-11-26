package com.springboot.cric.services;

import com.springboot.cric.models.ManOfTheSeries;
import com.springboot.cric.repositories.ManOfTheSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManOfTheSeriesService {
    @Autowired
    private ManOfTheSeriesRepository manOfTheSeriesRepository;

    public List<ManOfTheSeries> getBySeriesIds(List<Long> seriesIds) {
        return manOfTheSeriesRepository.findAllBySeriesIdIn(seriesIds);
    }

    public void add(Long seriesId, List<Long> playerIds) {
        List<ManOfTheSeries> manOfTheSeriesList = playerIds.stream().map(playerId -> new ManOfTheSeries(null, seriesId, playerId)).collect(Collectors.toList());
        manOfTheSeriesRepository.saveAll(manOfTheSeriesList);
    }

    public void delete(Long seriesId, List<Long> playerIds) {
        List<ManOfTheSeries> manOfTheSeriesList = manOfTheSeriesRepository.findAllBySeriesIdAndPlayerIdIn(seriesId, playerIds);
        manOfTheSeriesRepository.deleteAll(manOfTheSeriesList);
    }

    public void remove(Long seriesId)
    {
        manOfTheSeriesRepository.deleteAll(getBySeriesIds(Collections.singletonList(seriesId)));
    }
}
